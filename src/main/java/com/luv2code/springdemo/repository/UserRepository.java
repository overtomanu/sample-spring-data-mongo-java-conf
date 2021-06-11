package com.luv2code.springdemo.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.luv2code.springdemo.entity.User;

@Repository
public interface UserRepository extends MongoRepository<User, String> {
	User findByUsername(String userName);
}