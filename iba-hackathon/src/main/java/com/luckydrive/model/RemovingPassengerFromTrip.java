package com.luckydrive.model;

public class RemovingPassengerFromTrip {
    private Long driverId;
    private Long userId;
    private Long tripId;
    
    public RemovingPassengerFromTrip(){
        
    }
    
    public RemovingPassengerFromTrip(Long driverId, Long userId, Long tripId){
        this.driverId = driverId;
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

    public Long getDriverId() {
        return driverId;
    }

    public void setDriverId(Long driverId) {
        this.driverId = driverId;
    }
    
    
}
