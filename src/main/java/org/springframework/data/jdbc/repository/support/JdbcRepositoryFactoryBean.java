package org.springframework.data.jdbc.repository.support;

import java.io.Serializable;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jdbc.mapping.JdbcMappingContext;
import org.springframework.data.jdbc.repository.query.JdbcSqlDialect;
import org.springframework.data.mapping.context.MappingContext;
import org.springframework.data.repository.Repository;
import org.springframework.data.repository.core.support.RepositoryFactorySupport;
import org.springframework.data.repository.core.support.TransactionalRepositoryFactoryBeanSupport;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.util.Assert;

public class JdbcRepositoryFactoryBean<T extends Repository<S, ID>, S, ID extends Serializable> extends
		TransactionalRepositoryFactoryBeanSupport<T, S, ID> {
	
	JdbcTemplate jdbcTemplate;
	
	JdbcMappingContext jdbcMappingContext;
	
	Class<?> jdbcSqlDialect;
	
	@Autowired
	public void setJdbcOperations(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}
	
	@Override
	public void setMappingContext(MappingContext<?, ?> mappingContext) {
		super.setMappingContext(mappingContext);
		this.jdbcMappingContext = JdbcMappingContext.class.cast(mappingContext);
	}
	
	public void setJdbcSqlDialect(Class<?> jdbcSqlDialect) {
		this.jdbcSqlDialect = jdbcSqlDialect;
	}

	@Override
	protected RepositoryFactorySupport doCreateRepositoryFactory() {
		return createRepositoryFactory(jdbcTemplate);
	}

	/**
	 * Returns a {@link RepositoryFactorySupport}.
	 * 
	 * @param jdbcTemplate
	 * @return
	 */
	protected RepositoryFactorySupport createRepositoryFactory(JdbcTemplate jdbcTemplate) {
		
		JdbcSqlDialect jdbcSqlDialect = createJdbcSqlDialect();
		return new JdbcRepositoryFactory(jdbcTemplate, jdbcMappingContext, jdbcSqlDialect);
	}
	
	protected JdbcSqlDialect createJdbcSqlDialect() {
		return (JdbcSqlDialect) BeanUtils.instantiate(jdbcSqlDialect);
	}

	@Override
	public void afterPropertiesSet() {
		Assert.notNull(jdbcTemplate, "JdbcTemplate must not be null");
		super.afterPropertiesSet();
	}
}
