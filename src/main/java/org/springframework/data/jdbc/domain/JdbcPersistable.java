package org.springframework.data.jdbc.domain;

import java.io.Serializable;

import org.springframework.data.domain.Persistable;

public interface JdbcPersistable<T, ID extends Serializable> extends Persistable<ID> {
	
	T persist(boolean persisted);
}
