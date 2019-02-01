package com.luckydrive.dto;

import com.luckydrive.model.Notification;

public class JoinTripResponse {

	private String chatChannelUuid;
	private Notification notification;

	public JoinTripResponse(String chatChannelUuid, Notification notification) {
		this.chatChannelUuid = chatChannelUuid;
		this.notification = notification;
	}

	public String getChatChannelUuid() {
		return chatChannelUuid;
	}

	public void setChatChannelUuid(String chatChannelUuid) {
		this.chatChannelUuid = chatChannelUuid;
	}

	public Notification getNotification() {
		return notification;
	}

	public void setNotification(Notification notification) {
		this.notification = notification;
	}

}
