package org.springframework.data.jdbc.repository.support;

import java.io.Serializable;
import java.util.Map;

import org.springframework.jdbc.core.RowMapper;

/**
 * db 2 entity. entity 2 map
 * 
 * @author Jihwan Hwang
 *
 * @param <T>
 */
public interface BeanPropertyMapper<T> extends RowMapper<T>, Serializable {
	
	/**
	 * insert, update 용도. entity field 값이 null일 경우는 map 에 담지 않는다. 
	 * 
	 * @param entity
	 * @return
	 */
	Map<String, Object> toMap(T entity);
}
