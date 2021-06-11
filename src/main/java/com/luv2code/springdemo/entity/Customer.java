package com.luv2code.springdemo.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Version;
import org.springframework.data.mongodb.core.mapping.Document;

import com.luv2code.springdemo.audit.Auditable;

import lombok.AllArgsConstructor;
import lombok.Builder;


@Builder
@AllArgsConstructor
@Document
public class Customer extends Auditable<String> {

	/*
	Extending org.springframework.data.jpa.domain.AbstractAuditable gives error
	"Specified key was too long" if we use string as id column with hibernate JPA on mysql db
	So creating our own mappedSuperClass for auditing and defining Id column in subclass
	*/

	@Id
	private String id;

	private String firstName;

	private String lastName;

	private String email;

	@Version
	private Long version;

	public Customer() {}


	public String getId() { return id; }

	public void setId(String id) { this.id = id; }

	public String getFirstName() { return firstName; }

	public void setFirstName(String firstName) { this.firstName = firstName; }

	public String getLastName() { return lastName; }

	public void setLastName(String lastName) { this.lastName = lastName; }

	public String getEmail() { return email; }

	public void setEmail(String email) { this.email = email; }

	public Long getVersion() { return version; }

	public void setVersion(Long version) { this.version = version; }

	@Override
	public String toString() {
		return "Customer [id=" + getId() + ", firstName=" + firstName
				+ ", lastName="
				+ lastName + ", email=" + email + ", version=" + version + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}


	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Customer other = (Customer) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

}
