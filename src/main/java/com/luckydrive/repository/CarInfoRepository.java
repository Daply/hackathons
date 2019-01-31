package com.luckydrive.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import com.luckydrive.model.CarInfo;
import com.luckydrive.model.Trip;

public interface CarInfoRepository extends CrudRepository<CarInfo, Long> {
    /**
     * Get drivers car info
     * @param passengerId
     * @return
     */
    @Query(value = "SELECT * FROM car_info c WHERE c.user_id = user_id ",
    nativeQuery = true)
    CarInfo findDriversCarInfo(@Param("user_id") Long userId);
}
