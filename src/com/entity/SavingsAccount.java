package com.entity;

import javax.persistence.Entity;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;

/*
 * 
 * Table(SavingAccounts)
 * 		accId int FK
 * 		minBalance double
 */

@Entity
@Table(name="SavingAccounts")
@PrimaryKeyJoinColumn(name="accId")
public class SavingsAccount extends Account
{
	
	private double minBalance;
	
	public SavingsAccount() {
		// TODO Auto-generated constructor stub
	}

	public SavingsAccount(double balance, double minBal) {
		super(balance);
		this.minBalance = minBal;
	}

}
