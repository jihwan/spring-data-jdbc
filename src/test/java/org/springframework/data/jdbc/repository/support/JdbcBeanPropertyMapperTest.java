package org.springframework.data.jdbc.repository.support;

import static org.junit.Assert.*;

import java.beans.PropertyDescriptor;
import java.util.Iterator;

import org.junit.Before;
import org.junit.Test;
import org.springframework.data.jdbc.domain.sample.Foo;
import org.springframework.data.jdbc.mapping.JdbcMappingContext;
import org.springframework.data.jdbc.mapping.JdbcPersistentEntityImpl;
import org.springframework.data.jdbc.mapping.JdbcPersistentProperty;

@SuppressWarnings({"rawtypes", "unchecked"})
public class JdbcBeanPropertyMapperTest {

	JdbcMappingContext context;
	JdbcPersistableEntityInformation information;
	
	@Before
	public void setup() {
		context = new JdbcMappingContext();
		JdbcPersistentEntityImpl<?> persistentEntity = context.getPersistentEntity(Foo.class);
		information = new JdbcPersistableEntityInformation(persistentEntity, context);
	}
	
	@Test
	public void test() {
		
		JdbcBeanPropertyMapper<Foo> newInstance = 
				JdbcBeanPropertyMapper.newInstance(Foo.class, context);
		
		for(Iterator<String> iterator = newInstance.getMappedFields().keySet().iterator(); iterator.hasNext();) {
			String key = iterator.next();
			JdbcPersistentProperty jdbcPersistentProperty = newInstance.getMappedFields().get(key);
			System.err.println(key + "\t" + jdbcPersistentProperty);
		}
		
	}

}
