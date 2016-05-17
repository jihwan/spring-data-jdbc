package org.springframework.data.jdbc.repository.query;

import org.springframework.data.jdbc.repository.query.JdbcQueryExecution.CollectionExecution;
import org.springframework.data.jdbc.repository.query.JdbcQueryExecution.SingleEntityExecution;
import org.springframework.data.repository.query.ParametersParameterAccessor;
import org.springframework.data.repository.query.QueryMethod;
import org.springframework.data.repository.query.RepositoryQuery;
import org.springframework.data.repository.query.ResultProcessor;
import org.springframework.jdbc.core.JdbcOperations;
import org.springframework.util.Assert;

/**
 * 
 * RowMapper, RowUnMapper, JdbcDialect 등을 인자로 받아야 하겠다!!!
 * 
 * @author zhwan
 *
 */
public abstract class AbstractJdbcQuery implements RepositoryQuery {

	protected final JdbcQueryMethod method;
	protected final JdbcOperations jdbcOperations;
	
	public AbstractJdbcQuery(JdbcQueryMethod method, JdbcOperations jdbcOperations) {
		Assert.notNull(method);
		this.method = method;
		this.jdbcOperations = jdbcOperations;
	}
	
	@Override
	public Object execute(Object[] parameters) {
		return doExecute(getExecution(), parameters);
	}
	
	/**
	 * @param execution
	 * @param values
	 * @return
	 */
	private Object doExecute(JdbcQueryExecution execution, Object[] values) {

		ParametersParameterAccessor accessor = new ParametersParameterAccessor(method.getParameters(), values);
		ResultProcessor processor = method.getResultProcessor().withDynamicProjection(accessor);
		
		Object result = execution.execute(this, values, processor.getReturnedType().getDomainType());

		return result;
	}

	protected abstract Query createQuery();

	private JdbcQueryExecution getExecution() {
		
		if (method.isCollectionQuery()) {
			return new CollectionExecution();
		} else {
			return new SingleEntityExecution();
		}
		
//		if (method.isStreamQuery()) {
//			return new StreamExecution();
//		} else if (method.isProcedureQuery()) {
//			return new ProcedureExecution();
//		} else if (method.isCollectionQuery()) {
//			return new CollectionExecution();
//		} else if (method.isSliceQuery()) {
//			return new SlicedExecution(method.getParameters());
//		} else if (method.isPageQuery()) {
//			return new PagedExecution(method.getParameters());
//		} else if (method.isModifyingQuery()) {
//			return method.getClearAutomatically() ? new ModifyingExecution(method, em) : new ModifyingExecution(method, null);
//		} else {
//			return new SingleEntityExecution();
//		}
	}
	

	@Override
	public QueryMethod getQueryMethod() {
		return this.method;
	}
}
