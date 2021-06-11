package com.luv2code.springdemo.service;

import java.util.HashSet;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.luv2code.springdemo.entity.Role;
import com.luv2code.springdemo.entity.User;
import com.luv2code.springdemo.repository.RoleRepository;
import com.luv2code.springdemo.repository.UserRepository;

@Service
public class UserServiceImpl implements UserService {

	private UserRepository userRepository;
	private RoleRepository roleRepository;
	private PasswordEncoder delegatingPasswordEncoder;

	@Autowired
	public UserServiceImpl(UserRepository userRepository,
                       RoleRepository roleRepository,
                       PasswordEncoder delegatingPasswordEncoder) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.delegatingPasswordEncoder = delegatingPasswordEncoder;
    }

	@Override
	public User findUserByUserName(String userName) {
		return userRepository.findByUsername(userName);
	}

	@Override
	public User saveUser(User user) { 
		user.setPassword(delegatingPasswordEncoder.encode(user.getPassword()));
		user.setEnabled(true);
		Set<Role> userRoles;
		if (user.getRoles() != null) {
			userRoles = user.getRoles();
		} else {
			userRoles = new HashSet<>();
		}
		Role userRole = roleRepository.findByRole("ROLE_EMPLOYEE");
		userRoles.add(userRole);
		user.setRoles(userRoles);
		return userRepository.save(user);
	}

}
