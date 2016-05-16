package org.springframework.data.jdbc.repository.support;

import java.io.Serializable;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jdbc.repository.JdbcRepository;
import org.springframework.data.mapping.context.MappingContext;
import org.springframework.jdbc.core.JdbcOperations;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.util.Assert;

/**
 * 
 * {@link JdbcTemplate}, RowMapper, RowUnMapper, JdbcEntityInformation, SqlDialect 등을 만들어야 한다.
 * 
 * @author zhwan
 *
 * @param <T>
 * @param <ID>
 */
public class SimpleJdbcRepository<T, ID extends Serializable> implements JdbcRepository<T, ID> {
	
	final JdbcEntityInformation<T, ?> entityInformation;
	final JdbcOperations jdbcOperations;
	
	public SimpleJdbcRepository(JdbcEntityInformation<T, ?> entityInformation, JdbcOperations jdbcOperations) {

		Assert.notNull(entityInformation);
		Assert.notNull(jdbcOperations);

		this.entityInformation = entityInformation;
		this.jdbcOperations = jdbcOperations;
	}
	
	@Override
	public Iterable<T> findAll(Sort sort) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Page<T> findAll(Pageable pageable) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public <S extends T> S save(S entity) {
		
		if (entityInformation.isNew(entity)) {
			System.err.println("isNew > " + entity);
		}
		else {
			System.err.println("isNotNew > " + entity);
		}
		
		return entity;
	}

	@Override
	public <S extends T> Iterable<S> save(Iterable<S> entities) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public T findOne(ID id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean exists(ID id) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public Iterable<T> findAll() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Iterable<T> findAll(Iterable<ID> ids) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public long count() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void delete(ID id) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void delete(T entity) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void delete(Iterable<? extends T> entities) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void deleteAll() {
		// TODO Auto-generated method stub
		
	}

}
