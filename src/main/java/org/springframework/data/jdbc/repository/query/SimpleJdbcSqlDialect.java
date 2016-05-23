package org.springframework.data.jdbc.repository.query;

import java.util.Iterator;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jdbc.repository.support.JdbcEntityInformation;

public class SimpleJdbcSqlDialect implements JdbcSqlDialect {

	@Override
	public String findAll(JdbcEntityInformation<?, ?> information) {
		return "SELECT * FROM " + information.getEntityName();
	}

	@Override
	public String findAll(JdbcEntityInformation<?, ?> information, Sort sort) {
		return findAll(information) + sort(sort);
	}
	
	protected String sort(Sort sort) {
		if (sort == null) {
			return "";
		}
		StringBuilder orderByClause = new StringBuilder();
		orderByClause.append(" ORDER BY ");
		for(Iterator<Sort.Order> iterator = sort.iterator(); iterator.hasNext();) {
			final Sort.Order order = iterator.next();
			orderByClause.
					append(order.getProperty()).
					append(" ").
					append(order.getDirection().toString());
			if (iterator.hasNext()) {
				orderByClause.append(", ");
			}
		}
		return orderByClause.toString();
	}

	@Override
	public String findAll(JdbcEntityInformation<?, ?> information, Pageable pageable) {
		return findAll(information, pageable.getSort()) + pageable(pageable);
	}
	
	protected String pageable(Pageable page) {
		final int offset = page.getPageNumber() * page.getPageSize();
		return " LIMIT " + offset + ", " + page.getPageSize();
	}

	@Override
	public String findAllByIds() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String findById() {
		// TODO Auto-generated method stub
		return null;
	}

}
