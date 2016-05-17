package org.springframework.data.jdbc.domain;

import java.io.Serializable;

@SuppressWarnings("serial")
public class AbstractJdbcPersistable<T, ID extends Serializable> implements JdbcPersistable<T, ID> {

	transient boolean persisted = false;
	
	ID pk;
	
	@Override
	public ID getId() {
		return pk;
	}

	@Override
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
