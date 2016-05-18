package org.springframework.data.jdbc.repository.config;

import static org.junit.Assert.assertNotNull;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.h2.H2ConsoleAutoConfiguration;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.support.GenericApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import testconfig.H2JavaConfig;
import util.BeanDefinitionUtils;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes={EnableJdbcRepositoriesTest.EnableJdbcRepositoriesTestAppConfig.class})
public class H2ConsoleAutoConfigurationTest {

	@Autowired
	GenericApplicationContext context;
	
	@Test
	public void test() {
		assertNotNull(context);
		BeanDefinitionUtils.printBeanDefinitions(context);
	}
	
	@Configuration
	@Import({H2JavaConfig.class, H2ConsoleAutoConfiguration.class})
	static class EnableJdbcRepositoriesTestAppConfig {
	}
}
