package org.springframework.data.jdbc.mapping;

import java.util.Set;

import org.junit.Before;
import org.junit.Test;
import org.springframework.data.jdbc.domain.sample.Bar;
import org.springframework.data.jdbc.domain.sample.Foo;

import com.google.common.collect.Sets;
import com.sun.jndi.cosnaming.IiopUrl.Address;

public class MappingTests {

	JdbcMappingContext context;
//	JdbcPersistentEntityImpl<?> pe;
	
	@Before
	public void setup() {
		Set<Class<?>> initialEntitySet = Sets.newHashSet(Foo.class);
		context = new JdbcMappingContext(initialEntitySet);
//		pe = context.getPersistentEntity(Factory.class);
	}

//	@Ignore
//	@Test
//	public void test1() throws Exception {
//		
//		System.err.println(
//				pe.getIdProperty().getName()
//				);
//	}
	
	@Test
	public void testFoo() throws Exception {
		
		JdbcPersistentEntityImpl<?> fooE = context.getPersistentEntity(Foo.class);
		System.out.println(fooE);
//		fooE.getPersistentProperty(annotationType)
		
//		JdbcPersistentProperty idProperty = fooE.getIdProperty();
//		System.out.println( idProperty );
//		System.out.println( idProperty.getName() );
//		System.out.println( idProperty.getActualType() );
//		System.out.println( idProperty.getAssociation() );
//		System.out.println( idProperty.getComponentType() ); // null
//		System.out.println( idProperty.getField() );
//		System.out.println( idProperty.getPersistentEntityType() );
//		System.out.println( idProperty.getRawType() );
//		System.out.println( idProperty.getType() );
//		System.out.println( idProperty.getTypeInformation() );
		
		
		JdbcPersistentEntityImpl<?> barE = context.getPersistentEntity(Bar.class);
		System.out.println(barE);
		
		JdbcPersistentEntityImpl<?> addrE = context.getPersistentEntity(Address.class);
		System.out.println(addrE);
		
	}
	
}
