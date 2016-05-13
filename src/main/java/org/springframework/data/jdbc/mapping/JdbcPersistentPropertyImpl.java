package org.springframework.data.jdbc.mapping;

import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;

import org.springframework.data.mapping.Association;
import org.springframework.data.mapping.PersistentEntity;
import org.springframework.data.mapping.model.AnnotationBasedPersistentProperty;
import org.springframework.data.mapping.model.SimpleTypeHolder;

public class JdbcPersistentPropertyImpl extends AnnotationBasedPersistentProperty<JdbcPersistentProperty> implements JdbcPersistentProperty {

	public JdbcPersistentPropertyImpl(Field field, PropertyDescriptor propertyDescriptor,
			PersistentEntity<?, JdbcPersistentProperty> owner, SimpleTypeHolder simpleTypeHolder) {
		super(field, propertyDescriptor, owner, simpleTypeHolder);
	}

	@Override
	protected Association<JdbcPersistentProperty> createAssociation() {
		// TODO Auto-generated method stub
		return null;
	}


}
