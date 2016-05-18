package org.springframework.data.jdbc.repository.config;

import static org.junit.Assert.assertNotNull;

import java.sql.SQLException;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.h2.H2ConsoleAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.support.GenericApplicationContext;
import org.springframework.data.jdbc.domain.sample.Address;
import org.springframework.data.jdbc.domain.sample.Bar;
import org.springframework.data.jdbc.domain.sample.Factory;
import org.springframework.data.jdbc.domain.sample.FactoryDao;
import org.springframework.data.jdbc.domain.sample.Foo;
import org.springframework.data.jdbc.domain.sample.FooDao;
import org.springframework.data.jdbc.domain.sample.InitializeTables;
import org.springframework.data.jdbc.domain.sample.Machine;
import org.springframework.data.jdbc.repository.JdbcRepository;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import testconfig.H2JavaConfig;
import util.BeanDefinitionUtils;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes={EnableJdbcRepositoriesTest.EnableJdbcRepositoriesTestAppConfig.class})
public class EnableJdbcRepositoriesTest {

	@Autowired
	GenericApplicationContext context;
	
	@Autowired
	JdbcRepository<Factory, String> factoryDao;
	
	@Autowired
	JdbcTemplate jdbcTemplate;
	
	@Autowired
	FooDao fooDao;
	
	final String factoryName = "T";
	Address address;
	Factory factory;
	Machine machine;
	
	Foo foo;
	Bar bar;

	@Before
	public void before() throws SQLException {
		
//		Server webServer = Server.createWebServer("-web", "-webAllowOthers", "-webPort", "8080");
//	    webServer.start();
		
		address = new Address();
		address.setStreet("street1");
		
		factory = new Factory(factoryName);
		factory.setAddress(address);
		
		bar = new Bar("a", "b");
		foo = new Foo(bar);
		foo.setName("foo");
		foo.setAddr(address);
	}
	
	@Test
	public void test() {
		assertNotNull(context);
		BeanDefinitionUtils.printBeanDefinitions(context);
		
//		factoryDao.save(factory);
		
		fooDao.save(foo);
		
		jdbcTemplate.update("INSERT INTO foo (a, b, street, name) VALUES('aa', 'aa', 'bb_street', 'foo')");
		
		for(Foo myFoo : fooDao.findByName("foo")) {
			System.err.println(myFoo);
		}
		
		
		fooDao.customMethod(foo);
		
	}
	
	
	@Configuration
	@Import({H2JavaConfig.class, H2ConsoleAutoConfiguration.class})
	@EnableJdbcRepositories(basePackageClasses={
			FactoryDao.class
	})
	static class EnableJdbcRepositoriesTestAppConfig {
		@Bean
		InitializeTables initializeTables() {
			return new InitializeTables();
		}
	}
}
