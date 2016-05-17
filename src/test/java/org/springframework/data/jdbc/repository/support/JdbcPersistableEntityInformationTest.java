package org.springframework.data.jdbc.repository.support;

import static org.junit.Assert.*;

import java.beans.PropertyDescriptor;
import java.util.Collection;
import java.util.Set;

import javax.persistence.Column;

import org.junit.Test;
import org.springframework.beans.BeanUtils;
import org.springframework.data.jdbc.domain.sample.Factory;
import org.springframework.data.jdbc.domain.sample.Foo;
import org.springframework.data.jdbc.domain.sample.Machine;
import org.springframework.data.jdbc.mapping.JdbcMappingContext;
import org.springframework.data.jdbc.mapping.JdbcPersistentEntityImpl;
import org.springframework.data.jdbc.mapping.JdbcPersistentProperty;
import org.springframework.data.util.DirectFieldAccessFallbackBeanWrapper;

import com.google.common.collect.Sets;

public class JdbcPersistableEntityInformationTest {

	@Test
	public void test() {
		
		JdbcMappingContext jdbcMappingContext = new JdbcMappingContext();
		Set<Class<?>> initialEntitySet = Sets.newHashSet(Factory.class, Machine.class, Foo.class);
		jdbcMappingContext.setInitialEntitySet(initialEntitySet);
		jdbcMappingContext.initialize();
		
		
		JdbcPersistentEntityImpl<?> entity = jdbcMappingContext.getPersistentEntity(Foo.class);
		
		
		System.err.println(entity.getPersistentProperty("id").isAssociation());
		System.err.println( entity.getIdProperty().getActualType() );
		
		
//		JdbcPersistableEntityInformation eInfo = new JdbcPersistableEntityInformation(entity);
		
		
//		JdbcPersistentProperty p = entity.getPersistentProperty(Column.class);
//		System.err.println(p);
//		p.
		
		PropertyDescriptor[] propertyDescriptors = BeanUtils.getPropertyDescriptors(Foo.class);
		for (PropertyDescriptor propertyDescriptor : propertyDescriptors) {
//			System.err.println( propertyDescriptor.getName() + "\t" + propertyDescriptor.getDisplayName() );
			
			JdbcPersistentProperty persistentProperty = entity.getPersistentProperty(propertyDescriptor.getName());
			if( persistentProperty != null ) {
				System.err.println( persistentProperty + "\t" + persistentProperty.isTransient());
			}
		}
		
//		DirectFieldAccessFallbackBeanWrapper eWrapper = new DirectFieldAccessFallbackBeanWrapper(Factory.instance("T", "desc", "type"));
//		Object propertyValue = eWrapper.getPropertyValue(entity.getIdProperty().getName());
//		System.err.println(propertyValue);
	}
}
