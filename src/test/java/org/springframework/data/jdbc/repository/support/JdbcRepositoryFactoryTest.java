package org.springframework.data.jdbc.repository.support;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;
import org.springframework.data.jdbc.domain.sample.Factory;
import org.springframework.data.jdbc.domain.sample.FactoryDao;
import org.springframework.data.jdbc.domain.sample.Machine;
import org.springframework.data.jdbc.domain.sample.MachineDao;
import org.springframework.data.jdbc.mapping.JdbcMappingContext;
import org.springframework.data.jdbc.repository.support.JdbcRepositoryFactory;
import org.springframework.jdbc.core.JdbcTemplate;

public class JdbcRepositoryFactoryTest {
	
	final String factoryName = "T";
	final String machineName = "AOI";
	
	Factory factory;
	Machine machine;
	
	JdbcTemplate jdbc = new JdbcTemplate();
	JdbcMappingContext jmc = new JdbcMappingContext();
	JdbcRepositoryFactory jdbcRepositoryFactory = new JdbcRepositoryFactory(jdbc, jmc);
	
	@Before
	public void before() {
		factory = new Factory(factoryName);
		machine = new Machine(machineName);
	}

	@Test
	public void testGetRepository() {
		
		FactoryDao factoryDao = jdbcRepositoryFactory.getRepository(FactoryDao.class);
		Factory f = factoryDao.save(factory);
		assertEquals(factoryName, f.getName());
		
		MachineDao machineDao = jdbcRepositoryFactory.getRepository(MachineDao.class);
		Machine m = machineDao.save(machine);
		assertEquals(machineName, m.getId());
	}

}
