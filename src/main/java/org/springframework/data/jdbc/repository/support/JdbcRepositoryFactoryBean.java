package org.springframework.data.jdbc.repository.support;

import java.io.Serializable;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jdbc.mapping.JdbcMappingContext;

//import javax.persistence.EntityManager;
//import javax.persistence.PersistenceContext;

import org.springframework.data.mapping.context.MappingContext;
import org.springframework.data.repository.Repository;
import org.springframework.data.repository.core.support.RepositoryFactorySupport;
import org.springframework.data.repository.core.support.TransactionalRepositoryFactoryBeanSupport;
import org.springframework.jdbc.core.JdbcOperations;
import org.springframework.util.Assert;

public class JdbcRepositoryFactoryBean<T extends Repository<S, ID>, S, ID extends Serializable> extends
		TransactionalRepositoryFactoryBeanSupport<T, S, ID> {
	
	JdbcOperations jdbcOperations;
	
//	MappingContext<? extends JdbcPersistentEntity<?>, JdbcPersistentProperty> mappingContext;
	JdbcMappingContext jdbcMappingContext;
	
	@Autowired
	public void setJdbcOperations(JdbcOperations jdbcOperations) {
		this.jdbcOperations = jdbcOperations;
	}
	
	@Override
	public void setMappingContext(MappingContext<?, ?> mappingContext) {
		super.setMappingContext(mappingContext);
		
		this.jdbcMappingContext = JdbcMappingContext.class.cast(mappingContext);
	}

	@Override
	protected RepositoryFactorySupport doCreateRepositoryFactory() {
		return createRepositoryFactory(jdbcOperations);
	}

	/**
	 * Returns a {@link RepositoryFactorySupport}.
	 * 
	 * @param entityManager
	 * @return
	 */
	protected RepositoryFactorySupport createRepositoryFactory(JdbcOperations jdbcOperations) {
		return new JdbcRepositoryFactory(jdbcOperations, jdbcMappingContext);
	}

	@Override
	public void afterPropertiesSet() {

		Assert.notNull(jdbcOperations, "JdbcOperations must not be null!");
		super.afterPropertiesSet();
	}
}
