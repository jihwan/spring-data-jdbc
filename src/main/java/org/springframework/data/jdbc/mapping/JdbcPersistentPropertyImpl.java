package org.springframework.data.jdbc.mapping;

import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

import org.springframework.data.mapping.Association;
import org.springframework.data.mapping.PersistentEntity;
import org.springframework.data.mapping.model.AnnotationBasedPersistentProperty;
import org.springframework.data.mapping.model.SimpleTypeHolder;

public class JdbcPersistentPropertyImpl 
	extends AnnotationBasedPersistentProperty<JdbcPersistentProperty> 
	implements JdbcPersistentProperty {
	
	final static SimpleTypeHolder HOLDER = new SimpleTypeHolder();

	public JdbcPersistentPropertyImpl(Field field, PropertyDescriptor propertyDescriptor,
			PersistentEntity<?, JdbcPersistentProperty> owner, SimpleTypeHolder simpleTypeHolder) {
		super(field, propertyDescriptor, owner, simpleTypeHolder);
	}

	@Override
	public boolean isColumn() {
		
		if (isTransient() || isIdProperty()) {
			return false;
		}

		return true;
	}

	@Override
	protected Association<JdbcPersistentProperty> createAssociation() {
		return new Association<JdbcPersistentProperty>(this, null);
	}

	@Override
	public boolean isTransient() {
		
		// transient modifier가 있을 경우
		if( getField() != null && Modifier.isTransient(getField().getModifiers()) ) {
			return true;
		}
		
		return super.isTransient();
	}
	
	@Override
	public boolean isAssociation() {
		
		if (HOLDER.isSimpleType(getType()) == false) {
			return true;
		}
		
		return super.isAssociation();
	}
}
