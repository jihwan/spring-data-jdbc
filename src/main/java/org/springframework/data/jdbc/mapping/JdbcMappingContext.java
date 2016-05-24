package org.springframework.data.jdbc.mapping;

import java.beans.PropertyDescriptor;
import java.io.Serializable;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

import org.springframework.data.jdbc.repository.support.JdbcEntityInformation;
import org.springframework.data.mapping.context.AbstractMappingContext;
import org.springframework.data.mapping.model.SimpleTypeHolder;
import org.springframework.data.util.TypeInformation;
import org.springframework.util.Assert;

public class JdbcMappingContext extends
		AbstractMappingContext<JdbcPersistentEntityImpl<?>, JdbcPersistentProperty> {

	/**
	 * JdbcQueryExecution 에서 사용하는 용도이다.
	 * 근데 이게 모양새가 그리 좋아 보이지 않는다.
	 * 
	 * root entity 정보를 관리한다.
	 */
	private Map<Class<?>, JdbcEntityInformation<?, Serializable>> map = new HashMap<Class<?>, JdbcEntityInformation<?, Serializable>>();
	
	@Override
	protected <T> JdbcPersistentEntityImpl<?> createPersistentEntity(TypeInformation<T> typeInformation) {
		JdbcPersistentEntityImpl<T> jdbcPersistentEntityImpl = new JdbcPersistentEntityImpl<T>(typeInformation);
		return jdbcPersistentEntityImpl;
	}

	@Override
	protected JdbcPersistentProperty createPersistentProperty(Field field, PropertyDescriptor descriptor,
			JdbcPersistentEntityImpl<?> owner, SimpleTypeHolder simpleTypeHolder) {
		JdbcPersistentPropertyImpl jdbcPersistentPropertyImpl = new JdbcPersistentPropertyImpl(field, descriptor, owner, simpleTypeHolder);
		return jdbcPersistentPropertyImpl;
	}

	public void addEntityInformation(Class<?> domainClass, JdbcEntityInformation<?, Serializable> information) {
		Assert.notNull(domainClass);
		Assert.notNull(information);
		map.put(domainClass, information);
	}
	
	public JdbcEntityInformation<?, Serializable> getEntityInformation(Class<?> domainClass) {
		Assert.notNull(domainClass);
		
		if (map.containsKey(domainClass)) {
			return map.get(domainClass);
		}
		
		throw new IllegalArgumentException(domainClass + " is not support.");
	}
}
