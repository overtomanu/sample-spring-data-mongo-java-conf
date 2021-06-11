package com.luv2code.springdemo.service;

import com.luv2code.springdemo.entity.User;

public interface UserService {
	public User findUserByUserName(String userName);

	public User saveUser(User user);
}
