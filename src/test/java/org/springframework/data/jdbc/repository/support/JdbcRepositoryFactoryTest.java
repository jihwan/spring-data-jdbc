//package org.springframework.data.jdbc.repository.support;
//
//import static org.junit.Assert.assertEquals;
//
//import java.util.Set;
//
//import org.junit.Before;
//import org.junit.Test;
//import org.springframework.data.jdbc.domain.sample.Foo;
//import org.springframework.data.jdbc.mapping.JdbcMappingContext;
//import org.springframework.data.jdbc.repository.support.JdbcRepositoryFactory;
//import org.springframework.jdbc.core.JdbcTemplate;
//
//import com.google.common.collect.Sets;
//
//public class JdbcRepositoryFactoryTest {
//	
//	final String factoryName = "T";
//	final String machineName = "AOI";
//	
//	Set<Class<?>> initialEntitySet = Sets.newHashSet(Factory.class, Machine.class);
//	
//	JdbcTemplate jdbc = new JdbcTemplate();
//	JdbcMappingContext jmc = new JdbcMappingContext(initialEntitySet);
//	JdbcRepositoryFactory jdbcRepositoryFactory = new JdbcRepositoryFactory(jdbc, jmc);
//	
//	@Before
//	public void before() {
//		factory = new Factory(factoryName);
//		machine = new Machine(machineName);
//	}
//
//	@Test
//	public void testGetRepository() {
//		
//		FactoryDao factoryDao = jdbcRepositoryFactory.getRepository(FactoryDao.class);
//		Factory f = factoryDao.save(factory);
//		assertEquals(factoryName, f.getName());
//		
//		MachineDao machineDao = jdbcRepositoryFactory.getRepository(MachineDao.class);
//		Machine m = machineDao.save(machine);
//		assertEquals(machineName, m.getId());
//	}
//
//}
