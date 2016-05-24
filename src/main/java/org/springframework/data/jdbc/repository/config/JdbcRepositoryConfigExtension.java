package org.springframework.data.jdbc.repository.config;

import java.lang.annotation.Annotation;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;

import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.RootBeanDefinition;
import org.springframework.core.annotation.AnnotationAttributes;
import org.springframework.data.annotation.Persistent;
import org.springframework.data.jdbc.repository.JdbcRepository;
import org.springframework.data.jdbc.repository.support.JdbcRepositoryFactoryBean;
import org.springframework.data.repository.config.AnnotationRepositoryConfigurationSource;
import org.springframework.data.repository.config.RepositoryConfigurationExtensionSupport;
import org.springframework.data.repository.config.RepositoryConfigurationSource;
import org.springframework.data.repository.config.XmlRepositoryConfigurationSource;
import org.springframework.util.StringUtils;

/**
 * 여기서 overriding 하는 함수의 기능은 {@link JdbcRepositoryFactoryBean} 을 setting 하기 위한,
 * bean 생성이나, 필요한 bean을 가져 오기 위한 역할을 한다.
 * 
 * @author Jihwan Hwang
 *
 */
public class JdbcRepositoryConfigExtension extends RepositoryConfigurationExtensionSupport {

	private static final String DEFAULT_TRANSACTION_MANAGER_BEAN_NAME = "transactionManager";
	private static final String DEFAULT_JDBC_TEMPLATE_BEAN_NAME = "jdbcTemplate";
	private static final String ENABLE_DEFAULT_TRANSACTIONS_ATTRIBUTE = "enableDefaultTransactions";

	static final String JDBC_MAPPING_CONTEXT_BEAN_NAME = "jdbcMappingContext";
	
	@Override
	public String getModuleName() {
		return "JDBC";
	}

	@Override
	public String getRepositoryFactoryClassName() {
		return JdbcRepositoryFactoryBean.class.getName();
	}

	@Override
	protected String getModulePrefix() {
		return getModuleName().toLowerCase();
	}

	@Override
	protected Collection<Class<? extends Annotation>> getIdentifyingAnnotations() {
		return Arrays.asList(Persistent.class);
	}

	@Override
	protected Collection<Class<?>> getIdentifyingTypes() {
		return Collections.<Class<?>> singleton(JdbcRepository.class);
	}

	@Override
	public void postProcess(BeanDefinitionBuilder builder, RepositoryConfigurationSource source) {
		
		String transactionManagerRef = source.getAttribute("transactionManagerRef");
		builder.addPropertyValue("transactionManager",
				transactionManagerRef == null ? DEFAULT_TRANSACTION_MANAGER_BEAN_NAME : transactionManagerRef);
		
		String jdbcTemplateRef = source.getAttribute("jdbcTemplateRef");
		builder.addPropertyValue("jdbcTemplate",
				jdbcTemplateRef == null ? DEFAULT_JDBC_TEMPLATE_BEAN_NAME : jdbcTemplateRef);
		
		builder.addPropertyReference("mappingContext", JDBC_MAPPING_CONTEXT_BEAN_NAME);
	}

	@Override
	public void postProcess(BeanDefinitionBuilder builder, AnnotationRepositoryConfigurationSource config) {

		AnnotationAttributes attributes = config.getAttributes();
		builder.addPropertyValue(ENABLE_DEFAULT_TRANSACTIONS_ATTRIBUTE,
				attributes.getBoolean(ENABLE_DEFAULT_TRANSACTIONS_ATTRIBUTE));
	}

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
		
		RootBeanDefinition rootBeanDefinition = new RootBeanDefinition(JdbcMappingContextFactoryBean.class);
		// use for : scanning custom entity class
		rootBeanDefinition.getPropertyValues().add("configurationSource", config);
		registerIfNotAlreadyRegistered(rootBeanDefinition, registry, JDBC_MAPPING_CONTEXT_BEAN_NAME, source);
	}
}