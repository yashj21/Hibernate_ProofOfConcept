package com.entity;

import javax.persistence.*;

import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.OptimisticLockType;
import org.hibernate.annotations.OptimisticLocking;

/*
 * Message 
 * 	id				int primary key
 * 	text			varchar
 * 	description		varchar
 * 	nextMessageId	int FK
 */

@Entity  //Mandatory
@Table(name="Messages")  //Optional
@OptimisticLocking(type=OptimisticLockType.VERSION)
@DynamicUpdate
@Cacheable(true)
public class Message 
{
	@Id		//Mandatory
	@Column(name="messageId")		//Optional
	private int id;
	
	@Basic		//Optional
	@Column
	private String text;
	
	@Basic @Column
	private String description;
	
	@Version @Column
	private int version;
	
	public int getVersion() {
		return version;
	}

	public void setVersion(int version) {
		this.version = version;
	}

	@ManyToOne(targetEntity=Message.class, cascade=CascadeType.ALL, fetch=FetchType.LAZY)
	@JoinColumn(name="nextMessageId")
	private Message nextMessage;
	
	public Message() 
	{
		
	}

	public Message(int id, String text, String description) 
	{
		super();
		this.id = id;
		this.text = text;
		this.description = description;
	}

	public int getId() 
	{
		return id;
	}

	public void setId(int id) 
	{
		this.id = id;
	}

	public String getText() 
	{
		return text;
	}

	public void setText(String text) 
	{
		this.text = text;
	}

	public String getDescription() 
	{
		return description;
	}

	public void setDescription(String description) 
	{
		this.description = description;
	}

	public Message getNextMessage() 
	{
		return nextMessage;
	}

	public void setNextMessage(Message nextMessage) 
	{
		this.nextMessage = nextMessage;
	}
	
	
	
}
