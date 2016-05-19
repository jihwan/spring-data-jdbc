package org.springframework.data.jdbc.repository.config;

import java.lang.annotation.Annotation;

import org.springframework.data.repository.config.RepositoryBeanDefinitionRegistrarSupport;
import org.springframework.data.repository.config.RepositoryConfigurationExtension;

/**
 * {@link ImportBeanDefinitionRegistrar} to enable {@link EnableJdbcRepositories} annotation.
 * 
 * @author Oliver Gierke
 */



/**
 * 
 * @author Jihwan Hwang
 *
 */
class JdbcRepositoriesRegistrar extends RepositoryBeanDefinitionRegistrarSupport {	
	/* 
	 * (non-Javadoc)
	 * @see org.springframework.data.repository.config.RepositoryBeanDefinitionRegistrarSupport#getAnnotation()
	 */
	@Override
	protected Class<? extends Annotation> getAnnotation() {
		return EnableJdbcRepositories.class;
	}

	/* 
	 * (non-Javadoc)
	 * @see org.springframework.data.repository.config.RepositoryBeanDefinitionRegistrarSupport#getExtension()
	 */
	@Override
	protected RepositoryConfigurationExtension getExtension() {
		return new JdbcRepositoryConfigExtension();
	}
}