package org.springframework.data.jdbc.mapping;

import org.junit.Before;
import org.junit.Test;

import study.domain.Factory;

public class MappingTests {

	JdbcMappingContext context;
	JdbcPersistentEntityImpl<?> pe;
	
	@Before
	public void setup() {
		context = new JdbcMappingContext();
		pe = context.getPersistentEntity(Factory.class);
	}

	@Test
	public void test1() throws Exception {
		
		System.err.println(
				pe.getIdProperty().getName()
				);

	}
	
}
