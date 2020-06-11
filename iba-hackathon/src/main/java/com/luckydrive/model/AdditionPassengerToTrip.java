package com.luckydrive.model;

public class AdditionPassengerToTrip {
    private Long userId;
    private Long tripId;
    
    public AdditionPassengerToTrip(){
        
    }
    
    public AdditionPassengerToTrip(Long userId, Long tripId){
        this.userId = userId;
        this.tripId = tripId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getTripId() {
        return tripId;
    }

    public void setTripId(Long tripId) {
        this.tripId = tripId;
    }
    
    
}
