package org.springframework.data.jdbc.repository.query;

import java.io.Serializable;

import org.springframework.data.jdbc.repository.support.JdbcBeanPropertyMapper;
import org.springframework.data.jdbc.repository.support.JdbcEntityInformation;
import org.springframework.jdbc.core.JdbcOperations;

abstract class JdbcQueryExecution {
	
	protected abstract Object execute(AbstractJdbcQuery query, Object[] values, Class<?> domainClass);

	static class CollectionExecution extends JdbcQueryExecution {

		@Override
		public Object execute(AbstractJdbcQuery query, Object[] values, Class<?> domainClass) {
			
			JdbcOperations jdbcOperations = query.jdbcTemplate;
			JdbcEntityInformation<?, Serializable> information = query.jdbcMapping.getEntityInformation(domainClass);
			String sql = query.createQuery().getSql();
			return jdbcOperations.query(sql, values, JdbcBeanPropertyMapper.newInstance(information));
		}
	}
	
	static class SingleEntityExecution extends JdbcQueryExecution {

		@Override
		public Object execute(AbstractJdbcQuery query, Object[] values, Class<?> domainClass) {
			JdbcOperations jdbcOperations = query.jdbcTemplate;
			JdbcEntityInformation<?, Serializable> information = query.jdbcMapping.getEntityInformation(domainClass);
			String sql = query.createQuery().getSql();
			
			return jdbcOperations.queryForObject(sql, values, 
					JdbcBeanPropertyMapper.newInstance(information));
		}
	}
}
