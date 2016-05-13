package org.springframework.data.jdbc.repository.config;

//import java.lang.annotation.Annotation;
//import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Locale;

//import javax.persistence.Entity;
//import javax.persistence.MappedSuperclass;

import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.RootBeanDefinition;
import org.springframework.core.annotation.AnnotationAttributes;
import org.springframework.data.jdbc.repository.JdbcRepository;
import org.springframework.data.jdbc.repository.support.JdbcRepositoryFactoryBean;
import org.springframework.data.repository.config.AnnotationRepositoryConfigurationSource;
import org.springframework.data.repository.config.RepositoryConfigurationExtensionSupport;
import org.springframework.data.repository.config.RepositoryConfigurationSource;
import org.springframework.data.repository.config.XmlRepositoryConfigurationSource;
import org.springframework.util.StringUtils;

public class JdbcRepositoryConfigExtension extends RepositoryConfigurationExtensionSupport {

	// ================================= 주석 처리
//	private static final Class<?> PAB_POST_PROCESSOR = PersistenceAnnotationBeanPostProcessor.class;
	private static final String DEFAULT_TRANSACTION_MANAGER_BEAN_NAME = "transactionManager";
	private static final String ENABLE_DEFAULT_TRANSACTIONS_ATTRIBUTE = "enableDefaultTransactions";

	// ================================= 변경
	/* 
	 * (non-Javadoc)
	 * @see org.springframework.data.repository.config.RepositoryConfigurationExtensionSupport#getModuleName()
	 */
	@Override
	public String getModuleName() {
//		return "JPA";
		return "JDBC";
	}

	/* 
	 * (non-Javadoc)
	 * @see org.springframework.data.repository.config14.RepositoryConfigurationExtension#getRepositoryInterface()
	 */
	public String getRepositoryFactoryClassName() {
		return JdbcRepositoryFactoryBean.class.getName();
	}

	/* 
	 * (non-Javadoc)
	 * @see org.springframework.data.repository.config14.RepositoryConfigurationExtensionSupport#getModulePrefix()
	 */
	@Override
	protected String getModulePrefix() {
		return getModuleName().toLowerCase(Locale.US);
	}

	// ================================= 주석
//	/* 
//	 * (non-Javadoc)
//	 * @see org.springframework.data.repository.config.RepositoryConfigurationExtensionSupport#getIdentifyingAnnotations()
//	 */
//	@Override
//	protected Collection<Class<? extends Annotation>> getIdentifyingAnnotations() {
//		return Arrays.asList(Entity.class, MappedSuperclass.class);
//	}

	// ================================= 변경
	/* 
	 * (non-Javadoc)
	 * @see org.springframework.data.repository.config.RepositoryConfigurationExtensionSupport#getIdentifyingTypes()
	 */
	@Override
	protected Collection<Class<?>> getIdentifyingTypes() {
//		return Collections.<Class<?>> singleton(JpaRepository.class);
		return Collections.<Class<?>> singleton(JdbcRepository.class);
	}

	// ================================= 주석 처리
	/* 
	 * (non-Javadoc)
	 * @see org.springframework.data.repository.config.RepositoryConfigurationExtensionSupport#postProcess(org.springframework.beans.factory.support.BeanDefinitionBuilder, org.springframework.data.repository.config.RepositoryConfigurationSource)
	 */
	@Override
	public void postProcess(BeanDefinitionBuilder builder, RepositoryConfigurationSource source) {
		
		String transactionManagerRef = source.getAttribute("transactionManagerRef");
		builder.addPropertyValue("transactionManager",
				transactionManagerRef == null ? DEFAULT_TRANSACTION_MANAGER_BEAN_NAME : transactionManagerRef);
//		builder.addPropertyValue("entityManager", getEntityManagerBeanDefinitionFor(source, source.getSource()));
		
		
		builder.addPropertyReference("mappingContext", JDBC_MAPPING_CONTEXT_BEAN_NAME);
	}

	String JDBC_MAPPING_CONTEXT_BEAN_NAME = "jdbcMappingContext";
	
	/* 
	 * (non-Javadoc)
	 * @see org.springframework.data.repository.config.RepositoryConfigurationExtensionSupport#postProcess(org.springframework.beans.factory.support.BeanDefinitionBuilder, org.springframework.data.repository.config.AnnotationRepositoryConfigurationSource)
	 */
	@Override
	public void postProcess(BeanDefinitionBuilder builder, AnnotationRepositoryConfigurationSource config) {

		AnnotationAttributes attributes = config.getAttributes();

		builder.addPropertyValue(ENABLE_DEFAULT_TRANSACTIONS_ATTRIBUTE,
				attributes.getBoolean(ENABLE_DEFAULT_TRANSACTIONS_ATTRIBUTE));
	}

