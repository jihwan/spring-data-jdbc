package org.springframework.data.jdbc.repository.support;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import org.springframework.data.jdbc.mapping.JdbcPersistentEntity;
import org.springframework.data.jdbc.mapping.JdbcPersistentProperty;
import org.springframework.data.repository.core.EntityInformation;

public interface JdbcEntityInformation<T, ID extends Serializable> extends EntityInformation<T, ID> {
	
	/**
	 * domain entity {@link Class#getSimpleName()} 을 리턴.
	 * 테이블 명으로 사용 한다.
	 * 
	 * @return
	 */
	String getEntityName();
	
	/**
	 * domain entity 의 id field name 목록을 리턴.
	 * 
	 * @return Id field names
	 */
	List<String> getIdAttributeNames();
	
	/**
	 * 
	 * @param id
	 * @return {@link List} (Id field values)
	 */
	List<Object> getCompositeIdAttributeValue(final Serializable id);
	
	/**
	 * id값을 포함한 모든 field 값중, id key를 제거한 순수 column 젗보로 다시 리턴.
	 * 
	 * @param entityMap (id or all columns. key is a field name. value is field value.
	 * @return Map (removed id value)
	 */
	Map<String, Object> removeIdAttribute(final Map<String, ?> entityMap);
	
	/**
	 * entity class에서 관리 하고 있는 모든 field name을 기반으로, 
	 * 해당 {@link JdbcPersistentProperty} 를 리턴
	 * 
	 * @param fieldName
	 * @return {@link JdbcPersistentProperty}
	 */
	JdbcPersistentProperty getPropertyByFieldName(final String fieldName);
	
	/**
	 * Root Entity 정보를 가져올때 사용
	 * @return
	 */
	JdbcPersistentEntity<T> getRootPersistentEntity();
	
	/**
	 * entity 자체 혹은 내부 associate entity class 정보를 가져 올때 사용
	 * 
	 * @param clazz
	 * @return
	 */
	JdbcPersistentEntity<?> getPersistentEntity(Class<?> clazz);
}