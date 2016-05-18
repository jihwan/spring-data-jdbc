package org.springframework.data.jdbc.repository.support;

import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.PropertyAccessorFactory;
import org.springframework.data.jdbc.mapping.JdbcMappingContext;
import org.springframework.data.jdbc.mapping.JdbcPersistentEntityImpl;
import org.springframework.data.jdbc.mapping.JdbcPersistentProperty;
import org.springframework.data.mapping.PersistentEntity;
import org.springframework.data.mapping.PersistentPropertyAccessor;
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
		
		resolvePersistentEntity(this.mappedClass);
	}
	
	
	protected void resolvePersistentEntity(Class<?> associatedPropertyClass) {
		
//		JdbcMappingContext jdbcMappingContext = entityInformation.getJdbcMappingContext();
		JdbcPersistentEntityImpl<?> persistentEntity = jdbcMappingContext.getPersistentEntity(associatedPropertyClass);
		
		PropertyDescriptor[] pds = BeanUtils.getPropertyDescriptors(associatedPropertyClass);
		for (int i = 0; i < pds.length; i++) {
			
			PropertyDescriptor pd = pds[i];
			JdbcPersistentProperty persistentProperty = 
					persistentEntity.getPersistentProperty(pd.getName());
			
			
			
			if (pd.getWriteMethod() != null && persistentProperty != null) {
				
				System.out.println( associatedPropertyClass + "\t" + persistentProperty.getOwnerFieldName()  );
				
				
				
				if (persistentProperty.isAssociation()) {
					
					/**
					 * important!!!
					 */
//					persistentProperty.setOwnerFieldName(pd.getName());
					
					resolvePersistentEntity(persistentProperty.getActualType());
				}
				else {
					this.mappedFields.put(pd.getName().toLowerCase(), persistentProperty);
				}
				
//				if (persistentProperty.isIdProperty()) {
//					if (persistentProperty.isAssociation()) {
//						resolvePersistentEntity(persistentProperty.getActualType());
//					}
//					else {
//						this.mappedFields.put(pd.getName().toLowerCase(), persistentProperty);
//					}
//				}
//				else if (persistentProperty.isColumn()) {
//					if (persistentProperty.isAssociation()) {
//						resolvePersistentEntity(persistentProperty.getActualType());
//					}
//					else {
//						this.mappedFields.put(pd.getName().toLowerCase(), persistentProperty);
//					}
//				}
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

		for (int index = 1; index <= columnCount; index++) {
			
			String column = JdbcUtils.lookupColumnName(rsmd, index);
			JdbcPersistentProperty jdbcPersistentProperty = this.mappedFields.get(column.toLowerCase());
			if(jdbcPersistentProperty == null) {
				continue;
			}
			
			
			PersistentEntity<?, JdbcPersistentProperty> ownerEntity = jdbcPersistentProperty.getOwner();
			
			System.out.println( ">>>>>" + "\t" + jdbcPersistentProperty.getOwnerFieldName()  + "\t" + ownerEntity.getPersistentProperty(jdbcPersistentProperty.getOwnerFieldName()).getOwnerFieldName());
			
//			jdbcPersistentProperty.isEntity()
			
			
			
			
			
			
			Object columnValue = getColumnValue(rs, index, jdbcPersistentProperty);
			
			if (ownerEntity.getType() == getMappedClass()) {
//				PersistentPropertyAccessor propertyAccessor = entity.getPropertyAccessor(bw);
//				System.out.println(propertyAccessor);
				bw.setPropertyValue(jdbcPersistentProperty.getName(), columnValue);
				System.err.println(
						column.toLowerCase() + "\t" + 
								jdbcPersistentProperty.getName() + "\t" + 
								columnValue + "\t" + 
								ownerEntity.getIdProperty() + "\t ownerName:" + 
								ownerEntity.getName() + "\t" + 
								jdbcPersistentProperty
						);
			}
			else {
				
				JdbcPersistentEntityImpl<?> persistentEntity = jdbcMappingContext.getPersistentEntity(ownerEntity.getType());
//				System.out.println( persistentEntity );
				
//				PropertyDescriptor[] propertyDescriptors = BeanUtils.getPropertyDescriptors(jdbcPersistentProperty.getClass());
//				System.err.println(propertyDescriptors);
				
				
				BeanWrapper associationBw = PropertyAccessorFactory.forBeanPropertyAccess(BeanUtils.instantiate(ownerEntity.getType()));
				
				associationBw.setPropertyValue(jdbcPersistentProperty.getName(), columnValue);
				
//				bw.setPropertyValue(column.toLowerCase(), associationBw.getWrappedInstance());
				
				
//				System.out.println(
//						jdbcPersistentProperty.getOwnerFieldName() + "\t" + 
//						
//						column.toLowerCase() + "\t" + 
//								jdbcPersistentProperty.getName() + "\t" + 
//								columnValue + "\t" + 
//								ownerEntity.getIdProperty() + "\t ownerName:" + 
//								ownerEntity.getName() + "\t" + 
//								jdbcPersistentProperty
//						);
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
