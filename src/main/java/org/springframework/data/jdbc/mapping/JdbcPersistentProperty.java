package org.springframework.data.jdbc.mapping;

import org.springframework.data.mapping.PersistentProperty;

/**
 * 
 * @author Jihwan Hwang
 *
 */
public interface JdbcPersistentProperty extends PersistentProperty<JdbcPersistentProperty> {
	
	boolean isColumn();
	
//	boolean isOwnerEntity();

	void setOwnerFieldName(String ownerFieldName);
	String getOwnerFieldName();
}
