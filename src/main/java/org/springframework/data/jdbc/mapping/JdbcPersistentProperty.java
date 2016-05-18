package org.springframework.data.jdbc.mapping;

import org.springframework.data.mapping.PersistentProperty;

public interface JdbcPersistentProperty extends PersistentProperty<JdbcPersistentProperty> {
	
	boolean isColumn();
	
	boolean isOwnerEntity();

	void setOwnerFieldName(String name);
	String getOwnerFieldName();
}
