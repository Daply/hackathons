package com.luckydrive.repository;

import org.springframework.data.repository.CrudRepository;

import com.luckydrive.model.CarInfo;

public interface CarInfoRepository extends CrudRepository<CarInfo, Long> {

}
