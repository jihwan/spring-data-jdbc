package info.zhwan.data.mapping;

import org.junit.Before;
import org.junit.Test;
import org.springframework.data.mapping.model.BasicPersistentEntity;

import study.domain.Factory;

public class MappingTests {

	JdbcMappingContext context;
	BasicPersistentEntity<Object, JdbcPersistentProperty> pe;
	
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
