package org.springframework.data.jdbc.repository.query;

import org.springframework.data.jdbc.mapping.JdbcMappingContext;
import org.springframework.data.jdbc.repository.support.JdbcBeanPropertyMapperFactory;
import org.springframework.jdbc.core.JdbcOperations;

abstract class JdbcQueryExecution {
	
	protected abstract Object execute(AbstractJdbcQuery query, Object[] values, Class<?> domainClass);

	static class CollectionExecution extends JdbcQueryExecution {

		@Override
		public Object execute(AbstractJdbcQuery query, Object[] values, Class<?> domainClass) {
			
			JdbcOperations jdbcOperations = query.jdbcTemplate;
			JdbcMappingContext jdbcMapping = query.jdbcMapping;
			String sql = query.createQuery().getSql();
			
			return jdbcOperations.query(sql, values, 
					JdbcBeanPropertyMapperFactory.jdbcBeanPropertyMapper(domainClass, jdbcMapping));
		}
	}
	
	static class SingleEntityExecution extends JdbcQueryExecution {

		@Override
		public Object execute(AbstractJdbcQuery query, Object[] values, Class<?> domainClass) {
			JdbcOperations jdbcOperations = query.jdbcTemplate;
			JdbcMappingContext jdbcMapping = query.jdbcMapping;
			String sql = query.createQuery().getSql();
			
			return jdbcOperations.queryForObject(sql, values, 
					JdbcBeanPropertyMapperFactory.jdbcBeanPropertyMapper(domainClass, jdbcMapping));
		}
	}
}
