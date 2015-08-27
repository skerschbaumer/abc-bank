package com.abc;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.ListIterator;

import org.apache.commons.lang.time.DateUtils;


public class Account {

	
	public static final int CHECKING = 0;
	public static final int SAVINGS = 1; 
	public static final int MAXI_SAVINGS = 2;
	
	//would not usually hard code variable rates.
	//but if they are hard coded we at least set them to static variables
	//so our tests don't break every time we change them
	
	public static final double CHECKING_RATE = .001;
	public static final double SAVINGS_RATE = .001;
	public static final double SAVINGS_RATE_PREMIUM = .002;
	public static final double MAXI_SAVINGS_RATE_NON_PREMIUM = .001;
	public static final double MAXI_SAVINGS_RATE_PREMIUM = .05;  
	
	//same with rule values
	
	public static final int INTEREST_DAYS = 10;	
	
    private final int accountType;
    
    
    public List<Transaction> transactions;

    public Account(int accountType) {
        this.accountType = accountType;
        this.transactions = new ArrayList<Transaction>();
    }

   
    public void transferTo(	Account targetAccount, 
    						double amount) throws IllegalArgumentException {
    	
    	if (targetAccount!=null) {
    		withdraw(amount);
    		targetAccount.deposit(amount);
    	}
    	
    };
    
  
    public void deposit(double amount) {
        if (amount <= 0) {
            throw new IllegalArgumentException("amount must be greater than zero");
        } else {
            transactions.add(new Transaction(amount));
        }
    }

    
    public void withdraw(double amount) {
	    if (amount <= 0) {
	        throw new IllegalArgumentException("amount must be greater than zero");
	    } else {
	        transactions.add(new Transaction(-amount));
	    }
    }
    
    private boolean checkForWithdrawals(Date date, int days) {
    	Date pastDate = DateProvider.getDatePast(DateProvider.roundDate(date), days);
    	
    	ListIterator<Transaction> li = transactions.listIterator(transactions.size());

    	// Iterate in reverse.
    	while(li.hasPrevious()) {
    		Transaction transaction = (Transaction)li.previous();
    		if (transaction.transactionDate.after(pastDate)) {
    				if (transaction.amount<0) {
    					return true;
    				}
    		} else {
    			return false;
    		}
    		return false;
    	}
    
    	return false;
    	
    }
    
    public Date getStartDate() {
    	
    	Date startDate = null;
    	double amount = 0;
    	
    	for(Transaction transaction: transactions) {
    	
    		amount += transaction.amount;
    		
    		if (amount>0) {
    			//this is our start date on positive balance
    			startDate = DateProvider.roundDate(transaction.transactionDate);
    			return startDate;
    		}
    		
    	}
    	return startDate;
    }
    
    public double getDailyRate(double rate) {
    
    		return rate/DateProvider.MONTHS/DateProvider.WEEKS/DateProvider.DAYS;
    }
    
    
    private double getInterest(Date date, double amount) {
    	
    		switch(accountType) {
		    	case SAVINGS:
			        if (amount <= 1000)
			            return amount * getDailyRate(SAVINGS_RATE);
		        else
		            return 1 + (amount-1000) * getDailyRate(SAVINGS_RATE_PREMIUM);
			    //    case SUPER_SAVINGS:
		//        if (amount <= 4000)
		//            return 20;
		    	case MAXI_SAVINGS:
		        if (checkForWithdrawals(date, INTEREST_DAYS))
		            return amount * getDailyRate(MAXI_SAVINGS_RATE_NON_PREMIUM);
				else
					return amount * getDailyRate(MAXI_SAVINGS_RATE_PREMIUM);
		    default:
		        return amount * getDailyRate(CHECKING_RATE);
    	}
    }
    	
 
    
    public double calculateTotalInterest(boolean interestOnly) {
    	double total = 0;
    	double interestTally = 0;
    	
    	//get rounded down version of today's date
    	Date today = DateProvider.roundDate(DateProvider.getInstance().now());
    	
    	//start with initial transaction, accrue interest daily until present day
    	Date firstPositiveBalanceDate = getStartDate();

    	Date currentDate = firstPositiveBalanceDate;
   	   	
    	for (Transaction transaction: transactions) {
    		if (DateUtils.isSameDay(transaction.transactionDate, currentDate)) {
    			total += transaction.amount;
    		}
    		else {
    			total += transaction.amount;
    			double beforeInterest = total;
    			total += getInterest(currentDate, total);
    			interestTally += (total-beforeInterest);
    			Date nextDate = DateProvider.getDateFuture(currentDate, 1);
    			
    			if (DateUtils.isSameDay(nextDate, transaction.transactionDate)) {
    				currentDate = transaction.transactionDate;
    			}
    			currentDate = nextDate;
    		}
    	}
    	while(!DateUtils.isSameDay(currentDate,today)) {
     		double tallyBeforeInterest = total;
    		total += getInterest(currentDate, total);
    		interestTally += (total - tallyBeforeInterest);
    		currentDate = DateProvider.getDateFuture(currentDate,  1);
    	}
     	if (interestOnly) return interestTally;
    	return total;
    }

    public double interestEarned() {
    	return calculateTotalInterest(true);
    }

    public double sumTransactions() {
    	double amount = 0.0;
         for (Transaction t: transactions)
    	           amount += t.amount;     
        return amount;
    }

    public int getAccountType() {
        return accountType;
    }

}
