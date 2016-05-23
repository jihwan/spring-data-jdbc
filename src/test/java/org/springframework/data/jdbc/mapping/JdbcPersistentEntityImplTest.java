package org.springframework.data.jdbc.mapping;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;

import org.junit.Before;
import org.junit.Test;
import org.springframework.data.jdbc.domain.sample.Foo;
import org.springframework.data.util.ClassTypeInformation;

public class JdbcPersistentEntityImplTest {

	JdbcPersistentEntity<Foo> fooEntity;
	
	@Before
	public void setup() {
		fooEntity = new JdbcPersistentEntityImpl<Foo>(ClassTypeInformation.from(Foo.class));
	}
	
	@Test
	public void test() {
		
		PropertyDescriptor propertyDescriptor = getPropertyDescriptor(Foo.class, "addr");
		System.err.println(propertyDescriptor);
	}

	
	
	private static PropertyDescriptor getPropertyDescriptor(Class<?> type, String propertyName) {

		try {

			BeanInfo info = Introspector.getBeanInfo(type);

			for (PropertyDescriptor descriptor : info.getPropertyDescriptors()) {
				if (descriptor.getName().equals(propertyName)) {
					return descriptor;
				}
			}

			return null;

		} catch (IntrospectionException e) {
			return null;
		}
	}
}
