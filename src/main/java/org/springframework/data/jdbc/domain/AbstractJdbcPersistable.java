package org.springframework.data.jdbc.domain;

import java.io.Serializable;

import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import javax.persistence.Transient;

@SuppressWarnings("serial")
@MappedSuperclass
public class AbstractJdbcPersistable<T, ID extends Serializable> implements JdbcPersistable<T, ID> {

	transient boolean persisted = false;
	
	@Id @GeneratedValue ID id;
	
	@Override
	public ID getId() {
		return id;
	}
	
	public void setId(ID id) {
		this.id = id;
	}

	@Override
	@Transient
	public boolean isNew() {
		return !persisted;
	}

	@SuppressWarnings("unchecked")
	@Override
	public T persist(boolean persisted) {
		this.persisted = persisted;
		return (T)this;
	}

}
