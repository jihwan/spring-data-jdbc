package org.springframework.data.jdbc.domain;

import java.io.Serializable;

import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Persistent;
import org.springframework.data.annotation.Transient;
import org.springframework.util.ClassUtils;

@Persistent
public class AbstractJdbcPersistable<T, ID extends Serializable> implements JdbcPersistable<T, ID> {

	private static final long serialVersionUID = 305416690924663579L;

	transient boolean persisted = false;
	
	@Id ID id;
	
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

	@Override
	public void persist(boolean persisted) {
		this.persisted = persisted;
	}
	
	@Override
	public String toString() {
		return String.format("Entity of type %s with id: %s", this.getClass().getName(), getId());
	}

	@Override
	public boolean equals(Object obj) {

		if (null == obj) {
			return false;
		}

		if (this == obj) {
			return true;
		}

		if (!getClass().equals(ClassUtils.getUserClass(obj))) {
			return false;
		}

		AbstractJdbcPersistable<?, ?> that = (AbstractJdbcPersistable<?, ?>) obj;

		return null == this.getId() ? false : this.getId().equals(that.getId());
	}

	@Override
	public int hashCode() {

		int hashCode = 17;

		hashCode += null == getId() ? 0 : getId().hashCode() * 31;

		return hashCode;
	}
}
