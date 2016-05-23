package org.springframework.data.jdbc.repository.support;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
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
        List<ID> idsList = toList(ids);
        if (idsList.isEmpty()) {
            return Collections.emptyList();
        }
        return jdbcTemplate.query(
        		sqlGenerator.selectByIds(information, idsList.size()), this.beanPropertyMapper, flatten(idsList));
	}
	
	@Override
	public T findOne(ID id) {
		List<T> entityOrEmpty = jdbcTemplate.query(
				sqlGenerator.selectById(information), this.beanPropertyMapper, information.getCompositeIdAttributeValue(id).toArray());
		return entityOrEmpty.isEmpty() ? null : entityOrEmpty.get(0);
	}
	
	@Override
	public boolean exists(ID id) {
		List<Integer> object = jdbcTemplate.queryForList(
				sqlGenerator.existsById(information), information.getCompositeIdAttributeValue(id).toArray(), Integer.class);
		return !object.isEmpty();
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
//            result.addAll(Arrays.asList(wrapToArray(id)));
        }
        return result.toArray();
    }
    
//    /**
//     * Wraps the given object into an object array. If the object is an object
//     * array, then it returns it as-is. If it's an array of primitives, then
//     * it converts it into an array of primitive wrapper objects.
//     */
//    private Object[] wrapToArray(Object obj) {
//        if (obj == null) {
//            return new Object[0];
//        }
//        if (obj instanceof Object[]) {
//            return (Object[]) obj;
//        }
//        if (obj.getClass().isArray()) {
//            return toObjectArray(obj);
//        }
//        return new Object[]{ obj };
//    }
//    
//    private Object[] toObjectArray(Object source) {
//		if (source instanceof Object[]) {
//			return (Object[]) source;
//		}
//		if (source == null) {
//			return new Object[0];
//		}
//		if (!source.getClass().isArray()) {
//			throw new IllegalArgumentException("Source is not an array: " + source);
//		}
//		int length = Array.getLength(source);
//		if (length == 0) {
//			return new Object[0];
//		}
//		Class<?> wrapperType = Array.get(source, 0).getClass();
//		Object[] newArray = (Object[]) Array.newInstance(wrapperType, length);
//		for (int i = 0; i < length; i++) {
//			newArray[i] = Array.get(source, i);
//		}
//		return newArray;
//	}
}
