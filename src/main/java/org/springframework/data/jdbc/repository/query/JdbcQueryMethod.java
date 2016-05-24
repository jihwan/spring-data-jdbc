package org.springframework.data.jdbc.repository.query;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

import org.springframework.core.annotation.AnnotatedElementUtils;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.data.jdbc.repository.Modifying;
import org.springframework.data.jdbc.repository.Query;
import org.springframework.data.projection.ProjectionFactory;
import org.springframework.data.repository.core.RepositoryMetadata;
import org.springframework.data.repository.query.QueryMethod;
import org.springframework.util.StringUtils;

public class JdbcQueryMethod extends QueryMethod {
	
	final Method method;
	public JdbcQueryMethod(Method method, RepositoryMetadata metadata, ProjectionFactory factory) {
		super(method, metadata, factory);
		this.method = method;
	}
	
	String getAnnotatedQuery() {

		String query = getAnnotationValue("value", String.class);
		return StringUtils.hasText(query) ? query : null;
	}
	
	private <T> T getAnnotationValue(String attribute, Class<T> type) {
		return getMergedOrDefaultAnnotationValue(attribute, Query.class, type);
	}
	
	private <T> T getMergedOrDefaultAnnotationValue(String attribute, Class<? extends Annotation> annotationType, Class<T> targetType) {

		Annotation annotation = AnnotatedElementUtils.findMergedAnnotation(method, annotationType);
		return targetType.cast(AnnotationUtils.getValue(annotation, attribute));
	}

	@Override
	public boolean isModifyingQuery() {
		return null != AnnotationUtils.findAnnotation(method, Modifying.class);
	}
	
	Class<?> getReturnType() {
		return method.getReturnType();
	}
	
	Class<?> getOwnerClass() {
		return method.getDeclaringClass();
	}
	
	String getMethodName() {
		return method.getName();
	}

//	public boolean isProcedureQuery() {
//		return false;
//	}
}
