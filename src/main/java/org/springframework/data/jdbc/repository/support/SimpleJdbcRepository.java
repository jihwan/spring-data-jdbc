package org.springframework.data.jdbc.repository.support;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Persistable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jdbc.domain.JdbcPersistable;
import org.springframework.data.jdbc.repository.JdbcRepository;
import org.springframework.data.jdbc.repository.sql.SqlGenerator;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

/**
 * 
 * @author Tomasz Nurkiewicz 
 * @author Jakub Jirutka
 * @author Jihwan Hwang
 *
 * @param <T>
 * @param <ID>
 */
@Repository
@Transactional(readOnly = true)
public class SimpleJdbcRepository<T extends Persistable<ID>, ID extends Serializable> implements JdbcRepository<T, ID> {
	
	private static final String ID_MUST_NOT_BE_NULL = "The given id must not be null!";
	
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
	public List<T> findAll() {
		String sql = sqlGenerator.selectAll(information);
		return jdbcTemplate.query(sql, this.beanPropertyMapper);
	}
	
	@Override
	public List<T> findAll(Sort sort) {
		String sql = sqlGenerator.selectAll(information, sort);
		return jdbcTemplate.query(sql, this.beanPropertyMapper);
	}

	@Override
	public Page<T> findAll(Pageable pageable) {
		
		if(pageable == null) {
			return new PageImpl<T>(findAll());
		}
		
		String sql = sqlGenerator.selectAll(information, pageable);
		return new PageImpl<T>(jdbcTemplate.query(sql, this.beanPropertyMapper), pageable, count());
	}
	
	@Override
	public List<T> findAll(Iterable<ID> ids) {
        List<ID> idsList = toList(ids);
        if (idsList.isEmpty()) {
            return Collections.emptyList();
        }
        return jdbcTemplate.query(
        		sqlGenerator.selectByIds(information, idsList.size()), this.beanPropertyMapper, flatten(idsList));
	}
	
	@Override
	public T findOne(ID id) {
		
		Assert.notNull(id, ID_MUST_NOT_BE_NULL); 
		
		List<T> entityOrEmpty = jdbcTemplate.query(
				sqlGenerator.selectById(information), this.beanPropertyMapper, information.getCompositeIdAttributeValue(id).toArray());
		return entityOrEmpty.isEmpty() ? null : entityOrEmpty.get(0);
	}
	
	@Override
	public boolean exists(ID id) {
		
		Assert.notNull(id, ID_MUST_NOT_BE_NULL);
		
		List<Integer> object = jdbcTemplate.queryForList(
				sqlGenerator.existsById(information), information.getCompositeIdAttributeValue(id).toArray(), Integer.class);
		return !object.isEmpty();
	}

	@Override
	public long count() {
		return jdbcTemplate.queryForObject(sqlGenerator.count(information), Long.class);
	}

	@Transactional
	@Override
	public <S extends T> S save(S entity) {
		
		if (entity.isNew()) {
			return insert(entity);
		}
		else {
			return update(entity);
		}
	}

	@Transactional
	@Override
	public <S extends T> S insert(S entity) {
		
		Assert.state(entity.isNew() == true, "The given entity psersisted status must be false.");
		
		if (information.getId(entity) != null) {
			// manual assigned key
			return insertManualAssignedKey(entity);
		}
		else {
			// auto generate key
			return insertAutoGenerateKey(entity);
		}
	}
	
	protected <S extends T> S insertManualAssignedKey(S entity) {
		
		Map<String, Object> columns = this.beanPropertyMapper.toMap(entity);
		Object[] queryParams = columns.values().toArray();
		jdbcTemplate.update(sqlGenerator.insert(information, columns), queryParams);
		
		if (entity instanceof JdbcPersistable) {
			@SuppressWarnings("unchecked")
			JdbcPersistable<T, ?> cast = JdbcPersistable.class.cast(entity);
			cast.persist(true);
		}
		
		return entity;
	}
	
	protected <S extends T> S insertAutoGenerateKey(S entity) {
		
		Map<String, Object> columns = this.beanPropertyMapper.toMap(entity);
		Object[] queryParams = columns.values().toArray();
		KeyHolder key = new GeneratedKeyHolder();
		
		jdbcTemplate.update(new PreparedStatementCreator() {
			@Override
			public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
				PreparedStatement ps = 
						con.prepareStatement(sqlGenerator.insert(information, columns), 
								new String[]{information.getIdAttributeNames().get(0)});
				for (int i = 0; i < queryParams.length; ++i) {
					ps.setObject(i + 1, queryParams[i]);
				}
				return ps;
			}
		}, key);
		
		
		information.setIdAttributeValue(entity, key.getKey());
		
		return entity;
	}
	
	@Transactional
	@Override
	public <S extends T> S update(S entity) {
		
		Assert.notNull(entity.getId(), "The given entity id must not be null.");
		Assert.state(entity.isNew() == false, "The given entity psersisted status must be true.");
		
		Map<String, Object> columns = this.beanPropertyMapper.toMap(entity);
		Map<String, Object> simpleColumns = information.removeIdAttribute(columns);
		
		List<Object> queryParams = new ArrayList<Object>(simpleColumns.values());
		queryParams.addAll(information.getCompositeIdAttributeValue(entity.getId()));
		
		jdbcTemplate.update(sqlGenerator.update(information, simpleColumns), queryParams.toArray());
		
		if (entity instanceof JdbcPersistable) {
			@SuppressWarnings("unchecked")
			JdbcPersistable<T, ?> cast = JdbcPersistable.class.cast(entity);
			cast.persist(true);
		}
		return entity;
	}

	@Transactional
	@Override
	public <S extends T> List<S> save(Iterable<S> entities) {
		
		List<S> result = new ArrayList<S>();
		
		if (entities == null) {
			return result;
		}
		
		for (S entity : entities) {
			result.add(save(entity));
		}
		
		return result;
	}

	@Transactional
	@Override
	public void delete(ID id) {
		
		Assert.notNull(id, ID_MUST_NOT_BE_NULL);
		
		T entity = findOne(id);
		if (entity == null) {
			throw new EmptyResultDataAccessException(
					String.format("No %s entity with id %s exists!", information.getJavaType(), id), 1);
		}
		
		delete(entity);
	}

	@Transactional
	@Override
	public void delete(T entity) {
		
		Assert.notNull(entity, "The given entity must not be null!");
		
		jdbcTemplate.update(
				sqlGenerator.deleteById(information), information.getCompositeIdAttributeValue(entity.getId()).toArray());
		
		if (entity instanceof JdbcPersistable) {
			@SuppressWarnings("unchecked")
			JdbcPersistable<T, ?> cast = JdbcPersistable.class.cast(entity);
			cast.persist(false);
		}
	}

	@Transactional
	@Override
	public void delete(Iterable<? extends T> entities) {
		
		Assert.notNull(entities, "The given Iterable of entities not be null!");
		
		for (T t : entities) {
			delete(t);
		}
	}

	@Transactional
	@Override
	public void deleteAll() {
		
		jdbcTemplate.update(sqlGenerator.deleteAll(information));
	}
	
    @SuppressWarnings("hiding")
	private <T>List<T> toList(Iterable<T> iterable) {

        if (iterable instanceof List) {
            return (List<T>) iterable;
        }

        List<T> result = new ArrayList<T>();
        for (T item : iterable) {
            result.add(item);
        }

        return result;
    }
    
	private Object[] flatten(List<ID> ids) {
        List<Object> result = new ArrayList<Object>();
        for (ID id : ids) {
            result.addAll(information.getCompositeIdAttributeValue(id));
        }
        return result.toArray();
    }
}
