package info.zhwan.data.jdbc.repository.config;

import static org.junit.Assert.assertNotNull;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.support.GenericApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import study.domain.FactoryDao;
import testconfig.H2JavaConfig;
import util.BeanDefinitionUtils;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes={EnableJdbcRepositoriesTest.EnableJdbcRepositoriesTestAppConfig.class})
public class EnableJdbcRepositoriesTest {

	@Autowired
	GenericApplicationContext context;
	
	@Test
	public void test() {
		assertNotNull(context);
		BeanDefinitionUtils.printBeanDefinitions(context);
	}
	
	
	@Configuration
	@Import(H2JavaConfig.class)
	@EnableJdbcRepositories(basePackageClasses={
			FactoryDao.class
	})
	static class EnableJdbcRepositoriesTestAppConfig {
	}
}
