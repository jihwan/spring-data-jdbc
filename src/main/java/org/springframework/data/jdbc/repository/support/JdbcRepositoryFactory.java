package org.springframework.data.jdbc.repository.support;

import java.io.Serializable;

import org.springframework.data.jdbc.mapping.JdbcMappingContext;
import org.springframework.data.repository.core.EntityInformation;
import org.springframework.data.repository.core.RepositoryInformation;
import org.springframework.data.repository.core.RepositoryMetadata;
import org.springframework.data.repository.core.support.ReflectionEntityInformation;
import org.springframework.data.repository.core.support.RepositoryFactorySupport;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.util.Assert;

public class JdbcRepositoryFactory extends RepositoryFactorySupport {

	private final JdbcTemplate jdbcTemplate;
	private final JdbcMappingContext jdbcMappingContext;
//	private final MappingContext<? extends JdbcPersistentEntity<?>, JdbcPersistentProperty> mappingContext;
	
	public JdbcRepositoryFactory(JdbcTemplate jdbcTemplate, JdbcMappingContext jdbcMappingContext) {
		Assert.notNull(jdbcTemplate);
		Assert.notNull(jdbcMappingContext);
		
		this.jdbcTemplate = jdbcTemplate;
		this.jdbcMappingContext = jdbcMappingContext;
	}

	@Override
	public <T, ID extends Serializable> EntityInformation<T, ID> getEntityInformation(Class<T> domainClass) {
//		return null;
		
		return new ReflectionEntityInformation<>(domainClass);
	}

	@Override
	protected Object getTargetRepository(RepositoryInformation metadata) {
		
		SimpleJdbcRepository<?, ?> repository = new SimpleJdbcRepository<>();
//		SimpleJpaRepository<?, ?> repository = getTargetRepository(information, entityManager);
//		repository.setRepositoryMethodMetadata(crudMethodMetadataPostProcessor.getCrudMethodMetadata());

		return repository;
	}

	@Override
	protected Class<?> getRepositoryBaseClass(RepositoryMetadata metadata) {
		return SimpleJdbcRepository.class;
	}
}
