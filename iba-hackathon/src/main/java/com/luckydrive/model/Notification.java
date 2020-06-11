package com.luckydrive.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity(name="notification")
public class Notification {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "notification_id")
    private Long notificationId;

	private String message;
	
	private Date messageDate;
	
	@Column(name = "is_read")
	private boolean isRead = false;

	@JsonIgnore
	@OneToOne
	@JoinColumn(name="user_id", nullable=false)
    private User user;
	
	@JsonIgnore
	@OneToOne
    @JoinColumn(name="trip_id", nullable=false)
    private Trip trip;

	public Notification() {
	    
	}

	
	
    public Notification(String message, Date messageDate, User user) {
        super();
        this.message = message;
        this.messageDate = messageDate;
        this.user = user;
    }



    public Long getNotificationId() {
        return notificationId;
    }

    public void setNotificationId(Long notificationId) {
        this.notificationId = notificationId;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Date getMessageDate() {
        return messageDate;
    }

    public void setMessageDate(Date messageDate) {
        this.messageDate = messageDate;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public boolean isRead() {
        return isRead;
    }

    public void setRead(boolean isRead) {
        this.isRead = isRead;
    }

    public Trip getTrip() {
        return trip;
    }

    public void setTrip(Trip trip) {
        this.trip = trip;
    }

}
