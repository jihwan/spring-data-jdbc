package org.springframework.data.jdbc.repository.support;

import java.beans.PropertyDescriptor;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.PropertyAccessorFactory;
import org.springframework.data.jdbc.mapping.JdbcMappingContext;
import org.springframework.data.jdbc.mapping.JdbcPersistentEntity;
import org.springframework.data.jdbc.mapping.JdbcPersistentEntityImpl;
import org.springframework.data.jdbc.mapping.JdbcPersistentProperty;
import org.springframework.data.mapping.PersistentEntity;
import org.springframework.jdbc.support.JdbcUtils;
import org.springframework.util.Assert;

public class JdbcBeanPropertyMapper<T> implements BeanPropertyMapper<T> {

	protected Class<T> mappedClass;
	
	protected JdbcMappingContext jdbcMappingContext;
	
	private Map<String, JdbcPersistentProperty> mappedFields;
	
	public JdbcBeanPropertyMapper() {}
	
	public Class<T> getMappedClass() {
		return mappedClass;
	}

	public void setMappedClass(Class<T> mappedClass) {
		this.mappedClass = mappedClass;
	}
	
	public JdbcMappingContext getJdbcMappingContext() {
		return jdbcMappingContext;
	}
	
	public void setJdbcMappingContext(JdbcMappingContext jdbcMappingContext) {
		this.jdbcMappingContext = jdbcMappingContext;
	}
	
	public Map<String, JdbcPersistentProperty> getMappedFields() {
		return mappedFields;
	}
	
	protected void initialize() {

		Assert.notNull(jdbcMappingContext);
		Assert.notNull(mappedClass);
		this.mappedFields = new HashMap<String, JdbcPersistentProperty>();
		
		JdbcPersistentEntityImpl<?> persistentEntity = jdbcMappingContext.getPersistentEntity(this.mappedClass);
		resolvePersistentEntity(persistentEntity);
	}
	
	protected void resolvePersistentEntity(JdbcPersistentEntityImpl<?> persistentEntity) {
		
		PropertyDescriptor[] pds = BeanUtils.getPropertyDescriptors(persistentEntity.getType());
		for (int i = 0; i < pds.length; i++) {
			
			PropertyDescriptor pd = pds[i];
			JdbcPersistentProperty persistentProperty = 
					persistentEntity.getPersistentProperty(pd.getName());
			
			if (pd.getWriteMethod() != null && persistentProperty != null) {
				if (persistentProperty.isAssociation()) {
					resolveAssociationProperty(persistentProperty);
				}
				else {
					this.mappedFields.put(pd.getName().toLowerCase(), persistentProperty);
				}
			}
		}
	}
	
	protected void resolveAssociationProperty(JdbcPersistentProperty associationProperty) {
		
		JdbcPersistentEntity<?> persistentEntity = jdbcMappingContext.getPersistentEntity(associationProperty.getActualType());
		PropertyDescriptor[] pds = BeanUtils.getPropertyDescriptors(persistentEntity.getType());
		for (int i = 0; i < pds.length; i++) {
			PropertyDescriptor pd = pds[i];
			
			if (pd.getWriteMethod() != null && associationProperty != null) {
				JdbcPersistentProperty persistentProperty = 
						persistentEntity.getPersistentProperty(pd.getName());
				
				/**
				 * problem ... bad code...
				 */
				persistentProperty.setOwnerFieldName(associationProperty.getName());
				
				this.mappedFields.put(pd.getName().toLowerCase(), persistentProperty);
			}
		}
	}
	
	@Override
	public T mapRow(ResultSet rs, int rowNum) throws SQLException {
		
		Assert.state(this.mappedClass != null, "Mapped class was not specified");
		T mappedObject = BeanUtils.instantiate(this.mappedClass);
		BeanWrapper bw = PropertyAccessorFactory.forBeanPropertyAccess(mappedObject);
		
		ResultSetMetaData rsmd = rs.getMetaData();
		int columnCount = rsmd.getColumnCount();
		
		
		Map<Class<?>, BeanWrapper> assoMap = new HashMap<Class<?>, BeanWrapper>();

		for (int index = 1; index <= columnCount; index++) {
			
			String column = JdbcUtils.lookupColumnName(rsmd, index);
			JdbcPersistentProperty jdbcPersistentProperty = this.mappedFields.get(column.toLowerCase());
			if(jdbcPersistentProperty == null) {
				continue;
			}
			
			Object columnValue = getColumnValue(rs, index, jdbcPersistentProperty);
			
			PersistentEntity<?, JdbcPersistentProperty> ownerEntity = jdbcPersistentProperty.getOwner();
			if (ownerEntity.getType() == getMappedClass()) {
				bw.setPropertyValue(jdbcPersistentProperty.getName(), columnValue);
			}
			else {
				BeanWrapper associationBw;
				if(assoMap.containsKey(ownerEntity.getType())) {
					associationBw = assoMap.get(ownerEntity.getType());
				}
				else {
					associationBw = PropertyAccessorFactory.forBeanPropertyAccess(BeanUtils.instantiate(ownerEntity.getType()));
					assoMap.put(ownerEntity.getType(), associationBw);
				}
				associationBw.setPropertyValue(jdbcPersistentProperty.getName(), columnValue);
				bw.setPropertyValue(jdbcPersistentProperty.getOwnerFieldName(), associationBw.getWrappedInstance());
			}
		}
		
		return mappedObject;
	}

	@Override
	public Map<String, Object> toMap(T entity) {
		// TODO Auto-generated method stub
		return null;
	}
	
	protected Object getColumnValue(ResultSet rs, int index, JdbcPersistentProperty entity) throws SQLException {
		return JdbcUtils.getResultSetValue(rs, index, entity.getType());
	}

	public static <T> JdbcBeanPropertyMapper<T> newInstance(Class<T> mappedClass, JdbcMappingContext jdbcMappingContext) {
		JdbcBeanPropertyMapper<T> newInstance = new JdbcBeanPropertyMapper<T>();
		newInstance.setMappedClass(mappedClass);
		newInstance.setJdbcMappingContext(jdbcMappingContext);
		newInstance.initialize();
		return newInstance;
	}
}
