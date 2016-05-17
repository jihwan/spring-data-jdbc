package org.springframework.data.jdbc.repository.query;

import org.springframework.jdbc.core.JdbcOperations;

public class SqlJdbcQuery extends AbstractJdbcQuery {

	public SqlJdbcQuery(JdbcQueryMethod method, JdbcOperations jdbcOperations) {
		super(method, jdbcOperations);
		// TODO Auto-generated constructor stub
	}

	@Override
	protected Query createQuery() {
		
		String sql = method.getAnnotatedQuery();
		
		return new Query(sql);
	}

}
