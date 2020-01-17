package com.entity;

import java.util.Set;

import javax.persistence.*;

@Entity
@Table(name="BankTransactions")
public class BankTransaction 
{
	@Id @GeneratedValue(strategy=GenerationType.IDENTITY)
	private int transactionId;
	
	@Basic
	private double amount;
	
	@ManyToMany(mappedBy="transactions")
	private Set<Account> accounts;
	
	public BankTransaction() 
	{
		
	}

	public BankTransaction(double amount) {
		super();
		this.amount = amount;
	}

	public int getTransactionId() {
		return transactionId;
	}

	public void setTransactionId(int transactionId) {
		this.transactionId = transactionId;
	}

	public double getAmount() {
		return amount;
	}

	public void setAmount(double amount) {
		this.amount = amount;
	}
	
	
	
}
