package org.springframework.data.jdbc.repository.query;

import java.lang.reflect.Method;

import org.springframework.data.jdbc.mapping.JdbcMappingContext;
import org.springframework.data.projection.ProjectionFactory;
import org.springframework.data.repository.core.NamedQueries;
import org.springframework.data.repository.core.RepositoryMetadata;
import org.springframework.data.repository.query.EvaluationContextProvider;
import org.springframework.data.repository.query.QueryLookupStrategy;
import org.springframework.data.repository.query.RepositoryQuery;
import org.springframework.data.repository.query.QueryLookupStrategy.Key;
import org.springframework.jdbc.core.JdbcOperations;

public final class JdbcQueryLookupStrategy {
	
	private JdbcQueryLookupStrategy () {}

	public static QueryLookupStrategy create(JdbcOperations jdbcOperations, JdbcMappingContext jdbcMappingContext,
			Key key, EvaluationContextProvider evaluationContextProvider) {
		
		switch (key != null ? key : Key.CREATE_IF_NOT_FOUND) {
			case CREATE:
				return new CreateQueryLookupStrategy(jdbcOperations, jdbcMappingContext);
			case USE_DECLARED_QUERY:
				return new DeclaredQueryLookupStrategy(jdbcOperations, jdbcMappingContext);
			case CREATE_IF_NOT_FOUND:
				return new CreateIfNotFoundQueryLookupStrategy(jdbcOperations, jdbcMappingContext);
			default:
				throw new IllegalArgumentException(String.format("Unsupported query lookup strategy %s!", key));
		}
	}
	
	private abstract static class AbstractQueryLookupStrategy implements QueryLookupStrategy {
		
		JdbcOperations jdbcOperations;
		JdbcMappingContext jdbcMapping;
		
		public AbstractQueryLookupStrategy(JdbcOperations jdbcOperations, JdbcMappingContext jdbcMapping) {
			this.jdbcOperations = jdbcOperations;
			this.jdbcMapping = jdbcMapping;
		}
		
		@Override
		public RepositoryQuery resolveQuery(Method method, RepositoryMetadata metadata, ProjectionFactory factory,
				NamedQueries namedQueries) {
			JdbcQueryMethod queryMethod = new JdbcQueryMethod(method, metadata, factory);
			return doResolveQuery(queryMethod, namedQueries, jdbcOperations, jdbcMapping);
		}
		
		public abstract RepositoryQuery doResolveQuery(
				JdbcQueryMethod queryMethod, NamedQueries namedQueries,
				JdbcOperations jdbcOperations, JdbcMappingContext jdbcMapping);
	}

	private static class CreateQueryLookupStrategy extends AbstractQueryLookupStrategy {
		public CreateQueryLookupStrategy(JdbcOperations jdbcOperations, JdbcMappingContext jdbcMapping) {
			super(jdbcOperations, jdbcMapping);
		}

		@Override
		public RepositoryQuery doResolveQuery(JdbcQueryMethod queryMethod, NamedQueries namedQueries,
				JdbcOperations jdbcOperations, JdbcMappingContext jdbcMapping) {
			return new DummyJdbcQuery(queryMethod);
		}
	}
	
	private static class DeclaredQueryLookupStrategy extends AbstractQueryLookupStrategy {
		public DeclaredQueryLookupStrategy(JdbcOperations jdbcOperations, JdbcMappingContext jdbcMapping) {
			super(jdbcOperations, jdbcMapping);
		}
		
		@Override
		public RepositoryQuery doResolveQuery(JdbcQueryMethod queryMethod, NamedQueries namedQueries,
				JdbcOperations jdbcOperations, JdbcMappingContext jdbcMapping) {
			return new DummyJdbcQuery(queryMethod);
		}
	}
	
	private static class CreateIfNotFoundQueryLookupStrategy extends AbstractQueryLookupStrategy {
		public CreateIfNotFoundQueryLookupStrategy(JdbcOperations jdbcOperations, JdbcMappingContext jdbcMapping) {
			super(jdbcOperations, jdbcMapping);
		}
		
		@Override
		public RepositoryQuery doResolveQuery(JdbcQueryMethod queryMethod, NamedQueries namedQueries,
				JdbcOperations jdbcOperations, JdbcMappingContext jdbcMapping) {
			return new DummyJdbcQuery(queryMethod);
		}
	}

}
