package com.entity;

import java.util.Date;
import java.util.List;
import java.util.Scanner;
import java.util.Set;

import org.hibernate.Criteria;
import org.hibernate.LockOptions;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;
import org.hibernate.criterion.Restrictions;


public class Application 
{
	public static void main(String[] args)
	{
		Configuration configuration = new Configuration();
		//load hibernate.cfg.xml
		configuration.configure();
		
		//load persistence manager
		SessionFactory sessionFactory = configuration.buildSessionFactory();
		//createMessages(sessionFactory);
		//fetchMessages(sessionFactory);
		sessionCache(sessionFactory);			//ehcache changes
		//fetchAndUpdate(sessionFactory);
		//fetchAndUpdateAcrossSessions(sessionFactory);
		//pessimisticLocking(sessionFactory);
		//optimisticLocking(sessionFactory);
		
		//createCustomers(sessionFactory);
		//queryCustomers(sessionFactory);
		//addCustomerEmails(sessionFactory);
		
		//addCustomerAccounts(sessionFactory);
		
		//createTransactions(sessionFactory);
		
		//queryDemo(sessionFactory);
		//criteriaDemo(sessionFactory);
		sessionFactory.close();
		
	}
	
	private static void criteriaDemo(SessionFactory sessionFactory) 
	{
		Session session = sessionFactory.openSession();
		Criteria criteria = session.createCriteria(Account.class);
		criteria.add(Restrictions.lt("balance", 12000.00));
		
		List<Account> accounts = criteria.list();
//		query.setMaxResults(3);
//		query.setFirstResult(0);
		
		for(Account account:accounts)
		{
			System.out.println("Id: "+account.getAccountId());
			System.out.println("Balance: "+account.getBalance());
		}
		session.close();
	}

	private static void queryDemo(SessionFactory sessionFactory) 
	{
		Session session = sessionFactory.openSession();
//		org.hibernate.query.Query<Account> query = session.createQuery("from Account as a where a.balance> :bal");
		org.hibernate.query.Query<Account> query = session.getNamedQuery("fetchAccountByBalance");
		query.setDouble("bal", 23000);
		List<Account> accounts = query.list();
//		query.setMaxResults(3);
//		query.setFirstResult(0);
		
		for(Account account:accounts)
		{
			System.out.println("Id: "+account.getAccountId());
			System.out.println("Balance: "+account.getBalance());
		}
		session.close();
	}

	private static void createTransactions(SessionFactory sessionFactory) 
	{
		Session session = sessionFactory.openSession();
		Transaction tx = session.beginTransaction();
		
		Account acc1 = session.get(Account.class, 1);
		Account acc2 = session.get(Account.class, 2);
		
		BankTransaction bt1 = new BankTransaction(20000);
		BankTransaction bt2 = new BankTransaction(35000);
		
		acc1.getTransactions().add(bt1);
		acc1.getTransactions().add(bt2);
		
		acc2.getTransactions().add(bt2);
		
		tx.commit();
		session.close();
	}

	private static void addCustomerAccounts(SessionFactory sessionFactory) 
	{
		Session session = sessionFactory.openSession();
		Transaction tx = session.beginTransaction();
		
		Customer c1 = session.get(Customer.class, 1);
		Customer c2 = session.get(Customer.class, 2);
		
//		Account acc1 = new Account(5000);
//		Account acc2 = new Account(12000);
		
		SavingsAccount acc1 = new SavingsAccount(8000, 1000);
		SavingsAccount acc2 = new SavingsAccount(24000, 5000);
		
//		acc1.setCustomer(c2);				//no effect on db
//		acc2.setCustomer(c2);
		
		c1.getAccounts().add(acc1);			//Only this will work because customer is the master
		c1.getAccounts().add(acc2);
		
		tx.commit();
		session.close();
		
	}

	private static void addCustomerEmails(SessionFactory sessionFactory) 
	{
		Session session = sessionFactory.openSession();
		Transaction tx = session.beginTransaction();
		
		Customer customer = session.get(Customer.class, 1);
		Set<String> emails = customer.getEmails();
		emails.add("service@google.com");
		emails.add("support@google.com");
		emails.add("service@google.com");			//exixting collection
		
		
		/*Set<String> emails = new HashSet<>();
		emails.add("ceo@google.com");
		emails.add("serv@google.com");
		emails.add("feedback@google.com");			//new collection, replaces the existing one
		*/
		tx.commit();
		session.close();
	}

