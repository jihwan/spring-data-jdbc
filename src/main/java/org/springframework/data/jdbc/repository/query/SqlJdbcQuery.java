package org.springframework.data.jdbc.repository.query;

import org.springframework.data.jdbc.mapping.JdbcMappingContext;
import org.springframework.jdbc.core.JdbcOperations;

public class SqlJdbcQuery extends AbstractJdbcQuery {

	public SqlJdbcQuery(JdbcQueryMethod method, JdbcOperations jdbcOperations, JdbcMappingContext jdbcMapping) {
		super(method, jdbcOperations, jdbcMapping);
	}

	@Override
	protected Query createQuery() {
		
		String sql = method.getAnnotatedQuery();
		
		return new Query(sql);
	}

}
