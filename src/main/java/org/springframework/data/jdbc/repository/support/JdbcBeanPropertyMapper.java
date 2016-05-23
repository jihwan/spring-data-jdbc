package org.springframework.data.jdbc.repository.support;

import java.io.Serializable;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.core.convert.support.GenericConversionService;
import org.springframework.data.jdbc.domain.JdbcPersistable;
import org.springframework.data.jdbc.mapping.JdbcPersistentEntity;
import org.springframework.data.jdbc.mapping.JdbcPersistentProperty;
import org.springframework.data.mapping.Association;
import org.springframework.data.mapping.AssociationHandler;
import org.springframework.data.mapping.PersistentPropertyAccessor;
import org.springframework.data.mapping.PropertyHandler;
import org.springframework.data.mapping.model.ConvertingPropertyAccessor;
import org.springframework.data.mapping.model.MappingException;
import org.springframework.jdbc.support.JdbcUtils;
import org.springframework.util.Assert;

/**
 * 
 * 
 * @author Jihwan Hwang
 *
 * @param <T>
 */
public class JdbcBeanPropertyMapper<T> implements BeanPropertyMapper<T> {
	
	protected static final Logger LOGGER = LoggerFactory.getLogger(JdbcBeanPropertyMapper.class);

	protected GenericConversionService conversionService = new GenericConversionService();
//	protected EntityInstantiators instantiators = new EntityInstantiators();
	
	protected JdbcEntityInformation<T, Serializable> information;
	

	public JdbcEntityInformation<T, Serializable> getInformation() {
		return information;
	}
	
	public void setInformation(JdbcEntityInformation<T, Serializable> information) {
		this.information = information;
	}
	
	
	protected void initialize() {

		Assert.notNull(information);
	}
	
	public T mapRow(ResultSet rs, int rowNum) throws SQLException {

		JdbcPersistentEntity<T> persistentEntity = (JdbcPersistentEntity<T>) information.getPersistentEntity();
		if (persistentEntity == null) {
			throw new MappingException("No mapping metadata found for " + information.getEntityName());
		}
		
		ResultSetMetaData rsmd = rs.getMetaData();
		int columnCount = rsmd.getColumnCount();
		
		DBObject dbObject = new DBObject();
		
		for (int index = 1; index <= columnCount; index++) {

			String column = JdbcUtils.lookupColumnName(rsmd, index);
			JdbcPersistentProperty jdbcPersistentProperty = information.getMetaInfo().get(column.toLowerCase());
			if(jdbcPersistentProperty == null) {
				continue;
			}
			
			Object columnValue = getColumnValue(rs, index, jdbcPersistentProperty);			
			dbObject.put(jdbcPersistentProperty.getField(), columnValue);
		}
		
		T read = read(persistentEntity, dbObject);
		@SuppressWarnings("unchecked")
		JdbcPersistable<T, ?> cast = JdbcPersistable.class.cast(read);
		cast.persist(true);
		return read;
	}
	
	protected T read(final JdbcPersistentEntity<?> entity, final DBObject dbObject) {
		
		@SuppressWarnings("unchecked")
		T instance = (T) BeanUtils.instantiate(entity.getType());
		
		final PersistentPropertyAccessor accessor = 
				new ConvertingPropertyAccessor(entity.getPropertyAccessor(instance), conversionService);
		
		entity.doWithProperties(new PropertyHandler<JdbcPersistentProperty>() {
			@Override
			public void doWithPersistentProperty(JdbcPersistentProperty persistentProperty) {
				
				Object object = dbObject.get(persistentProperty.getField());
				accessor.setProperty(persistentProperty, object);
			}
		});
		
		entity.doWithAssociations(new AssociationHandler<JdbcPersistentProperty>() {
			@Override
			public void doWithAssociation(Association<JdbcPersistentProperty> association) {
				
				JdbcPersistentProperty property = association.getInverse();
				JdbcPersistentEntity<?> acturalEntity = information.getPersistentEntity(property.getActualType());
				T object = read(acturalEntity, dbObject);
				accessor.setProperty(property, object);
			}
		});
		
		return instance;
	}

	@Override
	public Map<String, Object> toMap(T entityObject) {
		
		Assert.notNull(entityObject);

		JdbcPersistentEntity<?> entity = information.getPersistentEntity(entityObject.getClass());
		if (entity == null) {
			throw new MappingException("No mapping metadata found for " + information.getEntityName());
		}
		
		Map<String, Object> map = new HashMap<String, Object>();
		return write(entity, entityObject, map);
	}
	
	protected Map<String, Object> write(final JdbcPersistentEntity<?> entity, final Object entityObject, final Map<String, Object> map) {
		
		final PersistentPropertyAccessor accessor = 
				new ConvertingPropertyAccessor(entity.getPropertyAccessor(entityObject), conversionService);
		
		entity.doWithProperties(new PropertyHandler<JdbcPersistentProperty>() {
			@Override
			public void doWithPersistentProperty(JdbcPersistentProperty persistentProperty) {
				
				Object value = accessor.getProperty(persistentProperty);
				if(value != null) {
					map.put(persistentProperty.getName(), value);
				}
			}
		});
		
		entity.doWithAssociations(new AssociationHandler<JdbcPersistentProperty>() {
			@Override
			public void doWithAssociation(Association<JdbcPersistentProperty> association) {
				
				JdbcPersistentProperty property = association.getInverse();
				Object innerObject = accessor.getProperty(property);
				if (innerObject != null) {
					JdbcPersistentEntity<?> acturalEntity = information.getPersistentEntity(property.getActualType());
					write(acturalEntity, innerObject, map);
				}
			}
		});
		
		return map;
	}
	
	protected Object getColumnValue(ResultSet rs, int index, JdbcPersistentProperty entity) throws SQLException {
		return JdbcUtils.getResultSetValue(rs, index, entity.getType());
	}
	
	public static <T> JdbcBeanPropertyMapper<T> newInstance(JdbcEntityInformation<T, Serializable> information) {
		JdbcBeanPropertyMapper<T> newInstance = new JdbcBeanPropertyMapper<T>();
		newInstance.setInformation(information);
		newInstance.initialize();
		return newInstance;
	}
}
