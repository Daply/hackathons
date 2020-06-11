package com.luckydrive.repository;

import org.springframework.data.repository.CrudRepository;

import com.luckydrive.model.Tag;

public interface TagRepository extends CrudRepository<Tag, Long> {

}
