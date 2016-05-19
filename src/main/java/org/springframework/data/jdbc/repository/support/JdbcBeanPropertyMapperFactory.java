package org.springframework.data.jdbc.repository.support;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import org.springframework.data.jdbc.mapping.JdbcMappingContext;

public class JdbcBeanPropertyMapperFactory {

	static final ConcurrentMap<Class<?>, JdbcBeanPropertyMapper<?>> CACHE =
			new ConcurrentHashMap<Class<?>, JdbcBeanPropertyMapper<?>>(64);
	
	public static JdbcBeanPropertyMapper<?> jdbcBeanPropertyMapper(Class<?> entityClass, JdbcMappingContext jdbcMapping) {
		
		JdbcBeanPropertyMapper<?> results = CACHE.get(entityClass);
		if (results != null) {
			return results;
		}
		
		results = JdbcBeanPropertyMapper.newInstance(entityClass, jdbcMapping);
		
		JdbcBeanPropertyMapper<?> existing = CACHE.putIfAbsent(entityClass, results);
		return (existing != null ? existing : results);
	}
}
