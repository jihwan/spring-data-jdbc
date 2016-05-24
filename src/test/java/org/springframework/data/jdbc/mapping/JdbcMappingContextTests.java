package org.springframework.data.jdbc.mapping;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.Set;

import org.junit.Before;
import org.junit.Test;
import org.springframework.data.jdbc.domain.sample.Address;
import org.springframework.data.jdbc.domain.sample.Bar;
import org.springframework.data.jdbc.domain.sample.Foo;

import com.google.common.collect.Sets;

public class JdbcMappingContextTests {

	JdbcMappingContext context;
	
	Class<?> clazz = Foo.class;
	
	@Before
	public void setup() {
		Set<Class<?>> initialEntitySet = Sets.newHashSet(clazz);
		context = new JdbcMappingContext();
		context.setInitialEntitySet(initialEntitySet);
		context.initialize();
	}

	@Test
	public void test() throws Exception {
		
		assertEquals(3, context.getPersistentEntities().size());
		
		JdbcPersistentEntityImpl<?> fooEntity = context.getPersistentEntity(clazz);
		assertNotNull(fooEntity);
		
		assertEquals(true, context.hasPersistentEntityFor(clazz));
		assertEquals(true, context.hasPersistentEntityFor(Bar.class));
		assertEquals(true, context.hasPersistentEntityFor(Address.class));
		
//		PersistentPropertyPath<JdbcPersistentProperty> persistentPropertyPath = 
//				context.getPersistentPropertyPath(PropertyPath.from("addR.street", clazz));
		
		assertEquals(3, context.getManagedTypes().size());
	}
	
}
