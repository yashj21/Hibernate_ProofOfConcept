package com.entity;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.*;


@Entity
@Table(name="customers")
public class Customer 
{
	@Id @GeneratedValue(strategy=GenerationType.IDENTITY)
	private int customerId;
	
	@ Basic
	private String name;
	
	@Temporal(TemporalType.DATE)
	private Date createdDate;
	
	@Embedded
	private Address address;
	
	@ElementCollection(fetch=FetchType.LAZY)
	@CollectionTable(name="Cust_Emails",joinColumns=@JoinColumn(name="custId"))
	@Column(name="email_Address")
	private Set<String> emails = new HashSet<>();
	
//	@OneToMany(targetEntity=Account.class, cascade={CascadeType.ALL}, fetch=FetchType.LAZY)
//	@JoinColumn(name="custId")
	@OneToMany(targetEntity=Account.class, cascade={CascadeType.ALL}, fetch=FetchType.LAZY , mappedBy="customer")  //change default order
	private Set<Account> accounts = new HashSet<>();
	
	public Set<Account> getAccounts() {
		return accounts;
	}

	public void setAccounts(Set<Account> accounts) {
		this.accounts = accounts;
	}

	public Set<String> getEmails() {
		return emails;
	}

	public void setEmails(Set<String> emails) {
		this.emails = emails;
	}

	public Customer()
	{
		
	}

	public Customer(String name, Date createdDate, Address address) {
		super();
		this.name = name;
		this.createdDate = createdDate;
		this.address = address;
	}

	public int getCustomerId() {
		return customerId;
	}

	public void setCustomerId(int customerId) {
		this.customerId = customerId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Date getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}

	public Address getAddress() {
		return address;
	}

	public void setAddress(Address address) {
		this.address = address;
	}
	
	
	
}
