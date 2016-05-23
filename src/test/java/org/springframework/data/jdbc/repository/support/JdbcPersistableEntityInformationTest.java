package org.springframework.data.jdbc.repository.support;

import java.io.Serializable;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;
import org.springframework.data.jdbc.domain.sample.Address;
import org.springframework.data.jdbc.domain.sample.Bar;
import org.springframework.data.jdbc.domain.sample.Foo;
import org.springframework.data.jdbc.mapping.JdbcMappingContext;
import org.springframework.data.jdbc.mapping.JdbcPersistentEntity;

import com.google.common.collect.Sets;

@SuppressWarnings({"unchecked", "rawtypes"})
public class JdbcPersistableEntityInformationTest {
	
	Foo foo;
	Bar bar;
	Address address;
	
	JdbcEntityInformation information;
	
	@Before
	public void setup() {
		
		address = new Address();
		address.setStreet("street1");
		
		bar = new Bar("a", "b");
		
		foo = new Foo(bar);
		foo.setName("foo");
		foo.setAddR(address);
		
		
		Set<Class<?>> initialEntitySet = Sets.newHashSet(Foo.class);
		JdbcMappingContext jdbcMappingContext = new JdbcMappingContext(initialEntitySet);
		jdbcMappingContext.setInitialEntitySet(initialEntitySet);
		jdbcMappingContext.initialize();
		JdbcPersistentEntity<?> entity = jdbcMappingContext.getPersistentEntity(Foo.class);
		information = new JdbcPersistableEntityInformation(entity, jdbcMappingContext);
	}

	@Test
	public void test() {
		
		Serializable id = information.getId(foo);
		System.out.println(id);
		
		Iterable idAttributeNames = information.getIdAttributeNames();
		System.out.println(idAttributeNames);
		
		System.out.println(information.getJavaType());
		System.out.println(information.getEntityName());
		
		Map metaInfo = information.getMetaInfo();
		for (Iterator iterator = metaInfo.keySet().iterator(); iterator.hasNext();) {
			Object key = iterator.next();
			Object value = metaInfo.get(key);
			System.err.println(key + "\t" + value);
		}
		
//		PropertyDescriptor[] propertyDescriptors = BeanUtils.getPropertyDescriptors(Foo.class);
//		for (PropertyDescriptor propertyDescriptor : propertyDescriptors) {
//			System.err.println( propertyDescriptor.getName() + "\t" + propertyDescriptor.getDisplayName() );
//			JdbcPersistentProperty persistentProperty = entity.getPersistentProperty(propertyDescriptor.getName());
//			if( persistentProperty != null ) {
//				System.err.println( persistentProperty + "\t" + persistentProperty.isTransient());
//			}
//		}
		
//		DirectFieldAccessFallbackBeanWrapper eWrapper = new DirectFieldAccessFallbackBeanWrapper(Factory.instance("T", "desc", "type"));
//		Object propertyValue = eWrapper.getPropertyValue(entity.getIdProperty().getName());
//		System.err.println(propertyValue);
	}
}
