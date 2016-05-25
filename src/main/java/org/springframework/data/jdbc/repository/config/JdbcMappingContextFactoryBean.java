package org.springframework.data.jdbc.repository.config;

import java.util.HashSet;
import java.util.Set;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.AbstractFactoryBean;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.ResourceLoaderAware;
import org.springframework.context.annotation.ClassPathScanningCandidateComponentProvider;
import org.springframework.context.support.GenericApplicationContext;
import org.springframework.core.io.ResourceLoader;
import org.springframework.core.type.filter.AnnotationTypeFilter;
import org.springframework.core.type.filter.AssignableTypeFilter;
import org.springframework.data.annotation.Persistent;
import org.springframework.data.domain.Persistable;
import org.springframework.data.jdbc.domain.JdbcPersistable;
import org.springframework.data.jdbc.mapping.JdbcMappingContext;
import org.springframework.data.repository.config.RepositoryConfigurationSource;
import org.springframework.util.ClassUtils;
import org.springframework.util.StringUtils;

/**
 * {@link Persistable} or {@link JdbcPersistable}을 implementation 한 jdbc Domain entity class 목록을 찾아 등록 합니다.
 * entity class를 찾기 위해서는, {@link EnableJdbcRepositories#basePackageClasses()} 나, {@link EnableJdbcRepositories#basePackages()} 에 명시 해야 합니다.
 * 
 * @author Jihwan Hwang
 *
 */
class JdbcMappingContextFactoryBean 
	extends AbstractFactoryBean<JdbcMappingContext> 
	implements ApplicationContextAware, ResourceLoaderAware {
	
	/**
	 * {@link JdbcRepositoryConfigExtension#registerBeansForRoot(org.springframework.beans.factory.support.BeanDefinitionRegistry, RepositoryConfigurationSource)}
	 * 에서 bean property set을 한다.
	 * 
	 * @see JdbcRepositoryConfigExtension
	 * @see EnableJdbcRepositories
	 */
	private RepositoryConfigurationSource configurationSource;

	private GenericApplicationContext applicationContext;
	
	private ResourceLoader resourceLoader;
	
	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		this.applicationContext = (GenericApplicationContext)applicationContext;
	}
	
	@Override
	public void setResourceLoader(ResourceLoader resourceLoader) {
		this.resourceLoader = resourceLoader;
	}
	
	public void setConfigurationSource(RepositoryConfigurationSource configurationSource) {
		this.configurationSource = configurationSource;
	}
	
	@Override
	public Class<?> getObjectType() {
		return JdbcMappingContext.class;
	}

	@Override
	protected JdbcMappingContext createInstance() throws Exception {
		
		Set<Class<?>> initialEntitySet = getInitialEntitySet();
		
		JdbcMappingContext jdbcMappingContext = new JdbcMappingContext();
		jdbcMappingContext.setInitialEntitySet(initialEntitySet);
		jdbcMappingContext.initialize();
		
		return jdbcMappingContext;
	}
	
	/**
	 * Candidate Entity(Domain object) : {@link Persistent} or {@link JdbcPersistable}
	 * 
	 * @return
	 * @throws ClassNotFoundException
	 */
	private Set<Class<?>> getInitialEntitySet() throws ClassNotFoundException {
		
		Set<Class<?>> entitySet = new HashSet<Class<?>>();
		ClassPathScanningCandidateComponentProvider scanner = 
				new ClassPathScanningCandidateComponentProvider(false);
		scanner.setEnvironment(applicationContext.getEnvironment());
		scanner.setResourceLoader(this.resourceLoader);
		scanner.addIncludeFilter(new AssignableTypeFilter(Persistable.class));
		scanner.addIncludeFilter(new AssignableTypeFilter(JdbcPersistable.class));
		scanner.addIncludeFilter(new AnnotationTypeFilter(Persistent.class));
		for (String basePackage : getBasePackages()) {
			if (StringUtils.hasText(basePackage)) {
				for (BeanDefinition candidate : scanner.findCandidateComponents(basePackage)) {
					entitySet.add(
							ClassUtils.forName(candidate.getBeanClassName(), applicationContext.getClassLoader()));
				}
			}
		}
		return entitySet;
	}
	
	
	private Iterable<String> getBasePackages() {

		return configurationSource.getBasePackages();
	}
}
