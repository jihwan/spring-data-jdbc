package org.springframework.data.jdbc.repository.support;

import java.io.Serializable;
import java.util.Map;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jdbc.domain.JdbcPersistable;
import org.springframework.data.jdbc.repository.JdbcRepository;
import org.springframework.data.jdbc.repository.sql.SqlGenerator;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.util.Assert;

/**
 * 
 * @author Jihwan Hwang
 *
 * @param <T>
 * @param <ID>
 */
public class SimpleJdbcRepository<T extends JdbcPersistable<T, Serializable>, ID extends Serializable> implements JdbcRepository<T, ID> {
	
	final JdbcEntityInformation<T, ?> information;
	final JdbcTemplate jdbcTemplate;
	final SqlGenerator sqlGenerator;
	
	final BeanPropertyMapper<T> beanPropertyMapper;
	
	public SimpleJdbcRepository(JdbcEntityInformation<T, Serializable> entityInformation, JdbcTemplate jdbcTemplate, SqlGenerator sqlGenerator) {
		Assert.notNull(entityInformation);
		Assert.notNull(jdbcTemplate);
		Assert.notNull(sqlGenerator);
		
		this.information = entityInformation;
		this.jdbcTemplate = jdbcTemplate;
		this.sqlGenerator = sqlGenerator;
		
		this.beanPropertyMapper = JdbcBeanPropertyMapper.newInstance(entityInformation);
	}
	
	@Override
	public Iterable<T> findAll() {
		String sql = sqlGenerator.selectAll(information);
		return jdbcTemplate.query(sql, this.beanPropertyMapper);
	}
	
	@Override
	public Iterable<T> findAll(Sort sort) {
		String sql = sqlGenerator.selectAll(information, sort);
		return jdbcTemplate.query(sql, this.beanPropertyMapper);
	}

	@Override
	public Page<T> findAll(Pageable pageable) {
		String sql = sqlGenerator.selectAll(information, pageable);
		return new PageImpl<T>(jdbcTemplate.query(sql, this.beanPropertyMapper), pageable, count());
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
		return jdbcTemplate.queryForObject(sqlGenerator.count(information), Long.class);
	}

	@Override
	public <S extends T> S save(S entity) {
		
		if (information.isNew(entity)) {
			return insert(entity);
		}
		else {
			return update(entity);
		}
	}

	public <S extends T> S insert(S entity) {
		
		Map<String, Object> map = this.beanPropertyMapper.toMap(entity);
		
		entity.persist(true);
		return entity;
	}
	
	public <S extends T> S update(S entity) {
		
		Map<String, Object> map = this.beanPropertyMapper.toMap(entity);
		
		entity.persist(true);
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
		
		entity.persist(false);
	}

	@Override
	public void delete(Iterable<? extends T> entities) {
		
		for (T t : entities) {
			delete(t);
		}
	}

	@Override
	public void deleteAll() {
	}
}
