package org.springframework.data.jdbc.repository.query;

public class Query {

	String sql;
	
	public Query(String sql) {
		this.sql = sql;
	}
	
	public String getSql() {
		return sql;
	}
}
