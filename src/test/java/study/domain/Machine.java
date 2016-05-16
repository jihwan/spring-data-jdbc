package study.domain;

import javax.persistence.Entity;
import javax.persistence.Id;

import org.springframework.data.jdbc.domain.JdbcPersistable;

import lombok.Data;

@Data @Entity
public class Machine implements JdbcPersistable<Machine, String> {
	
	private static final long serialVersionUID = 1L;
	
	transient boolean persisted = false;
	@Id
	String name;

	public Machine(String name) {
		super();
		this.name = name;
	}

	@Override
	public String getId() {
		return name;
	}

	@Override
	public boolean isNew() {
		return !persisted;
	}

	@Override
	public Machine persist(boolean persisted) {
		this.persisted = persisted;
		return this;
	}
}
