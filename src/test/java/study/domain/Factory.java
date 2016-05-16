package study.domain;

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
	
	String description;
	
	String factoryType;
	
	public Factory() {}

	public Factory(String name) {
		super();
		this.name = name;
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
