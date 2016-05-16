package org.springframework.data.jdbc.repository.query;

import org.springframework.data.repository.query.ParametersParameterAccessor;
import org.springframework.data.repository.query.QueryMethod;
import org.springframework.data.repository.query.RepositoryQuery;
import org.springframework.data.repository.query.ResultProcessor;
import org.springframework.util.Assert;

/**
 * 
 * RowMapper, RowUnMapper, JdbcDialect 등을 인자로 받아야 하겠다!!!
 * 
 * @author zhwan
 *
 */
public class AbstractJdbcQuery implements RepositoryQuery {

	private final JdbcQueryMethod method;
	
	public AbstractJdbcQuery(JdbcQueryMethod method) {
		Assert.notNull(method);
		this.method = method;
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

		Object result = execution.execute(this, values);

		ParametersParameterAccessor accessor = new ParametersParameterAccessor(method.getParameters(), values);
		ResultProcessor withDynamicProjection = method.getResultProcessor().withDynamicProjection(accessor);

		return withDynamicProjection.processResult(result);
//		return withDynamicProjection. processResult(result, TupleConverter.INSTANCE);
	}

	private JdbcQueryExecution getExecution() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public QueryMethod getQueryMethod() {
		return this.method;
	}
}
