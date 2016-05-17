package org.springframework.data.jdbc.repository.support;

import java.util.Map;

import org.apache.commons.beanutils.BeanUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.data.jdbc.domain.sample.Factory;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import testconfig.H2JavaConfig;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes={SimpleJdbcInsertTest.AppConfig.class})
public class SimpleJdbcInsertTest {
	
	@Autowired
	JdbcTemplate jdbcTemplate;

	@Test
	public void testInsert() throws Exception {
		
		Factory factory = new Factory("T");
		factory.setDescription("desc");
		factory.setFactoryType("on-line");
		
		
		SimpleJdbcInsert simpleJdbcInsert = 
				new SimpleJdbcInsert(jdbcTemplate)
				.withTableName("TAB")
				.usingColumns("name", "desc", "factoryType");
		
		Map<String, String> map = BeanUtils.describe(factory);
		System.err.println(map);
		
		simpleJdbcInsert.execute(map);
	}
	
	@Configuration
	@Import(H2JavaConfig.class)
	static class AppConfig {
		
	}
}
