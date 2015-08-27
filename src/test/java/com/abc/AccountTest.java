import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;

import com.abc.Account;
import com.abc.DateProvider;
import com.abc.Transaction;

public class AccountTest {
	
	private static final double DOUBLE_DELTA_LIBERAL = 1e-4;

	//setup accounts
	

	Account checkingAccount = new Account(Account.CHECKING);
	Account savingsAccount = new Account(Account.SAVINGS);
	
	@Before
	public void setUp() throws Exception {
		
		checkingAccount.deposit(200);
		savingsAccount.deposit(1000);
		
	}
	
	
	@Test
	
	public void checkTransferTo() {
		
		checkingAccount.transferTo(savingsAccount, 50);
		assertEquals(checkingAccount.calculateTotalInterest(false), 150, DOUBLE_DELTA_LIBERAL);
		assertEquals(savingsAccount.calculateTotalInterest(false), 1050, DOUBLE_DELTA_LIBERAL);
	}
	
    @Test
	public void checkDeposit() {
    	   
		checkingAccount.deposit(500);
		System.out.println("Checking:" + checkingAccount.calculateTotalInterest(false) + " : " +
							"Savings:" + savingsAccount.calculateTotalInterest(false));
		assertEquals(checkingAccount.calculateTotalInterest(false), 700, DOUBLE_DELTA_LIBERAL);
		
	
	}
	

    @Test
    public void savingsAccountHistorical() {
     
    	checkingAccount = new Account(Account.MAXI_SAVINGS);
     
        checkingAccount.deposit(3000.0);
        checkingAccount.deposit(1000.0);
        
        int days = 6;
        //fake in some historical transactions
        
        for (Transaction transaction: checkingAccount.getTransactions()) {
        	transaction.setTransactionDate(DateProvider.
        			getDatePast(transaction.getTransactionDate(), days));
        }
        
        System.out.println("LOG:" + checkingAccount.getDailyRate(.05) + ":" + checkingAccount.calculateTotalInterest(true)/days/4000);
        assertEquals(checkingAccount.getDailyRate(.05), checkingAccount.calculateTotalInterest(true)/days/4000, DOUBLE_DELTA_LIBERAL);
    
    }
    
    @Test
    public void savingsAccountHistoricaVariableInterest() {
    	checkingAccount = new Account(Account.MAXI_SAVINGS);
   
        checkingAccount.deposit(3000.0);
        checkingAccount.deposit(1000.0);
        
        int days = 12;
       
        //fake in some historical transactions
        
        for (Transaction transaction: checkingAccount.getTransactions()) {
        	
        	transaction.setTransactionDate(DateProvider.getDatePast(transaction.getTransactionDate(), days));
        }
        
        assertEquals(checkingAccount.getDailyRate(.05), checkingAccount.calculateTotalInterest(true)/days/4000, DOUBLE_DELTA_LIBERAL);
    
    }
    
    @Test
    public void savingsAccountHistoricaVariableInterestLow() {
    	
    	checkingAccount = new Account(Account.MAXI_SAVINGS);
   
        checkingAccount.deposit(3000.0);
        checkingAccount.deposit(1000.0);
        
        int days = 6;
        
        //fake in some historical transactions
        
        for (Transaction transaction: checkingAccount.getTransactions()) {
        	
        	transaction.setTransactionDate(DateProvider.getDatePast(transaction.getTransactionDate(), days));
        }
        
        checkingAccount.withdraw(500);
        
        assertEquals(checkingAccount.getDailyRate(Account.MAXI_SAVINGS_RATE_NON_PREMIUM), checkingAccount.calculateTotalInterest(true)/days/4000, DOUBLE_DELTA_LIBERAL);
    
    }
    
    
    @Test
    public void checkingAccountHistorical() {
     
    	checkingAccount = new Account(Account.CHECKING);
     
        checkingAccount.deposit(3000.0);
        checkingAccount.deposit(1000.0);
        
        int days = 6;
        //fake in some historical transactions
        
        for (Transaction transaction: checkingAccount.getTransactions()) {
        	transaction.setTransactionDate(DateProvider.
        			getDatePast(transaction.getTransactionDate(), days));
        }
        
        assertEquals(checkingAccount.getDailyRate(Account.CHECKING_RATE), checkingAccount.calculateTotalInterest(true)/days/4000, DOUBLE_DELTA_LIBERAL);
    
    }
    
    @Test
    public void checkingAccountSums() {
     
    	checkingAccount = new Account(Account.CHECKING);
     
        checkingAccount.deposit(3000.0);
        checkingAccount.deposit(1000.0);
        checkingAccount.withdraw(200);
        
        assertEquals(checkingAccount.sumTransactions(), 3800, DOUBLE_DELTA_LIBERAL);
    
    }
    

    
}
