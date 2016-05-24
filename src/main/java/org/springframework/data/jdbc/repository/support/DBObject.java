package org.springframework.data.jdbc.repository.support;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

import org.springframework.util.Assert;

class DBObject implements Serializable {

	private static final long serialVersionUID = -4372854073573972995L;
	
	private final String FIELD_MUST_NOT_BE_NULL = "The given field must not be null!";
	
	private final Map<Field, Object> entityMap;

	DBObject(int columnCount) {
		entityMap = new HashMap<Field, Object>(columnCount);
	}
	
	DBObject add(Field field, Object value) {
		Assert.notNull(field, FIELD_MUST_NOT_BE_NULL);
		this.entityMap.put(field, value);
		
		return this;
	}
	
	Object get(Field field) {
		Assert.notNull(field, FIELD_MUST_NOT_BE_NULL);
		return this.entityMap.get(field);
	}
}
