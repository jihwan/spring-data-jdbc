package org.springframework.data.jdbc.repository.query;

import org.springframework.jdbc.core.BeanPropertyRowMapper;

interface JdbcQueryExecution {

	Object execute(AbstractJdbcQuery query, Object[] values, Class<?> domainClass);

	static class CollectionExecution implements JdbcQueryExecution {

		@Override
		public Object execute(AbstractJdbcQuery query, Object[] values, Class<?> domainClass) {
			
			String sql = query.createQuery().getSql();
			return query.jdbcOperations.query(sql, values, BeanPropertyRowMapper.newInstance(domainClass));
		}
	}
	
	static class SingleEntityExecution implements JdbcQueryExecution {

		@Override
		public Object execute(AbstractJdbcQuery query, Object[] values, Class<?> domainClass) {
			String sql = query.createQuery().getSql();
			return query.jdbcOperations.queryForObject(sql, values, BeanPropertyRowMapper.newInstance(domainClass));
		}
	}
}
