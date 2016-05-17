package org.springframework.data.jdbc.repository.support;

import java.beans.PropertyDescriptor;
import java.io.Serializable;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.PropertyAccessorFactory;
import org.springframework.data.jdbc.domain.JdbcPersistable;
import org.springframework.data.jdbc.mapping.JdbcMappingContext;
import org.springframework.data.jdbc.mapping.JdbcPersistentEntity;
import org.springframework.data.jdbc.mapping.JdbcPersistentEntityImpl;
import org.springframework.data.jdbc.mapping.JdbcPersistentProperty;
import org.springframework.data.repository.core.support.PersistentEntityInformation;

public class JdbcPersistableEntityInformation<T extends JdbcPersistable<T, Serializable>, ID extends Serializable> 
	extends PersistentEntityInformation<T, ID>
	implements JdbcEntityInformation<T, ID> {
	
	JdbcPersistentEntity<T> persistentEntity;
	JdbcMappingContext jdbcMappingContext;
	
	public JdbcPersistableEntityInformation(JdbcPersistentEntity<T> entity) {
		super(entity);
		this.persistentEntity = entity;
	}
	
	public JdbcPersistableEntityInformation(JdbcPersistentEntity<T> entity, JdbcMappingContext jdbcMappingContext) {
		super(entity);
		this.persistentEntity = entity;
		this.jdbcMappingContext = jdbcMappingContext;
	}

	@Override
	public boolean isNew(T entity) {
		return entity.isNew();
	}
	
	@Override
	public void entity2Map(T entity) {
		
		
		BeanWrapper bw = PropertyAccessorFactory.forBeanPropertyAccess(entity);
		BeanWrapper compositeBw;
		
		JdbcPersistentProperty idProperty = persistentEntity.getIdProperty();
		
		if( idProperty.isAssociation() ) {
			JdbcPersistentEntityImpl<?> associationEntity = jdbcMappingContext.getPersistentEntity(idProperty);
			Object associationValue =  bw.getPropertyValue(idProperty.getName());
			
			compositeBw = PropertyAccessorFactory.forBeanPropertyAccess(associationValue);
			
			PropertyDescriptor[] pdescs = BeanUtils.getPropertyDescriptors(idProperty.getActualType());
			
			for (PropertyDescriptor pdesc : pdescs) {
				JdbcPersistentProperty persistentProperty2 = associationEntity.getPersistentProperty(pdesc.getName());
				if (persistentProperty2 != null) {
					
					if ( persistentProperty2.isColumn() ) {
						
						Object propertyValue = compositeBw.getPropertyValue(persistentProperty2.getName());
						System.err.println( persistentProperty2.getName() + "\t" + propertyValue );
					}
				}
			}
		}
		else {
			Object propertyValue = bw.getPropertyValue(idProperty.getName());
			System.err.println( idProperty.getName() + "\t" + propertyValue );
		}
		
		
		
		PropertyDescriptor[] propertyDescriptors = BeanUtils.getPropertyDescriptors(entity.getClass());
		for (PropertyDescriptor propertyDescriptor : propertyDescriptors) {
			JdbcPersistentProperty persistentProperty = persistentEntity.getPersistentProperty(propertyDescriptor.getName());
			if ( persistentProperty != null ) {
				
				if ( persistentProperty.isColumn() ) {
					
					Object propertyValue = bw.getPropertyValue(persistentProperty.getName());
					System.err.println( persistentProperty.getName() + "\t" + propertyValue );
				}
				
				if( persistentProperty.isAssociation() && !persistentProperty.isIdProperty() ) {
					
					JdbcPersistentEntityImpl<?> associationEntity = jdbcMappingContext.getPersistentEntity(persistentProperty);
					Object associationValue =  bw.getPropertyValue(persistentProperty.getName());
					compositeBw = PropertyAccessorFactory.forBeanPropertyAccess(associationValue);
					
					PropertyDescriptor[] pdescs = BeanUtils.getPropertyDescriptors(persistentProperty.getActualType());
					
					for (PropertyDescriptor pdesc : pdescs) {
						JdbcPersistentProperty persistentProperty2 = associationEntity.getPersistentProperty(pdesc.getName());
						if (persistentProperty2 != null) {
							
							if ( persistentProperty2.isColumn() ) {
								
								Object propertyValue = compositeBw.getPropertyValue(persistentProperty2.getName());
								System.err.println( persistentProperty2.getName() + "\t" + propertyValue );
							}
						}
					}
				}
			}
		}
		
	}

//	@Override
//	public SingularAttribute<? super T, ?> getIdAttribute() {
//		return null;
//	}
//
//	@Override
//	public boolean hasCompositeId() {
//		// TODO Auto-generated method stub
//		return false;
//	}
//
//	@Override
//	public Iterable<String> getIdAttributeNames() {
//		// TODO Auto-generated method stub
//		return null;
//	}
//
//	@Override
//	public Object getCompositeIdAttributeValue(Serializable id, String idAttribute) {
//		// TODO Auto-generated method stub
//		return null;
//	}
}
