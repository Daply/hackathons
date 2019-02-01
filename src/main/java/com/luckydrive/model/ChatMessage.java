package com.luckydrive.model;

import java.time.LocalTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;

@Entity(name = "chat_messages")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
public class ChatMessage {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@OneToOne
	@JoinColumn(name = "sender_id")
	private User sender;

	@Column(nullable = false)
	private String content;

	@Column(name = "time_sent", nullable = false)
	private LocalTime timeSent;

	protected ChatMessage() {
	}

	protected ChatMessage(User sender, String content, LocalTime timeSent) {
		this.sender = sender;
		this.content = content;
		this.timeSent = timeSent;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public LocalTime getTimeSent() {
		return timeSent;
	}

	public void setTimeSent(LocalTime timeSent) {
		this.timeSent = timeSent;
	}

	public User getSender() {
		return sender;
	}

	public void setSender(User sender) {
		this.sender = sender;
	}

}
