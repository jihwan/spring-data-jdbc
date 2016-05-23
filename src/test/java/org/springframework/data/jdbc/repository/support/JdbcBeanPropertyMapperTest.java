//package org.springframework.data.jdbc.repository.support;
//
//import java.util.Iterator;
//import java.util.Set;
//
//import org.junit.Before;
//import org.junit.Test;
//import org.springframework.data.jdbc.domain.sample.Foo;
//import org.springframework.data.jdbc.mapping.JdbcMappingContext;
//import org.springframework.data.jdbc.mapping.JdbcPersistentEntityImpl;
//import org.springframework.data.jdbc.mapping.JdbcPersistentProperty;
//
//import com.google.common.collect.Sets;
//
//@SuppressWarnings({"rawtypes", "unchecked"})
//public class JdbcBeanPropertyMapperTest {
//
//	JdbcMappingContext context;
//	JdbcPersistableEntityInformation information;
//	
//	Class<?> fooClazz = Foo.class;
//	
//	@Before
//	public void setup() {
//		
//		Set<Class<?>> entitySet = Sets.newHashSet(fooClazz);
//		
//		context = new JdbcMappingContext(entitySet);
//		JdbcPersistentEntityImpl<?> persistentEntity = context.getPersistentEntity(fooClazz);
//		information = new JdbcPersistableEntityInformation(persistentEntity, context);
//	}
//	
//	@Test
//	public void test() {
//		
//		JdbcBeanPropertyMapper<Foo> newInstance = 
//				(JdbcBeanPropertyMapper<Foo>) JdbcBeanPropertyMapper.newInstance(fooClazz, context);
//		
//		for(Iterator<String> iterator = newInstance.getMappedFields().keySet().iterator(); iterator.hasNext();) {
//			String key = iterator.next();
//			JdbcPersistentProperty jdbcPersistentProperty = newInstance.getMappedFields().get(key);
//			System.err.println(key + "\t" + jdbcPersistentProperty);
//		}
//		
//	}
//
//}
