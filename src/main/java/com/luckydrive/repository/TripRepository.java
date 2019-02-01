package com.luckydrive.repository;

import java.util.List;
import java.util.Set;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import com.luckydrive.model.Trip;

//SELECT * 
//FROM mixedvalues 
//WHERE value REGEXP '^[0-9]+$';

//for help:  https://www.baeldung.com/spring-data-jpa-query
public interface TripRepository extends CrudRepository<Trip, Long> {

	/**
	 * Get trips by closest start points
	 * 
	 * @param point all data
	 * @return
	 */
	@Query(value = "SELECT * FROM (SELECT * FROM trip t " + "INNER JOIN point p on t.start_point_id=p.point_id) tp "
			+ "WHERE ACOS(SIN(RADIANS(tp.lat))*SIN(RADIANS(:lat))" + "+COS(RADIANS(tp.lat))*COS(RADIANS(:lat))"
			+ "*COS(ABS(RADIANS(tp.lng)-RADIANS(:lng))))*6371*1000 < :radius", nativeQuery = true)
	List<Trip> findTripByClosestStartPoints(@Param("lat") double lat, @Param("lng") double lng,
			@Param("radius") double radius);

	/**
	 * Get trips by closest end points
	 * 
	 * @param point all data
	 * @return
	 */
	@Query(value = "SELECT * FROM (SELECT * FROM trip t " + "INNER JOIN point p on t.end_point_id=p.point_id) tp "
			+ "WHERE ACOS(SIN(RADIANS(tp.lat))*SIN(RADIANS(:lat))" + "+COS(RADIANS(tp.lat))*COS(RADIANS(:lat))"
			+ "*COS(ABS(RADIANS(tp.lng)-RADIANS(:lng))))*6371*1000 < :radius", nativeQuery = true)
	List<Trip> findTripByClosestEndPoints(@Param("lat") double lat, @Param("lng") double lng,
			@Param("radius") double radius);

	/**
	 * Get driver's active or not active trips
	 * 
	 * @param driverId
	 * @return
	 */
	@Query(value = "SELECT * FROM trip t WHERE t.driver_id=driver_id AND "
			+ "t.status = 'ACTIVE' or t.status = 'NOT_ACTIVE'", nativeQuery = true)
	List<Trip> getDriversTrips(@Param("driver_id") Long driverId);

	/**
	 * Get passenger's active or not active trips
	 * 
	 * @param passengerId
	 * @return
	 */
	@Query(value = "SELECT * FROM trip t WHERE t.trip_id IN " + "(SELECT * FROM user_trip_connect utc WHERE "
			+ "utc.user_id = :passenger_id) AND t.status='ACTIVE'", nativeQuery = true)
	List<Trip> getPassengersTrips(@Param("passenger_id") Long passengerId);

	/**
	 * Get driver's active or not active trips
	 * 
	 * @param driverId
	 * @return
	 */
	@Query(value = "SELECT * FROM trip t WHERE t.driver_id=driver_id AND "
			+ "t.status = 'COMPLETED'", nativeQuery = true)
	List<Trip> getCompletedDriversTrips(@Param("driver_id") Long driverId);

	/**
	 * Get passenger's active or not active trips
	 * 
	 * @param passengerId
	 * @return
	 */
	@Query(value = "SELECT * FROM trip t WHERE t.trip_id IN " + "(SELECT * FROM user_trip_connect utc WHERE "
			+ "utc.user_id = :passenger_id) AND (t.status='COMPLETED' OR t.status='DELETED')", nativeQuery = true)
	List<Trip> getCompletedPassengersTrips(@Param("passenger_id") Long passengerId);

	/**
	 * Get trips by tags
	 * 
	 * @param point all data
	 * @return
	 */
	@Query(value = "SELECT * FROM trip trp WHERE " + "trp.trip_id IN " + "(SELECT trip_id FROM "
			+ " (SELECT * FROM tag t " + "  WHERE t.tag_name IN " + "  (:tags)) coincide_tag "
			+ " INNER JOIN trip_tag_connect ttc " + " ON coincide_tag.tag_id=ttc.tag_id)", nativeQuery = true)
	List<Trip> findTripByTags(@Param("tags") Set<String> tags);

}