	/* 
	 * (non-Javadoc)
	 * @see org.springframework.data.repository.config.RepositoryConfigurationExtensionSupport#postProcess(org.springframework.beans.factory.support.BeanDefinitionBuilder, org.springframework.data.repository.config.XmlRepositoryConfigurationSource)
	 */
	@Override
	public void postProcess(BeanDefinitionBuilder builder, XmlRepositoryConfigurationSource config) {

		String enableDefaultTransactions = config.getAttribute(ENABLE_DEFAULT_TRANSACTIONS_ATTRIBUTE);

		if (StringUtils.hasText(enableDefaultTransactions)) {
			builder.addPropertyValue(ENABLE_DEFAULT_TRANSACTIONS_ATTRIBUTE, enableDefaultTransactions);
		}
	}

	@Override
	public void registerBeansForRoot(BeanDefinitionRegistry registry, RepositoryConfigurationSource config) {
		
		super.registerBeansForRoot(registry, config);
		Object source = config.getSource();
				
		registerIfNotAlreadyRegistered(new RootBeanDefinition(JdbcMappingContextFactoryBean.class), registry, JDBC_MAPPING_CONTEXT_BEAN_NAME, source);		
	}
	
	
	// ================================= 주석 처리
//	/* 
//	 * (non-Javadoc)
//	 * @see org.springframework.data.repository.config.RepositoryConfigurationExtensionSupport#registerBeansForRoot(org.springframework.beans.factory.support.BeanDefinitionRegistry, org.springframework.data.repository.config.RepositoryConfigurationSource)
//	 */
//	@Override
//	public void registerBeansForRoot(BeanDefinitionRegistry registry, RepositoryConfigurationSource config) {
//
//		super.registerBeansForRoot(registry, config);
//
//		Object source = config.getSource();
//
//		registerIfNotAlreadyRegistered(new RootBeanDefinition(EntityManagerBeanDefinitionRegistrarPostProcessor.class),
//				registry, EM_BEAN_DEFINITION_REGISTRAR_POST_PROCESSOR_BEAN_NAME, source);
//
//		registerIfNotAlreadyRegistered(new RootBeanDefinition(JpaMetamodelMappingContextFactoryBean.class), registry,
//				JPA_MAPPING_CONTEXT_BEAN_NAME, source);
//
//		registerIfNotAlreadyRegistered(new RootBeanDefinition(PAB_POST_PROCESSOR), registry,
//				AnnotationConfigUtils.PERSISTENCE_ANNOTATION_PROCESSOR_BEAN_NAME, source);
//
//		// Register bean definition for DefaultJpaContext
//
//		RootBeanDefinition contextDefinition = new RootBeanDefinition(DefaultJpaContext.class);
//		contextDefinition.setAutowireMode(AbstractBeanDefinition.AUTOWIRE_CONSTRUCTOR);
//
//		registerIfNotAlreadyRegistered(contextDefinition, registry, JPA_CONTEXT_BEAN_NAME, source);
//	}

//	/**
//	 * Creates an anonymous factory to extract the actual {@link javax.persistence.EntityManager} from the
//	 * {@link javax.persistence.EntityManagerFactory} bean name reference.
//	 * 
//	 * @param entityManagerFactoryBeanName
//	 * @param source
//	 * @return
//	 */
//	private static AbstractBeanDefinition getEntityManagerBeanDefinitionFor(RepositoryConfigurationSource config,
//			Object source) {
//
//		BeanDefinitionBuilder builder = BeanDefinitionBuilder
//				.rootBeanDefinition("org.springframework.orm.jpa.SharedEntityManagerCreator");
//		builder.setFactoryMethod("createSharedEntityManager");
//		builder.addConstructorArgReference(getEntityManagerBeanRef(config));
//
//		AbstractBeanDefinition bean = builder.getRawBeanDefinition();
//		bean.setSource(source);
//
//		return bean;
//	}
//
//	private static String getEntityManagerBeanRef(RepositoryConfigurationSource config) {
//
//		String entityManagerFactoryRef = config == null ? null : config.getAttribute("entityManagerFactoryRef");
//		return entityManagerFactoryRef == null ? "entityManagerFactory" : entityManagerFactoryRef;
//	}
}