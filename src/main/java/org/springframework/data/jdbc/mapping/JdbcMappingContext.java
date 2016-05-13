package org.springframework.data.jdbc.mapping;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;

import org.springframework.data.mapping.context.AbstractMappingContext;
import org.springframework.data.mapping.model.SimpleTypeHolder;
import org.springframework.data.util.TypeInformation;

public class JdbcMappingContext extends
		AbstractMappingContext<JdbcPersistentEntityImpl<?>, JdbcPersistentProperty> {

	@Override
	protected <T> JdbcPersistentEntityImpl<?> createPersistentEntity(TypeInformation<T> typeInformation) {
		
		JdbcPersistentEntityImpl<T> jdbcPersistentEntityImpl = new JdbcPersistentEntityImpl<T>(typeInformation);
		return jdbcPersistentEntityImpl;
	}

	@Override
	protected JdbcPersistentProperty createPersistentProperty(Field field, PropertyDescriptor descriptor,
			JdbcPersistentEntityImpl<?> owner, SimpleTypeHolder simpleTypeHolder) {
		
		JdbcPersistentPropertyImpl jpaPersistentPropertyImpl = new JdbcPersistentPropertyImpl(field, descriptor, owner, simpleTypeHolder);
		return jpaPersistentPropertyImpl;
	}


}