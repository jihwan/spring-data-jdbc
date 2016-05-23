package org.springframework.data.jdbc.repository.query;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jdbc.repository.support.JdbcEntityInformation;

public interface JdbcSqlDialect {

	String findAll(JdbcEntityInformation<?, ?> information);
	String findAll(JdbcEntityInformation<?, ?> information, Sort sort);
	String findAll(JdbcEntityInformation<?, ?> information, Pageable pageable);
	
	String findAllByIds();
	String findById();
	
}
