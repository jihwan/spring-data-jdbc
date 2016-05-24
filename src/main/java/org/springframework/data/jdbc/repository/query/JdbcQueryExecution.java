package org.springframework.data.jdbc.repository.query;

import java.io.Serializable;

import org.springframework.data.jdbc.repository.support.JdbcBeanPropertyMapper;
import org.springframework.data.jdbc.repository.support.JdbcEntityInformation;
import org.springframework.jdbc.core.JdbcOperations;
import org.springframework.util.Assert;

interface JdbcQueryExecution {
	
	final String QUERY_MUST_HAVE_STRING = " @Query annotation value must have query string.";
	
	Object execute(AbstractJdbcQuery query, Object[] values, Class<?> domainClass);

	static class CollectionExecution implements JdbcQueryExecution {
		@Override
		public Object execute(AbstractJdbcQuery query, Object[] values, Class<?> domainClass) {
			
			String sql = query.createQuery().getSql();
			Assert.hasText(sql, query.method.getOwnerClass() + "#" + query.method.getMethodName() + QUERY_MUST_HAVE_STRING);
			
			JdbcOperations jdbcOperations = query.jdbcTemplate;
			JdbcEntityInformation<?, Serializable> information = query.jdbcMapping.getEntityInformation(domainClass);
			
			return jdbcOperations.query(sql, values, JdbcBeanPropertyMapper.newInstance(information));
		}
	}
	
	static class SingleEntityExecution implements JdbcQueryExecution {
		@Override
		public Object execute(AbstractJdbcQuery query, Object[] values, Class<?> domainClass) {
			
			String sql = query.createQuery().getSql();
			Assert.hasText(sql, query.method.getOwnerClass() + "#" + query.method.getMethodName() + QUERY_MUST_HAVE_STRING);
			
			JdbcOperations jdbcOperations = query.jdbcTemplate;
			JdbcEntityInformation<?, Serializable> information = query.jdbcMapping.getEntityInformation(domainClass);
			
			return jdbcOperations.queryForObject(sql, values, 
					JdbcBeanPropertyMapper.newInstance(information));
		}
	}
	
	static class ModifyingExecution implements JdbcQueryExecution {
		@Override
		public Object execute(AbstractJdbcQuery query, Object[] values, Class<?> domainClass) {
			
			Class<?> returnType = query.method.getReturnType();

			boolean isVoid = void.class.equals(returnType) || Void.class.equals(returnType);
			boolean isInt = int.class.equals(returnType) || Integer.class.equals(returnType);

			Assert.isTrue(isInt || isVoid, "Modifying queries can only use void or int/Integer as return type!");
			
			String sql = query.createQuery().getSql();
			Assert.hasText(sql, query.method.getOwnerClass() + "#" + query.method.getMethodName() + QUERY_MUST_HAVE_STRING);
			
			JdbcOperations jdbcOperations = query.jdbcTemplate;
			
			if(values == null || values.length == 0) {
				return jdbcOperations.update(sql);
			}
			else {
				return jdbcOperations.update(sql, values);
			}
		}
	}
}
