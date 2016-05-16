package org.springframework.data.jdbc.repository.config;

import static org.junit.Assert.assertNotNull;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.support.GenericApplicationContext;
import org.springframework.data.jdbc.repository.JdbcRepository;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import study.domain.Factory;
import study.domain.FactoryDao;
import study.domain.Machine;
import testconfig.H2JavaConfig;
import util.BeanDefinitionUtils;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes={EnableJdbcRepositoriesTest.EnableJdbcRepositoriesTestAppConfig.class})
public class EnableJdbcRepositoriesTest {

	@Autowired
	GenericApplicationContext context;
	
	@Autowired
	JdbcRepository<Factory, String> factoryDao;
	
	final String factoryName = "T";
	Factory factory;
	Machine machine;
	
	@Before
	public void before() {
		factory = new Factory(factoryName);
	}
	
	@Test
	public void test() {
		assertNotNull(context);
		BeanDefinitionUtils.printBeanDefinitions(context);
		
		factoryDao.save(factory);
	}
	
	
	@Configuration
	@Import({H2JavaConfig.class})
	@EnableJdbcRepositories(basePackageClasses={
			FactoryDao.class
	})
	static class EnableJdbcRepositoriesTestAppConfig {
	}
}
