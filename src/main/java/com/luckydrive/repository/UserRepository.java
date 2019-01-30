package com.luckydrive.repository;

import java.util.List;
import java.util.Optional;

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


import com.luckydrive.model.User;

public interface UserRepository extends CrudRepository<User, Long> {
    
    @Query(value = "SELECT * FROM user", 
    nativeQuery = true)
    List<User> findAll();
    
    Optional<User> findById(Long id);    

}