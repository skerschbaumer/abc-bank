package com.abc;

import java.util.Date;

public class Transaction {
	
	public final double amount;
    
	private Date transactionDate;

	public Transaction(double amount) {
		this.amount = amount;
		this.transactionDate = DateProvider.getInstance().now();
	}
	
	public Date getTransactionDate() {
		return this.transactionDate;
	}
	
	public void setTransactionDate(Date date) {
		this.transactionDate = date;
	}

	public String toString() {
		StringBuffer buffer = new StringBuffer();
		buffer.append("[ amount:" + amount + "," + "date:" + transactionDate + "]");
		return buffer.toString();
    }
    
}
