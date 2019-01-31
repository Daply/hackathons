package com.luckydrive.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import com.luckydrive.model.Notification;
import com.luckydrive.model.User;

public interface NotificationRepository extends CrudRepository<Notification, Long> {
    
    @Query(value = "SELECT * FROM notification n WHERE n.user_id=user_id", 
    nativeQuery = true)
    Notification findUsersNotification(@Param("user_id") Long userId);
    
    @Query(value = "SELECT * FROM notification n WHERE n.trip_id = trip_id",
    nativeQuery = true)
    List<Notification> findAllUsersNotificationsInOneTrip(@Param("trip_id") Long tripId);
    
    @Query(value = "DELETE FROM notification n WHERE n.trip_id = trip_id", 
    nativeQuery = true)
    void deleteAllUsersNotificationsInOneTrip(@Param("trip_id") Long tripId);
}
