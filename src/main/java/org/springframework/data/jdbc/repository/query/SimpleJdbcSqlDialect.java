package org.springframework.data.jdbc.repository.query;

import java.util.Iterator;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jdbc.repository.support.JdbcEntityInformation;

/**
 * <a href="https://github.com/nurkiewicz/spring-data-jdbc-repository">original code</a>
 * <a href="https://github.com/jirutka/spring-data-jdbc-repository">fork code</a>
 * 
 * 
 * @author Tomasz Nurkiewicz 
 * @author Jakub Jirutka  
 * @author Jihwan Hwang
 *
 */
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
		StringBuilder orderBy = new StringBuilder();
		orderBy.append(" ORDER BY ");
		for(Iterator<Sort.Order> iterator = sort.iterator(); iterator.hasNext();) {
			final Sort.Order order = iterator.next();
			orderBy.
					append(order.getProperty()).
					append(" ").
					append(order.getDirection().toString());
			if (iterator.hasNext()) {
				orderBy.append(", ");
			}
		}
		return orderBy.toString();
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
	
	@Override
	public String count(JdbcEntityInformation<?, ?> information) {
		return "SELECT COUNT(*) FROM " + information.getEntityName();
	}

}
