package org.springframework.data.jdbc.repository.config;

import static org.junit.Assert.assertNotNull;

import java.sql.SQLException;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.support.GenericApplicationContext;
import org.springframework.data.jdbc.domain.InitializeTables;
import org.springframework.data.jdbc.domain.sample.Address;
import org.springframework.data.jdbc.domain.sample.Bar;
import org.springframework.data.jdbc.domain.sample.Foo;
import org.springframework.data.jdbc.domain.sample.FooDao;
import org.springframework.data.jdbc.domain.sample2.FooDaoCustom;
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
	JdbcTemplate jdbcTemplate;
	
	@Autowired
	FooDao fooDao;
	
	Bar bar;
	Foo foo;
	Address address;
	
	@Before
	public void before() throws SQLException {
		
		bar = new Bar("a", "b");
		
		address = new Address();
		address.setStreet("street1");
		
		foo = new Foo(bar);
		foo.setName("foo");
		foo.setAddR(address);
	}
	
	@Test
	public void test() {
		assertNotNull(context);
		BeanDefinitionUtils.printBeanDefinitions(context);
		
		fooDao.save(foo);
		jdbcTemplate.update("INSERT INTO foo (a, b, street, no, name) VALUES('bar_a', 'bar_b', 'addr_street', 123, 'foo_name')");
		jdbcTemplate.update("INSERT INTO foo (a, b, street, no, name) VALUES('bar_a1', 'bar_b1', 'addr_street', 123, 'foo_name2')");
		
		for(Foo myFoo : fooDao.findByName("foo_name")) {
			System.err.println(myFoo);
		}
		
		Foo customMethod = fooDao.customMethod(foo);
		assertNotNull(customMethod);
		
		Iterable<Foo> findAll = fooDao.findAll();
		for (Foo foo : findAll) {
			System.err.println(foo);
		}
	}
	
	@Configuration
	@Import({H2JavaConfig.class})
	@ComponentScan(basePackageClasses={FooDaoCustom.class})
	@EnableJdbcRepositories(basePackageClasses={
			FooDao.class
	})
	static class EnableJdbcRepositoriesTestAppConfig {
		@Bean
		InitializeTables initializeTables() {
			return new InitializeTables();
		}
	}
}
