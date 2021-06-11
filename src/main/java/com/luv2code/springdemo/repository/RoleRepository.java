package com.luv2code.springdemo.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.luv2code.springdemo.entity.Role;

@Repository
public interface RoleRepository extends MongoRepository<Role, String> {
	Role findByRole(String role);

}