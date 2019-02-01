package com.luckydrive.model;

import java.time.LocalTime;

import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;

public class PrivateChatMessage extends ChatMessage {

	@OneToOne
	@JoinColumn(name = "recipient_id")
	private User recipient;

	public PrivateChatMessage(User sender, String content, User recipient, LocalTime timeSent) {
		super(sender, content, timeSent);
		this.recipient = recipient;
	}

	public User getRecipient() {
		return recipient;
	}

	public void setRecipient(User recipient) {
		this.recipient = recipient;
	}

}
