package com.shash.file.repository;


import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.transaction.annotation.Transactional;

import com.shash.file.model.ImageModel;

@Transactional
public interface ImageRepository extends CrudRepository<ImageModel, Long>{
	
	@Query("select u from ImageModel u where u.name= ?1")
	ImageModel findByName(String name);
}
