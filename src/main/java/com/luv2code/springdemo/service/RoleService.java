package com.luv2code.springdemo.service;

import com.luv2code.springdemo.entity.Role;

public interface RoleService {

	public Role findByRole(String role);

	public Role saveRole(Role role);

}
