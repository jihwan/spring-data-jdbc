package org.springframework.data.jdbc.repository.query;

import java.lang.reflect.Method;

import org.springframework.data.projection.ProjectionFactory;
import org.springframework.data.repository.core.RepositoryMetadata;
import org.springframework.data.repository.query.QueryMethod;

public class JdbcQueryMethod extends QueryMethod {

	public JdbcQueryMethod(Method method, RepositoryMetadata metadata, ProjectionFactory factory) {
		super(method, metadata, factory);
		// TODO Auto-generated constructor stub
	}

}
