package org.springframework.data.jdbc.repository.query;

import java.lang.reflect.Method;

import org.springframework.data.jdbc.mapping.JdbcMappingContext;
import org.springframework.data.projection.ProjectionFactory;
import org.springframework.data.repository.core.NamedQueries;
import org.springframework.data.repository.core.RepositoryMetadata;
import org.springframework.data.repository.query.EvaluationContextProvider;
import org.springframework.data.repository.query.QueryLookupStrategy;
import org.springframework.data.repository.query.QueryLookupStrategy.Key;
import org.springframework.data.repository.query.RepositoryQuery;
import org.springframework.jdbc.core.JdbcTemplate;

public final class JdbcQueryLookupStrategy {
	
	private JdbcQueryLookupStrategy () {}

	public static QueryLookupStrategy create(JdbcTemplate jdbcTemplate, JdbcMappingContext jdbcMappingContext,
			Key key, EvaluationContextProvider evaluationContextProvider) {
		
		switch (key != null ? key : Key.CREATE_IF_NOT_FOUND) {
			case CREATE:
				return new CreateQueryLookupStrategy(jdbcTemplate, jdbcMappingContext);
			case USE_DECLARED_QUERY:
				return new DeclaredQueryLookupStrategy(jdbcTemplate, jdbcMappingContext);
			case CREATE_IF_NOT_FOUND:
				return new CreateIfNotFoundQueryLookupStrategy(jdbcTemplate, jdbcMappingContext);
			default:
				throw new IllegalArgumentException(String.format("Unsupported query lookup strategy %s!", key));
		}
	}
	
	private abstract static class AbstractQueryLookupStrategy implements QueryLookupStrategy {
		
		JdbcTemplate jdbcTemplate;
		JdbcMappingContext jdbcMapping;
		
		public AbstractQueryLookupStrategy(JdbcTemplate jdbcTemplate, JdbcMappingContext jdbcMapping) {
			this.jdbcTemplate = jdbcTemplate;
			this.jdbcMapping = jdbcMapping;
		}
		
		@Override
		public RepositoryQuery resolveQuery(Method method, RepositoryMetadata metadata, ProjectionFactory factory,
				NamedQueries namedQueries) {
			JdbcQueryMethod queryMethod = new JdbcQueryMethod(method, metadata, factory);
			return doResolveQuery(queryMethod, namedQueries, jdbcTemplate, jdbcMapping);
		}
		
		public abstract RepositoryQuery doResolveQuery(
				JdbcQueryMethod queryMethod, NamedQueries namedQueries,
				JdbcTemplate jdbcTemplate, JdbcMappingContext jdbcMapping);
	}

	/**
	 * 이것을 만드는 것은 현상황에서 하기에는 시간과 노력이 많이 투자된다.
	 * 일단 쉽게 할 수 있는 것 부터 해 보자
	 * @author Jihwan Hwang
	 *
	 */
	private static class CreateQueryLookupStrategy extends AbstractQueryLookupStrategy {
		public CreateQueryLookupStrategy(JdbcTemplate jdbcTemplate, JdbcMappingContext jdbcMapping) {
			super(jdbcTemplate, jdbcMapping);
		}

		@Override
		public RepositoryQuery doResolveQuery(JdbcQueryMethod queryMethod, NamedQueries namedQueries,
				JdbcTemplate jdbcTemplate, JdbcMappingContext jdbcMapping) {
			throw new UnsupportedOperationException("now this function is not support.");
		}
	}
	
	private static class DeclaredQueryLookupStrategy extends AbstractQueryLookupStrategy {
		public DeclaredQueryLookupStrategy(JdbcTemplate jdbcTemplate, JdbcMappingContext jdbcMapping) {
			super(jdbcTemplate, jdbcMapping);
		}
		
		@Override
		public RepositoryQuery doResolveQuery(JdbcQueryMethod queryMethod, NamedQueries namedQueries,
				JdbcTemplate jdbcTemplate, JdbcMappingContext jdbcMapping) {
			return new SqlJdbcQuery(queryMethod, jdbcTemplate, jdbcMapping);
		}
	}
	
	private static class CreateIfNotFoundQueryLookupStrategy extends AbstractQueryLookupStrategy {
		
		public CreateIfNotFoundQueryLookupStrategy(JdbcTemplate jdbcTemplate, JdbcMappingContext jdbcMapping) {
			super(jdbcTemplate, jdbcMapping);
		}
		
		@Override
		public RepositoryQuery doResolveQuery(JdbcQueryMethod queryMethod, NamedQueries namedQueries,
				JdbcTemplate jdbcTemplate, JdbcMappingContext jdbcMapping) {
			return new SqlJdbcQuery(queryMethod, jdbcTemplate, jdbcMapping);
		}
	}

}
