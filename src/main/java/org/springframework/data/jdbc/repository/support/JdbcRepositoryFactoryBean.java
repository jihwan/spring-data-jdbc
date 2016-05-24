package org.springframework.data.jdbc.repository.support;

import java.io.Serializable;

import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.ListableBeanFactory;
import org.springframework.data.jdbc.mapping.JdbcMappingContext;
import org.springframework.data.jdbc.repository.sql.SqlGenerator;
import org.springframework.data.jdbc.repository.sql.SqlGeneratorResolver;
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
	
	private JdbcTemplate jdbcTemplate;
	
	public void setJdbcTemplate(String jdbcTemplateName) {
		this.jdbcTemplateName = jdbcTemplateName;
	}
	
	@Override
	public void setMappingContext(MappingContext<?, ?> mappingContext) {
		super.setMappingContext(mappingContext);
		this.jdbcMappingContext = JdbcMappingContext.class.cast(mappingContext);
	}
	
	@Override
	protected RepositoryFactorySupport doCreateRepositoryFactory() {
		return createRepositoryFactory(jdbcTemplate);
	}
	
	protected RepositoryFactorySupport createRepositoryFactory(JdbcTemplate jdbcTemplate) {

		SqlGenerator sqlGenerator = createSqlGenerator(jdbcTemplate);
		return new JdbcRepositoryFactory(jdbcTemplate, jdbcMappingContext, sqlGenerator);
	}
	
	protected SqlGenerator createSqlGenerator(JdbcTemplate jdbcTemplate) {
		
		SqlGenerator resolveSqlGenerator = 
				new SqlGeneratorResolver().resolveSqlGenerator(jdbcTemplate.getDataSource());
		return resolveSqlGenerator;
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
		super.afterPropertiesSet();
	}
}
