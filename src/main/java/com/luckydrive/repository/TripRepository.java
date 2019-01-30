package com.luckydrive.repository;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import javax.swing.text.html.HTML.Tag;

import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.luckydrive.model.Point;
import com.luckydrive.model.Trip;
import com.luckydrive.model.User;



//SELECT * 
//FROM mixedvalues 
//WHERE value REGEXP '^[0-9]+$';


//for help:  https://www.baeldung.com/spring-data-jpa-query
public interface TripRepository extends CrudRepository<Trip, Long> {
    
    @Query(value = "SELECT * FROM (SELECT * FROM trip t " +
            "INNER JOIN point p on t.start_point_id=p.point_id) tp " +
            "WHERE tp.lat >= :lat AND tp.lat <= :lat + :radius AND " +
            "tp.lng >= :lng AND tp.lng <= :lng + :radius", 
    nativeQuery = true)
    List<Trip> findTripByClosestStartPoints(@Param("lat") double lat, 
    		@Param("lng") double lng, @Param("radius") double radius);
    
    @Query(value = "SELECT * FROM (SELECT * FROM trip t " +
            "INNER JOIN point p on t.end_point_id=p.point_id) tp " +
            "WHERE tp.lat >= :lat AND tp.lat <= :lat + :radius AND " +
            "tp.lng >= :lng AND tp.lng <= :lng + :radius", 
    nativeQuery = true)
    List<Trip> findTripByClosestEndPoints(@Param("lat") double lat, 
            @Param("lng") double lng, @Param("radius") double radius);
    
    @Query(value = "SELECT * FROM trip t WHERE t.driver_id=driver_id", 
    nativeQuery = true)
    List<Trip> getDriversTrips(@Param("driver_id") Long driverId);
    
    @Query(value = "SELECT * FROM trip t WHERE t.driver_id=driver_id", 
    nativeQuery = true)
    List<Trip> getPassengersTrips(@Param("driver_id") Long driverId);
 
    @Query(value = "SELECT * FROM trip trp WHERE " + 
            "trp.trip_id IN " + 
            "(SELECT trip_id FROM " + 
            " (SELECT * FROM tag t " + 
            "  WHERE t.tag_name IN " + 
            "  (:tags)) coincide_tag " + 
            " INNER JOIN trip_tag_connect ttc " + 
            " ON coincide_tag.tag_id=ttc.tag_id)", 
    nativeQuery = true)
    List<Trip> findTripByTags(@Param("tags") Set<String> tags);
}
