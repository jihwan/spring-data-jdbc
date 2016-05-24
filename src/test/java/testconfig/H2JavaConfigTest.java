package testconfig;

import static org.junit.Assert.*;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.GenericApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.PlatformTransactionManager;

import util.BeanDefinitionUtils;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes={H2JavaConfig.class})
public class H2JavaConfigTest {
	
	@Autowired
	GenericApplicationContext context;
	
	@Test
	public void testApplicationContext() throws Exception {
		assertNotNull(context);
		assertNotNull(context.getBean(PlatformTransactionManager.class));
		
		BeanDefinitionUtils.printBeanDefinitions(context);
	}
}
