package org.springframework.data.jdbc.repository.config;

import java.lang.annotation.Annotation;

import org.springframework.data.repository.config.RepositoryBeanDefinitionRegistrarSupport;
import org.springframework.data.repository.config.RepositoryConfigurationExtension;

/**
 * 
 * @author Jihwan Hwang
 *
 */
class JdbcRepositoriesRegistrar extends RepositoryBeanDefinitionRegistrarSupport {	
	
	@Override
	protected Class<? extends Annotation> getAnnotation() {
		return EnableJdbcRepositories.class;
	}

	@Override
	protected RepositoryConfigurationExtension getExtension() {
		return new JdbcRepositoryConfigExtension();
	}
}