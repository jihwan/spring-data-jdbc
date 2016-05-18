package org.springframework.data.jdbc.repository.support;

import java.util.Map;

import org.springframework.jdbc.core.RowMapper;

public interface BeanPropertyMapper<T> extends RowMapper<T> {

	Map<String, Object> toMap(T entity);
}
