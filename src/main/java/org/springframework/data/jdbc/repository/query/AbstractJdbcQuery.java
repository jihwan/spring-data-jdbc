package org.springframework.data.jdbc.repository.query;

import org.springframework.data.jdbc.mapping.JdbcMappingContext;
import org.springframework.data.jdbc.repository.query.JdbcQueryExecution.CollectionExecution;
import org.springframework.data.jdbc.repository.query.JdbcQueryExecution.SingleEntityExecution;
import org.springframework.data.repository.query.ParametersParameterAccessor;
import org.springframework.data.repository.query.QueryMethod;
import org.springframework.data.repository.query.RepositoryQuery;
import org.springframework.data.repository.query.ResultProcessor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.util.Assert;

/**
 * 
 * RowMapper, RowUnMapper, JdbcDialect 등을 인자로 받아야 하겠다!!!
 * 
 * @author Jihwan Hwang
 *
 */
public abstract class AbstractJdbcQuery implements RepositoryQuery {

	protected final JdbcQueryMethod method;
	protected final JdbcTemplate jdbcTemplate;
	protected final JdbcMappingContext jdbcMapping;
	
	public AbstractJdbcQuery(JdbcQueryMethod method, JdbcTemplate jdbcTemplate, JdbcMappingContext jdbcMapping) {
		Assert.notNull(method);
		this.method = method;
		this.jdbcTemplate = jdbcTemplate;
		this.jdbcMapping = jdbcMapping;
	}
	
	@Override
	public Object execute(Object[] parameters) {
		return doExecute(getExecution(), parameters);
	}
	
	private Object doExecute(JdbcQueryExecution execution, Object[] values) {

		ParametersParameterAccessor accessor = new ParametersParameterAccessor(method.getParameters(), values);
		ResultProcessor processor = method.getResultProcessor().withDynamicProjection(accessor);
		
		Object result = execution.execute(this, values, processor.getReturnedType().getDomainType());
		return result;
	}

	protected abstract Query createQuery();

	private JdbcQueryExecution getExecution() {
		
		if (method.isStreamQuery()) {
			throw new UnsupportedOperationException("method.isStreamQuery() is not support");
//			return new StreamExecution();
		} else if (method.isProcedureQuery()) {
			throw new UnsupportedOperationException("method.isProcedureQuery() is not support");
//			return new ProcedureExecution();
		} else if (method.isCollectionQuery()) {
			return new CollectionExecution();
		} else if (method.isSliceQuery()) {
			throw new UnsupportedOperationException("method.isSliceQuery() is not support");
//			return new SlicedExecution(method.getParameters());
		} else if (method.isPageQuery()) {
			throw new UnsupportedOperationException("method.isPageQuery() is not support");
//			return new PagedExecution(method.getParameters());
		} else if (method.isModifyingQuery()) {
			throw new UnsupportedOperationException("method.isModifyingQuery() is not support");
//			return method.getClearAutomatically() ? new ModifyingExecution(method, em) : new ModifyingExecution(method, null);
		} else {
			return new SingleEntityExecution();
		}
	}
	
	@Override
	public QueryMethod getQueryMethod() {
		return this.method;
	}
}
