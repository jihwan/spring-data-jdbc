package org.springframework.data.jdbc.repository.config;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.Entity;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.AbstractFactoryBean;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.ResourceLoaderAware;
import org.springframework.context.annotation.ClassPathScanningCandidateComponentProvider;
import org.springframework.context.support.GenericApplicationContext;
import org.springframework.core.io.ResourceLoader;
import org.springframework.core.type.StandardAnnotationMetadata;
import org.springframework.core.type.filter.AnnotationTypeFilter;
import org.springframework.data.jdbc.mapping.JdbcMappingContext;
import org.springframework.data.repository.config.AnnotationRepositoryConfigurationSource;
import org.springframework.util.ClassUtils;
import org.springframework.util.StringUtils;

class JdbcMappingContextFactoryBean extends AbstractFactoryBean<JdbcMappingContext> implements ApplicationContextAware, ResourceLoaderAware {

	GenericApplicationContext applicationContext;
	
	ResourceLoader resourceLoader;
	
	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		this.applicationContext = (GenericApplicationContext)applicationContext;
	}
	
	@Override
	public void setResourceLoader(ResourceLoader resourceLoader) {
		this.resourceLoader = resourceLoader;
	}

	@Override
	public Class<?> getObjectType() {
		return JdbcMappingContext.class;
	}

	@Override
	protected JdbcMappingContext createInstance() throws Exception {
		
		JdbcMappingContext jdbcMappingContext = new JdbcMappingContext();
		Set<Class<?>> initialEntitySet = getInitialEntitySet();
		jdbcMappingContext.setInitialEntitySet(initialEntitySet);
		jdbcMappingContext.initialize();
		
		return jdbcMappingContext;
	}
	
	private Set<Class<?>> getInitialEntitySet() throws ClassNotFoundException {
		
		Set<Class<?>> entitySet = new HashSet<Class<?>>();
		ClassPathScanningCandidateComponentProvider scanner = new ClassPathScanningCandidateComponentProvider(
				false);
		scanner.setEnvironment(applicationContext.getEnvironment());
		scanner.setResourceLoader(this.resourceLoader);
		scanner.addIncludeFilter(new AnnotationTypeFilter(Entity.class));
//		scanner.addIncludeFilter(new AnnotationTypeFilter(Persistent.class));
		for (String basePackage : getBasePackages()) {
			if (StringUtils.hasText(basePackage)) {
				for (BeanDefinition candidate : scanner
						.findCandidateComponents(basePackage)) {
					entitySet.add(ClassUtils.forName(candidate.getBeanClassName(),
							applicationContext.getClassLoader()));
				}
			}
		}
		return entitySet;
	}
	
	public Iterable<String> getBasePackages() {

		BeanDefinition beanDefinition = applicationContext.getBeanDefinition("jdbcMappingContext");
		Object source = beanDefinition.getSource();
		StandardAnnotationMetadata metadata = (StandardAnnotationMetadata)source;
		
		AnnotationRepositoryConfigurationSource annotationRepositoryConfigurationSource = 
				new AnnotationRepositoryConfigurationSource(metadata, EnableJdbcRepositories.class, resourceLoader, applicationContext.getEnvironment());
		
		return annotationRepositoryConfigurationSource.getBasePackages();
	}
}
