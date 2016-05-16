package org.springframework.data.jdbc.mapping;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import study.domain.Factory;
import study.domain.Foo;

public class MappingTests {

	JdbcMappingContext context;
	JdbcPersistentEntityImpl<?> pe;
	
	@Before
	public void setup() {
		context = new JdbcMappingContext();
		pe = context.getPersistentEntity(Factory.class);
	}

	@Ignore
	@Test
	public void test1() throws Exception {
		
		System.err.println(
				pe.getIdProperty().getName()
				);
	}
	
	@Test
	public void testFoo() throws Exception {
		
		
		JdbcPersistentEntityImpl<?> jdbcPe = context.getPersistentEntity(Foo.class);
		JdbcPersistentProperty idProperty = jdbcPe.getIdProperty();
		System.out.println( idProperty );
		System.out.println( idProperty.getName() );
		System.out.println( idProperty.getActualType() );
		System.out.println( idProperty.getAssociation() );
		System.out.println( idProperty.getComponentType() );
		System.out.println( idProperty.getField() );
		System.out.println( idProperty.getPersistentEntityType() );
		System.out.println( idProperty.getRawType() );
		System.out.println( idProperty.getType() );
		System.out.println( idProperty.getTypeInformation() );
	}
	
}
