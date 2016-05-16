package org.springframework.data.jdbc.repository.support;

import java.io.Serializable;
import java.lang.reflect.Method;

import org.springframework.data.jdbc.domain.JdbcPersistable;
import org.springframework.data.jdbc.mapping.JdbcMappingContext;
import org.springframework.data.jdbc.mapping.JdbcPersistentEntityImpl;
import org.springframework.data.jdbc.repository.query.JdbcQueryMethod;
import org.springframework.data.jdbc.repository.query.StringBasedJdbcQuery;
import org.springframework.data.projection.ProjectionFactory;
import org.springframework.data.repository.core.NamedQueries;
import org.springframework.data.repository.core.RepositoryInformation;
import org.springframework.data.repository.core.RepositoryMetadata;
import org.springframework.data.repository.core.support.RepositoryFactorySupport;
import org.springframework.data.repository.query.EvaluationContextProvider;
import org.springframework.data.repository.query.QueryLookupStrategy;
import org.springframework.data.repository.query.RepositoryQuery;
import org.springframework.data.repository.query.QueryLookupStrategy.Key;
import org.springframework.jdbc.core.JdbcOperations;
import org.springframework.util.Assert;

public class JdbcRepositoryFactory extends RepositoryFactorySupport {

	private final JdbcOperations jdbcOperations;
	private final JdbcMappingContext jdbcMappingContext;
//	private final MappingContext<? extends JdbcPersistentEntity<?>, JdbcPersistentProperty> mappingContext;
	
	public JdbcRepositoryFactory(JdbcOperations jdbcOperations, JdbcMappingContext jdbcMappingContext) {
		Assert.notNull(jdbcOperations);
		Assert.notNull(jdbcMappingContext);
		
		this.jdbcOperations = jdbcOperations;
		this.jdbcMappingContext = jdbcMappingContext;
	}


	@Override
	protected Object getTargetRepository(RepositoryInformation information) {
		
		SimpleJdbcRepository<?, ?> repository = getTargetRepository(information, jdbcOperations);
//		repository.setRepositoryMethodMetadata(crudMethodMetadataPostProcessor.getCrudMethodMetadata());

		return repository;
	}

	private SimpleJdbcRepository<?, ?> getTargetRepository(RepositoryInformation information, JdbcOperations jdbcOperations) {
		
		JdbcEntityInformation<?, Serializable> entityInformation = getEntityInformation(information.getDomainType());

		return getTargetRepositoryViaReflection(information, entityInformation, jdbcOperations);
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public <T, ID extends Serializable> JdbcEntityInformation<T, ID> getEntityInformation(Class<T> domainClass) {

		if(JdbcPersistable.class.isAssignableFrom(domainClass)) {
			JdbcPersistentEntityImpl<?> entity = jdbcMappingContext.getPersistentEntity(domainClass);
			return new JdbcPersistableEntityInformation(entity);
		} else {
			throw new IllegalStateException(domainClass + " must implementation JdbcPersistable interface");
		}
		
	}
	
	
	@Override
	protected QueryLookupStrategy getQueryLookupStrategy(Key key, EvaluationContextProvider evaluationContextProvider) {
		return new QueryLookupStrategy() {
			@Override
			public RepositoryQuery resolveQuery(Method method, RepositoryMetadata metadata, ProjectionFactory factory, NamedQueries namedQueries) {

				
				JdbcQueryMethod queryMethod = new JdbcQueryMethod(method, metadata, factory);
				String namedQueryName = queryMethod.getNamedQueryName();
				
				if (namedQueries.hasQuery(namedQueryName)) {
					
					namedQueries.getQuery(namedQueryName);
					
				}
				else {
					
				}
				
				System.out.println(method);
				System.out.println(metadata);
				System.out.println(factory);
				System.out.println(namedQueries);
				
				return new StringBasedJdbcQuery();
			}
		};
	}

	@Override
	protected Class<?> getRepositoryBaseClass(RepositoryMetadata metadata) {
		return SimpleJdbcRepository.class;
	}
}