	private static void queryCustomers(SessionFactory sessionFactory) 
	{
		Session session = sessionFactory.openSession();
		Query query = session.createQuery("from Customer as c left join fetch c.accounts"); //HQL
		List<Customer> customers = query.list();
		
		int i=0;
		for(Customer customer:customers)	
		{
			i++;
			System.out.println("Id: "+customer.getCustomerId());
			System.out.println("Name: "+customer.getName());
			System.out.println("City: "+customer.getAddress().getCity());

			Set<Account> accounts = customer.getAccounts();
			for(Account account:accounts)
			{
				System.out.println("  Account Id: "+account.getAccountId());
				System.out.println("  Balance: "+account.getBalance());
			}
			
			System.out.println("---------------------------");
			
		}
		System.out.println(i);
		
	}

	private static void createCustomers(SessionFactory sessionFactory) 
	{
		Session session = sessionFactory.openSession();
		
		Customer c1 = new Customer("Raj",new Date(),new Address("Mumbai", "Maharashtra", "400064"));
		Customer c2 = new Customer("Karan",new Date(),new Address("Pune", "Maharashtra", "400087"));
		
		session.save(c1);
		session.save(c2);
		
		Transaction tx = session.beginTransaction();
		tx.commit();
		
		session.close();
	}

	private static void optimisticLocking(SessionFactory sessionFactory) 
	{
		Session session = sessionFactory.openSession();
		Transaction tx = session.beginTransaction();
		
		Message message = session.get(Message.class, 1);
		System.out.println("Text : "+message.getText());
		message.setText(message.getText()+" --upd");
		
		Scanner scanner = new Scanner(System.in);
		System.out.println("Press <ENTER> to cntinue....");
		scanner.nextLine();
		
		tx.commit();
		session.close();
	}

	private static void pessimisticLocking(SessionFactory sessionFactory) 
	{
		Session session = sessionFactory.openSession();
		Transaction tx = session.beginTransaction();
		
		Message message = session.get(Message.class, 1,LockOptions.UPGRADE);
		message.setText(message.getText()+" --upd");
		
		Scanner scanner = new Scanner(System.in);
		System.out.println("Press <ENTER> to cntinue....");
		scanner.nextLine();
		
		tx.commit();
		session.close();
	}

	private static void fetchAndUpdateAcrossSessions(SessionFactory sessionFactory) 
	{
		Session s1 = sessionFactory.openSession();
		Message m1 = s1.get(Message.class, 1);
		s1.close();
		
		m1.setText(m1.getText()+"-1");
		
		Session s2 = sessionFactory.openSession();
		Message m2 = s2.get(Message.class, 1);
		m2.setText(m2.getText()+"-2");
		
		Transaction tx = s2.beginTransaction();
		s2.merge(m1);
		tx.commit();
		
		s2.close();
	}

	private static void fetchAndUpdate(SessionFactory sessionFactory) 
	{
		Session session = sessionFactory.openSession();
		Transaction tx = session.beginTransaction();
		
		Message message = session.get(Message.class, 1);
		message.setText(message.getText()+" --upd");
		
		tx.commit();
		session.close();
	}

	private static void sessionCache(SessionFactory sessionFactory) 
	{
		System.out.println("------First Session------");
		Session s1 = sessionFactory.openSession();
		Message m1 = s1.get(Message.class, 1);
		System.out.println(m1);
		//s1.evict(m1);
		
		System.out.println("------First Session 2nd GET------");
		Message m2 = s1.get(Message.class, 1);
		s1.close();
		
		System.out.println("------Second Session------");
		Session s2 = sessionFactory.openSession();
		Message m3 = s2.get(Message.class, 1);
		s2.close();
		
	}

	private static void fetchMessages(SessionFactory sessionFactory) 
	{
		Session session = sessionFactory.openSession();
		//Message m = session.get(Message.class, 1);
		Message m = session.load(Message.class, 1);
		
		System.out.println("Type of m: "+m.getClass().getName());
		System.out.println("Id: "+m.getId());
		System.out.println("Text: "+m.getText());
		System.out.println("Desc: "+m.getDescription());
		
		if(m.getNextMessage()!=null)
		{
			System.out.println("Type of next message: "+m.getNextMessage().getClass().getName());
			System.out.println("Next message text : "+m.getNextMessage().getText());
		}
		session.close();
		
	}

	private static void createMessages(SessionFactory sessionFactory) {
		Session session = sessionFactory.openSession();
		
		//Save entities
		Message m1 = new Message(1,"Hello World","The First Message");
		Message m2 = new Message(2,"Hibernate Demos","The Next Message");
		m1.setNextMessage(m2);
		
		//Begin Transaction
		Transaction tx = session.beginTransaction();
		session.save(m1);
		
		//Commit tx
		tx.commit();
		
		session.close();
	}
}
