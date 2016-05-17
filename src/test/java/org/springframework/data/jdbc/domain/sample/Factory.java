package org.springframework.data.jdbc.domain.sample;

import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.Id;

import org.springframework.data.jdbc.domain.JdbcPersistable;

import lombok.Data;

@Entity @Data
public class Factory implements JdbcPersistable<Factory, String> {
	
	private static final long serialVersionUID = 1L;

	transient boolean persisted = false;
	
	@Id
	String name;
	
	@Column
	String description;
	
	@Column
	String factoryType;
	
//	@Transient
//	@Column
	String dummy;
	
	@Embedded
	Address address;
	
	public Factory() {}

	public Factory(String name) {
		super();
		this.name = name;
	}
	
	public static Factory instance(String name, String desc, String type) {
		Factory factory = new Factory(name);
		factory.setDescription(desc);
		factory.setFactoryType(type);
		return factory;
	}

	@Override
	public String getId() {
		return this.name;
	}

	@Override
	public boolean isNew() {
		return !this.persisted;
	}

	@Override
	public Factory persist(boolean persisted) {
		this.persisted = persisted;
		return this;
	}
}
