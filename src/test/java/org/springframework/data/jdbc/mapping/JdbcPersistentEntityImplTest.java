package org.springframework.data.jdbc.mapping;

import static org.junit.Assert.*;

import java.util.Iterator;

import org.junit.Before;
import org.junit.Test;
import org.springframework.data.jdbc.domain.sample.Foo;
import org.springframework.data.mapping.PreferredConstructor;
import org.springframework.data.mapping.PreferredConstructor.Parameter;
import org.springframework.data.util.ClassTypeInformation;

public class JdbcPersistentEntityImplTest {

	JdbcPersistentEntity<Foo> fooEntity;
	
	@Before
	public void setup() {
		fooEntity = new JdbcPersistentEntityImpl<Foo>(ClassTypeInformation.from(Foo.class));
	}
	
	@Test
	public void test() {
		
		assertEquals(Foo.class.getName(), fooEntity.getName());
		
		PreferredConstructor<Foo,JdbcPersistentProperty> persistenceConstructor = fooEntity.getPersistenceConstructor();
		Iterator<Parameter<Object, JdbcPersistentProperty>> iterator = persistenceConstructor.getParameters().iterator();
		assertEquals(false, iterator.hasNext());
	}
}
