package com.luckydrive.dto;

public class NewPassengerNotification {

	private UserShortInfo joinedPassenger;
	private String chatChannelUuid;

	public NewPassengerNotification(UserShortInfo joinedPassenger) {
		this.joinedPassenger = joinedPassenger;
	}

	public UserShortInfo getJoinedPassenger() {
		return joinedPassenger;
	}

	public void setJoinedPassenger(UserShortInfo joinedPassenger) {
		this.joinedPassenger = joinedPassenger;
	}

	public String getChatChannelUuid() {
		return chatChannelUuid;
	}

	public void setChatChannelUuid(String chatChannelUuid) {
		this.chatChannelUuid = chatChannelUuid;
	}

}
