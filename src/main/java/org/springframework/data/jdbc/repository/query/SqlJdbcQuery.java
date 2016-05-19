package org.springframework.data.jdbc.repository.query;

import org.springframework.data.jdbc.mapping.JdbcMappingContext;
import org.springframework.jdbc.core.JdbcTemplate;

public class SqlJdbcQuery extends AbstractJdbcQuery {

	public SqlJdbcQuery(JdbcQueryMethod method, JdbcTemplate jdbcTemplate, JdbcMappingContext jdbcMapping) {
		super(method, jdbcTemplate, jdbcMapping);
	}

	@Override
	protected Query createQuery() {
		
		String sql = method.getAnnotatedQuery();
		
		return new Query(sql);
	}

}
