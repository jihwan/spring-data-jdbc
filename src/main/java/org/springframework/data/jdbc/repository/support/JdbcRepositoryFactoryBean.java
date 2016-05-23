package org.springframework.data.jdbc.repository.support;

import java.io.Serializable;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.ListableBeanFactory;
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
	
	private JdbcMappingContext jdbcMappingContext;
	
	private String jdbcTemplateName;
	private Class<?> jdbcSqlDialectClazz;
	
	private JdbcTemplate jdbcTemplate;
	
	@Override
	public void setMappingContext(MappingContext<?, ?> mappingContext) {
		super.setMappingContext(mappingContext);
		this.jdbcMappingContext = JdbcMappingContext.class.cast(mappingContext);
	}
	
	public void setJdbcTemplate(String jdbcTemplateName) {
		this.jdbcTemplateName = jdbcTemplateName;
	}
	
	public void setJdbcSqlDialectClazz(Class<?> jdbcSqlDialect) {
		this.jdbcSqlDialectClazz = jdbcSqlDialect;
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
		return (JdbcSqlDialect) BeanUtils.instantiate(jdbcSqlDialectClazz);
	}
	
	@Override
	public void setBeanFactory(BeanFactory beanFactory) {
		super.setBeanFactory(beanFactory);
		
		ListableBeanFactory listableBeanFactory = (ListableBeanFactory) beanFactory;
		this.jdbcTemplate = listableBeanFactory.getBean(this.jdbcTemplateName, JdbcTemplate.class);
		Assert.notNull(jdbcTemplate);
	}

	@Override
	public void afterPropertiesSet() {
		Assert.hasText(jdbcTemplateName);
		Assert.notNull(jdbcSqlDialectClazz);
		super.afterPropertiesSet();
	}
}
