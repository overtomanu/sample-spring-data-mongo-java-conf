package com.luv2code.springdemo.audit;

import java.time.LocalDateTime;

import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;

public abstract class Auditable<U> {

	@CreatedBy
	protected U createdBy;

	@CreatedDate
	private LocalDateTime creationDate;

	@LastModifiedBy
	protected U lastModifiedBy;

	@LastModifiedDate
	private LocalDateTime lastUpdateDate;

}