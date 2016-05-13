//package study.domain;
//
//import javax.persistence.EntityManagerFactory;
//import javax.persistence.metamodel.Metamodel;
//import javax.sql.DataSource;
//
//import org.junit.Test;
//import org.junit.runner.RunWith;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.autoconfigure.AutoConfigurationPackages;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.context.annotation.Import;
//import org.springframework.context.support.GenericApplicationContext;
//import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
//import org.springframework.orm.jpa.vendor.HibernateJpaDialect;
//import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
//import org.springframework.test.context.ContextConfiguration;
//import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
//
//import testconfig.H2JavaConfig;
//
//
//@RunWith(SpringJUnit4ClassRunner.class)
//@ContextConfiguration(classes={HibernateTest.AppConf.class})
//public class HibernateTest {
//	
//	@Autowired
//	GenericApplicationContext context;
//
//	@Autowired
//	DataSource dataSource;
//	
//	@Test
//	public void testName() throws Exception {
//		
//        LocalContainerEntityManagerFactoryBean entityManagerFactory = new LocalContainerEntityManagerFactoryBean();
//        entityManagerFactory.setDataSource(dataSource);
//        
//        HibernateJpaVendorAdapter hibernateJpaVendorAdapter = new HibernateJpaVendorAdapter();
//        hibernateJpaVendorAdapter.setShowSql(true);
//        entityManagerFactory.setJpaVendorAdapter(hibernateJpaVendorAdapter);
//        
////        entityManagerFactory.setJpaProperties();
//        entityManagerFactory.setPackagesToScan("study.domain");
//        entityManagerFactory.setJpaDialect(new HibernateJpaDialect());
//        entityManagerFactory.afterPropertiesSet();
//        
//        EntityManagerFactory em = entityManagerFactory.getObject();
//        Metamodel metamodel = em.getMetamodel();
//        
//        System.err.println( metamodel );
//
//	}
//	
//	@Configuration
//	@Import(H2JavaConfig.class)
//	static class AppConf {
//		@Bean
//		String a() {
//			return "a";
//		}
//	}
//}
