package org.springframework.data.jdbc.domain.sample;

import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.Id;

import org.springframework.data.jdbc.domain.AbstractJdbcPersistable;

import lombok.Data;

@Entity @Data
public class Machine extends AbstractJdbcPersistable<Machine, String> {
	
	private static final long serialVersionUID = 1L;
	
	@Id
	String name;
	
	@Embedded
	Address address;

	public Machine(String name) {
		super();
		this.name = name;
	}

	@Override
	public String getId() {
		return name;
	}
}
