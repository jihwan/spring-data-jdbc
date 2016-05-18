package org.springframework.data.jdbc.repository.query;

import org.springframework.data.jdbc.mapping.JdbcMappingContext;
import org.springframework.data.jdbc.repository.support.JdbcBeanPropertyMapper;
import org.springframework.jdbc.core.JdbcOperations;

interface JdbcQueryExecution {

	Object execute(AbstractJdbcQuery query, Object[] values, Class<?> domainClass);

	static class CollectionExecution implements JdbcQueryExecution {

		@Override
		public Object execute(AbstractJdbcQuery query, Object[] values, Class<?> domainClass) {
			
			JdbcOperations jdbcOperations = query.jdbcOperations;
			JdbcMappingContext jdbcMapping = query.jdbcMapping;
			String sql = query.createQuery().getSql();
			
			return jdbcOperations.query(sql, values, JdbcBeanPropertyMapper.newInstance(domainClass, jdbcMapping));
		}
	}
	
	static class SingleEntityExecution implements JdbcQueryExecution {

		@Override
		public Object execute(AbstractJdbcQuery query, Object[] values, Class<?> domainClass) {
			JdbcOperations jdbcOperations = query.jdbcOperations;
			JdbcMappingContext jdbcMapping = query.jdbcMapping;
			String sql = query.createQuery().getSql();
			
			return jdbcOperations.queryForObject(sql, values, JdbcBeanPropertyMapper.newInstance(domainClass, jdbcMapping));
		}
	}
}
