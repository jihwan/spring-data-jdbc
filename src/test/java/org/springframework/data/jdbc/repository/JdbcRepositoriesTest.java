package org.springframework.data.jdbc.repository;

import static org.junit.Assert.*;

import java.sql.SQLException;
import java.util.List;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.support.GenericApplicationContext;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.jdbc.domain.InitializeTables;
import org.springframework.data.jdbc.domain.sample.Address;
import org.springframework.data.jdbc.domain.sample.Bar;
import org.springframework.data.jdbc.domain.sample.Foo;
import org.springframework.data.jdbc.domain.sample.FooDao;
import org.springframework.data.jdbc.domain.sample2.FooDaoCustom;
import org.springframework.data.jdbc.domain.sample3.Zoo;
import org.springframework.data.jdbc.domain.sample3.ZooDao;
import org.springframework.data.jdbc.mapping.JdbcMappingContext;
import org.springframework.data.jdbc.repository.config.EnableJdbcRepositories;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.google.common.collect.Lists;

import testconfig.H2JavaConfig;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes={JdbcRepositoriesTest.EnableJdbcRepositoriesTestAppConfig.class})
public class JdbcRepositoriesTest {

	@Autowired
	GenericApplicationContext context;
	
	@Autowired
	JdbcMappingContext mappingContext;
	
	@Autowired
	FooDao fooDao;
	
	@Autowired
	ZooDao zooDao;
	
	Bar bar1;
	Foo foo1;
	
	Bar bar2;
	Foo foo2;
	
	Bar bar3;
	Foo foo3;
	
	Bar bar4;
	
	@Before
	public void before() throws SQLException {
		assertNotNull(context);
		assertNotNull(fooDao);
		assertNotNull(zooDao);
		
		Address address = new Address();
		address.setStreet("street1");
		
		bar1 = new Bar("a1", "b1");
		foo1 = new Foo(bar1);
		foo1.setName("foo1");
		foo1.setAddR(address);
		
		bar2 = new Bar("a2", "b2");
		foo2 = new Foo(bar2);
		foo2.setName("foo2");
		foo2.setAddR(address);
		
		bar3 = new Bar("a3", "b3");
		foo3 = new Foo(bar3);
		foo3.setName("foo3");
		foo3.setAddR(address);
		
		bar4 = new Bar("a4", "b4");
		
		fooDao.deleteAll();
		zooDao.deleteAll();
	}
	
	// auto generate key
	@Test
	public void testZoo() {
		
		Zoo zoo = new Zoo();
		zoo.setName("zoo_name");
		assertTrue(zoo.isNew());
		
		zooDao.save(zoo);
		assertNotNull(zoo.getId());
		assertFalse(zoo.isNew());
		
		Zoo findOne = zooDao.findOne(zoo.getId());
		assertNotNull(findOne);
		assertEquals(zoo, findOne);
		assertFalse(zoo.isNew());
		
		Zoo zoo2 = new Zoo();
		zoo2.setName("zoo2");
		assertTrue(zoo2.isNew());		
		zooDao.save(zoo2);
		
		System.err.println(zoo2);
		
//		zooDao.delete(findOne);
//		assertTrue(findOne.isNew());
	}
	
	// manual assign key
	@Ignore
	@Test
	public void testFoo() {
		
		// Foo, Bar, Address, Zoo
		assertEquals(4, mappingContext.getPersistentEntities().size());
		
		List<Foo> list = fooDao.save(Lists.newArrayList(foo1, foo2, foo3));
		for (Foo foo : list) {
			assertFalse(foo.isNew());
		}
		
		assertEquals(1, fooDao.updateByName("foo1_2", "a1", "b1"));
		
		Foo findOne = fooDao.findOne(bar1);
		assertFalse(findOne.isNew());
		assertTrue(findOne.getId().equals(bar1));
		
		assertEquals(1, fooDao.deleteByName("foo1_2"));
		
		assertEquals(0, fooDao.findByName("foo1_2").size());
		
		assertEquals(0, fooDao.deleteDummy());
		
		// sort test
		list = fooDao.findAll(new Sort(new Sort.Order(Direction.DESC, "name")));
		assertEquals(2, list.size());
		assertEquals("foo3", list.get(0).getName());
		assertEquals("foo2", list.get(1).getName());
		
		// page
		for (Foo myFoo : fooDao.findAll(new PageRequest(1, 1, new Sort(new Sort.Order(Direction.ASC, "name"))))) {
			System.err.println(myFoo + "\t" + myFoo.isNew());
		}
		
		Foo customMethod = fooDao.customMethod(foo1);
		assertTrue(customMethod.equals(foo1));
	}
	
	@Configuration
	@Import({H2JavaConfig.class})
	@ComponentScan(basePackageClasses={FooDaoCustom.class})
	@EnableJdbcRepositories(basePackageClasses={
			FooDao.class
	}, basePackages={
			"org.springframework.data.jdbc.domain.sample3"
	})
	static class EnableJdbcRepositoriesTestAppConfig {
		@Bean
		InitializeTables initializeTables() {
			return new InitializeTables();
		}
	}
}
