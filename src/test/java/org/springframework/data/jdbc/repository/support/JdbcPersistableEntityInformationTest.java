package org.springframework.data.jdbc.repository.support;

import static org.junit.Assert.*;

import java.util.Set;

import org.junit.Before;
import org.junit.Test;
import org.springframework.data.jdbc.domain.sample.Address;
import org.springframework.data.jdbc.domain.sample.Bar;
import org.springframework.data.jdbc.domain.sample.Foo;
import org.springframework.data.jdbc.mapping.JdbcMappingContext;
import org.springframework.data.jdbc.mapping.JdbcPersistentEntity;
import org.springframework.data.jdbc.mapping.JdbcPersistentProperty;

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
		
		bar = new Bar("ka", "kb");
		
		foo = new Foo(bar);
		foo.setName("foo");
		foo.setAddR(address);
		
		Set<Class<?>> initialEntitySet = Sets.newHashSet(Foo.class);
		
		JdbcMappingContext jdbcMappingContext = new JdbcMappingContext();
		jdbcMappingContext.setInitialEntitySet(initialEntitySet);
		jdbcMappingContext.initialize();
		
		JdbcPersistentEntity<?> entity = jdbcMappingContext.getPersistentEntity(Foo.class);
		information = new JdbcPersistableEntityInformation(entity, jdbcMappingContext);
	}

	@Test
	public void test() throws Exception {
		
		assertEquals("Foo", information.getEntityName());

		assertEquals(2, information.getIdAttributeNames().size());
		assertEquals("a", information.getIdAttributeNames().get(0));
		assertEquals("b", information.getIdAttributeNames().get(1));
		
		assertEquals(2, information.getCompositeIdAttributeValue(bar).size());
		assertEquals("ka", information.getCompositeIdAttributeValue(bar).get(0));
		assertEquals("kb", information.getCompositeIdAttributeValue(bar).get(1));
		
		JdbcPersistentProperty property = information.getPropertyByFieldName("city");
		assertNotNull(property);
		
		property = information.getPropertyByFieldName("addR");
		assertNull(property);
		
		assertEquals(Foo.class, information.getRootPersistentEntity().getType());
		assertNotEquals(Bar.class, information.getRootPersistentEntity().getType());
		
		assertEquals(Bar.class, information.getPersistentEntity(Bar.class).getType());
	}
}
