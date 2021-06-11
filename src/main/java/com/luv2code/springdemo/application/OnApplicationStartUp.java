package com.luv2code.springdemo.application;

import java.util.HashSet;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import com.luv2code.springdemo.entity.Role;
import com.luv2code.springdemo.entity.User;
import com.luv2code.springdemo.service.RoleService;
import com.luv2code.springdemo.service.UserService;

import lombok.extern.slf4j.Slf4j;

@Order(500)
@Component
@Slf4j
public class OnApplicationStartUp {

	public static final String SEED_USER = "susan";
	public static final String SEED_PASSWORD = "fun123";

	@Autowired
	private UserService userService;

	@Autowired
	private RoleService roleService;


	@EventListener
	@Order(2)
	// Uncomment to use transactions when running mongo replica set
	// @Transactional
	public void onApplicationEvent(ContextRefreshedEvent event) {

		log.info("onApplicationEvent "
				+ event.getApplicationContext().getApplicationName()
				+ " refresh event received");

		// Write your business logic here.
		if (userService.findUserByUserName(SEED_USER) == null) {
			preloadData();
		}
		log.info("onApplicationEvent "
				+ event.getApplicationContext().getApplicationName()
				+ " event processed");
	}

	private void preloadData() {
		log.info("preloadData ----> loading seed data");
		Role employeeRole = new Role();
		employeeRole.setRole("ROLE_EMPLOYEE");
		Role adminRole = new Role();
		adminRole.setRole("ROLE_ADMIN");
		Role managerRole = new Role();
		managerRole.setRole("ROLE_MANAGER");

		roleService.saveRole(employeeRole);
		roleService.saveRole(adminRole);
		roleService.saveRole(managerRole);

		User user = new User(SEED_USER, SEED_PASSWORD, Boolean.TRUE,
				new HashSet<>(Set.of(adminRole)));
		userService.saveUser(user);
		user = new User("mary", SEED_PASSWORD, Boolean.TRUE,
				new HashSet<>(Set.of(managerRole)));
		userService.saveUser(user);
		user = new User("john", SEED_PASSWORD, Boolean.TRUE);
		userService.saveUser(user);
	}
}
