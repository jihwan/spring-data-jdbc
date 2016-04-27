package info.zhwan.data.jdbc.repository.support;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;

import study.domain.Factory;
import study.domain.FactoryDao;
import study.domain.Machine;
import study.domain.MachineDao;

public class JdbcRepositoryFactoryTest {
	
	final String factoryName = "T";
	final String machineName = "AOI";
	
	Factory factory;
	Machine machine;
	
	JdbcRepositoryFactory jdbcRepositoryFactory = new JdbcRepositoryFactory();
	
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
		assertEquals(machineName, m.getName());
	}

}
