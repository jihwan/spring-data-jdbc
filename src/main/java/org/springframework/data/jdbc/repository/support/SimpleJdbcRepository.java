package org.springframework.data.jdbc.repository.support;

import java.io.Serializable;
import java.util.Map;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jdbc.repository.JdbcRepository;
import org.springframework.data.jdbc.repository.query.JdbcSqlDialect;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.util.Assert;

/**
 * 
 * @author Jihwan Hwang
 *
 * @param <T>
 * @param <ID>
 */
public class SimpleJdbcRepository<T, ID extends Serializable> implements JdbcRepository<T, ID> {
	
	final JdbcEntityInformation<T, ?> entityInformation;
	final JdbcTemplate jdbcTemplate;
	final JdbcSqlDialect jdbcSqlDialect;
	
	final BeanPropertyMapper<T> beanPropertyMapper;
	
	public SimpleJdbcRepository(JdbcEntityInformation<T, Serializable> entityInformation, JdbcTemplate jdbcTemplate, JdbcSqlDialect jdbcSqlDialect) {

		Assert.notNull(entityInformation);
		Assert.notNull(jdbcTemplate);
		Assert.notNull(jdbcSqlDialect);
		
		this.entityInformation = entityInformation;
		this.jdbcTemplate = jdbcTemplate;
		this.jdbcSqlDialect = jdbcSqlDialect;
		
		this.beanPropertyMapper = JdbcBeanPropertyMapper.newInstance(entityInformation);
	}
	
	@Override
	public Iterable<T> findAll() {
		
		String sql = jdbcSqlDialect.findAll(entityInformation);
		return jdbcTemplate.query(sql, this.beanPropertyMapper);
	}
	
	@Override
	public Iterable<T> findAll(Sort sort) {
		return null;
	}

	@Override
	public Page<T> findAll(Pageable pageable) {
		return null;
	}
	
	@Override
	public Iterable<T> findAll(Iterable<ID> ids) {
		return null;
	}
	
	@Override
	public T findOne(ID id) {
		return null;
	}
	
	@Override
	public boolean exists(ID id) {
		return false;
	}

	@Override
	public long count() {
		return 0;
	}

	@Override
	public <S extends T> S save(S entity) {
		
		if (entityInformation.isNew(entity)) {
			System.err.println("isNew > " + entity);
			return insert(entity);
		}
		else {
			System.err.println("isNotNew > " + entity);
			return entity;
		}
	}

	public <S extends T> S insert(S entity) {
		
		Map<String, Object> map = this.beanPropertyMapper.toMap(entity);
		System.err.println("Object2Map >>> " + map);
		
		return entity;
	}

	@Override
	public <S extends T> Iterable<S> save(Iterable<S> entities) {
		
		for (S s : entities) {
			save(s);
		}
		
		return entities;
	}

	@Override
	public void delete(ID id) {
	}

	@Override
	public void delete(T entity) {
	}

	@Override
	public void delete(Iterable<? extends T> entities) {
	}

	@Override
	public void deleteAll() {
	}
}
