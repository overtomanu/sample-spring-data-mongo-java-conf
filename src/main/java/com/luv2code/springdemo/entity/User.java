package com.luv2code.springdemo.entity;

import java.util.Set;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor

@Document
public class User {
	@Id
	private String username;
	@EqualsAndHashCode.Exclude
	@ToString.Exclude
	private String password;
	private Boolean enabled;

	public User(String username, String password, Boolean enabled) {
		super();
		this.username = username;
		this.password = password;
		this.enabled = enabled;
	}

	@EqualsAndHashCode.Exclude
	@ToString.Exclude
	@DBRef
	private Set<Role> roles;
}
