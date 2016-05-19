package org.springframework.data.jdbc.repository.support;

import java.beans.PropertyDescriptor;
import java.util.Set;

import org.junit.Test;
import org.springframework.beans.BeanUtils;
import org.springframework.data.jdbc.domain.sample.Foo;
import org.springframework.data.jdbc.mapping.JdbcMappingContext;
import org.springframework.data.jdbc.mapping.JdbcPersistentEntityImpl;
import org.springframework.data.jdbc.mapping.JdbcPersistentProperty;

import com.google.common.collect.Sets;

public class JdbcPersistableEntityInformationTest {

	@Test
	public void test() {
		
		Set<Class<?>> initialEntitySet = Sets.newHashSet(Foo.class);
		
		JdbcMappingContext jdbcMappingContext = new JdbcMappingContext(initialEntitySet);
		jdbcMappingContext.setInitialEntitySet(initialEntitySet);
		jdbcMappingContext.initialize();
		
		
		JdbcPersistentEntityImpl<?> entity = jdbcMappingContext.getPersistentEntity(Foo.class);
		
		
//		System.err.println(entity.getPersistentProperty("id").isAssociation());
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
