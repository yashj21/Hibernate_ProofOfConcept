package com.entity;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.*;

import org.hibernate.annotations.NamedQuery;

@Entity
@Table(name="accounts")
@Inheritance(strategy=InheritanceType.JOINED)
@NamedQuery(name="fetchAccountByBalance", query="from Account as a where a.balance> :bal")
public class Account 
{	
	@Id @GeneratedValue(strategy=GenerationType.IDENTITY)
	private int accountId;
	
	@Basic
	private double balance;
	
	@ManyToOne(targetEntity=Customer.class, fetch=FetchType.LAZY, cascade={CascadeType.MERGE, CascadeType.PERSIST})		//all will try to delete customer also
	@JoinColumn(name="custId")
	private Customer customer;				//all is fine for parent to child
	
	
	/**
	 * JoinTable(ACC_TRANS)
	 * 			accId
 * 				transId
	 */
	
	@ManyToMany(fetch=FetchType.LAZY, cascade={CascadeType.ALL})
	@JoinTable(name="ACC_TRANS", joinColumns={@JoinColumn(name="accId")}, inverseJoinColumns={@JoinColumn(name="transId")})
	private Set<BankTransaction> transactions = new HashSet<>();
	
	public Set<BankTransaction> getTransactions() {
		return transactions;
	}

	public void setTransactions(Set<BankTransaction> transactions) {
		this.transactions = transactions;
	}

	public Customer getCustomer() {
		return customer;
	}

	public void setCustomer(Customer customer) {
		this.customer = customer;
	}

	public Account() 
	{
		
	}

	public Account(double balance) {
		super();
		this.balance = balance;
	}

	public int getAccountId() {
		return accountId;
	}

	public void setAccountId(int accountId) {
		this.accountId = accountId;
	}

	public double getBalance() {
		return balance;
	}

	public void setBalance(double balance) {
		this.balance = balance;
	}
	
	
	
}
