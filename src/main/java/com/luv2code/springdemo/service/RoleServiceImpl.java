package com.luv2code.springdemo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.luv2code.springdemo.entity.Role;
import com.luv2code.springdemo.repository.RoleRepository;

@Service
public class RoleServiceImpl implements RoleService {

	@Autowired
	RoleRepository roleRepository;

	@Override
	public Role findByRole(String role) {
		return roleRepository.findByRole(role);
	}

	@Override
	public Role saveRole(Role role) { 
		return roleRepository.save(role);
	}

}
