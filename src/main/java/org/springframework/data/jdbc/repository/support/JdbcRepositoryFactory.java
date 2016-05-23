package org.springframework.data.jdbc.repository.support;

import java.io.Serializable;

import org.springframework.data.jdbc.domain.JdbcPersistable;
import org.springframework.data.jdbc.mapping.JdbcMappingContext;
import org.springframework.data.jdbc.mapping.JdbcPersistentEntityImpl;
import org.springframework.data.jdbc.repository.query.JdbcQueryLookupStrategy;
import org.springframework.data.jdbc.repository.query.JdbcSqlDialect;
import org.springframework.data.repository.core.RepositoryInformation;
import org.springframework.data.repository.core.RepositoryMetadata;
import org.springframework.data.repository.core.support.RepositoryFactorySupport;
import org.springframework.data.repository.query.EvaluationContextProvider;
import org.springframework.data.repository.query.QueryLookupStrategy;
import org.springframework.data.repository.query.QueryLookupStrategy.Key;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.util.Assert;

public class JdbcRepositoryFactory extends RepositoryFactorySupport {

	private final JdbcTemplate jdbcTemplate;
	private final JdbcMappingContext jdbcMappingContext;
	private final JdbcSqlDialect jdbcSqlDialect;
	
	public JdbcRepositoryFactory(JdbcTemplate jdbcTemplate, JdbcMappingContext jdbcMappingContext, JdbcSqlDialect jdbcSqlDialect) {
		Assert.notNull(jdbcTemplate);
		Assert.notNull(jdbcMappingContext);
		this.jdbcTemplate = jdbcTemplate;
		this.jdbcMappingContext = jdbcMappingContext;
		this.jdbcSqlDialect = jdbcSqlDialect;
	}

	@Override
	protected Object getTargetRepository(RepositoryInformation information) {
		
		SimpleJdbcRepository<?, ?> repository = doGetTargetRepository(information);
//		repository.setRepositoryMethodMetadata(crudMethodMetadataPostProcessor.getCrudMethodMetadata());
		return repository;
	}

	private SimpleJdbcRepository<?, ?> doGetTargetRepository(RepositoryInformation information) {
		
		JdbcEntityInformation<?, Serializable> entityInformation = getEntityInformation(information.getDomainType());
		return getTargetRepositoryViaReflection(information, entityInformation, jdbcTemplate, jdbcSqlDialect);
//		return getTargetRepositoryViaReflection(information, entityInformation, jdbcMappingContext, jdbcTemplate);
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public <T, ID extends Serializable> JdbcEntityInformation<T, ID> getEntityInformation(Class<T> domainClass) {
		
		if(JdbcPersistable.class.isAssignableFrom(domainClass)) {
			JdbcPersistentEntityImpl<?> entity = jdbcMappingContext.getPersistentEntity(domainClass);
			JdbcEntityInformation information = new JdbcPersistableEntityInformation(entity, jdbcMappingContext);
			jdbcMappingContext.addEntityInformation(domainClass, information);
			return information;
//			return new JdbcPersistableEntityInformation(entity, jdbcMappingContext);
		} else {
			throw new IllegalStateException(domainClass + " must implementation JdbcPersistable interface");
		}
		
	}
	
	@Override
	protected QueryLookupStrategy getQueryLookupStrategy(Key key, EvaluationContextProvider evaluationContextProvider) {
		
		return JdbcQueryLookupStrategy.create(jdbcTemplate, jdbcMappingContext, key, evaluationContextProvider);
	}

	@Override
	protected Class<?> getRepositoryBaseClass(RepositoryMetadata metadata) {
		return SimpleJdbcRepository.class;
	}
}
