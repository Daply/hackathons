package com.luckydrive.dto;

public class PrivateChatMessageDTO extends ChatMessageDTO {

	private Long recipientId;

	public Long getRecipientId() {
		return recipientId;
	}

	public void setRecipientId(Long recipientId) {
		this.recipientId = recipientId;
	}

}
