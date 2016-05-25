package org.springframework.data.jdbc.domain;

import java.io.Serializable;

import org.springframework.data.domain.Persistable;

/**
 * 
 * @author Jihwan Hwang
 *
 * @param <T>
 * @param <ID>
 */
public interface JdbcPersistable<T, ID extends Serializable> extends Persistable<ID> {
	
	void persist(boolean persisted);
}
