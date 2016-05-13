package org.springframework.data.jdbc.repository.support;

import java.io.Serializable;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jdbc.mapping.JdbcMappingContext;
import org.springframework.data.jdbc.mapping.JdbcPersistentEntity;
import org.springframework.data.jdbc.mapping.JdbcPersistentProperty;

//import javax.persistence.EntityManager;
//import javax.persistence.PersistenceContext;

import org.springframework.data.mapping.context.MappingContext;
import org.springframework.data.repository.Repository;
import org.springframework.data.repository.core.support.RepositoryFactorySupport;
import org.springframework.data.repository.core.support.TransactionalRepositoryFactoryBeanSupport;
//import org.springframework.util.Assert;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.util.Assert;

/**
 * Special adapter for Springs {@link org.springframework.beans.factory.FactoryBean} interface to allow easy setup of
 * repository factories via Spring configuration.
 * 
 * @author Oliver Gierke
 * @author Eberhard Wolff
 * @param <T> the type of the repository
 */
public class JdbcRepositoryFactoryBean<T extends Repository<S, ID>, S, ID extends Serializable> extends
		TransactionalRepositoryFactoryBeanSupport<T, S, ID> {
	
	JdbcTemplate jdbcTemplate;
	
//	MappingContext<? extends JdbcPersistentEntity<?>, JdbcPersistentProperty> mappingContext;
	JdbcMappingContext jdbcMappingContext;
	
	@Autowired
	public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}
	

//	private EntityManager entityManager;
//
//	/**
//	 * The {@link EntityManager} to be used.
//	 * 
//	 * @param entityManager the entityManager to set
//	 */
//	@PersistenceContext
//	public void setEntityManager(EntityManager entityManager) {
//		this.entityManager = entityManager;
//	}

	/* 
	 * (non-Javadoc)
	 * @see org.springframework.data.repository.core.support.RepositoryFactoryBeanSupport#setMappingContext(org.springframework.data.mapping.context.MappingContext)
	 */
	@Override
	public void setMappingContext(MappingContext<?, ?> mappingContext) {
		super.setMappingContext(mappingContext);
		
		this.jdbcMappingContext = JdbcMappingContext.class.cast(mappingContext);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.springframework.data.repository.support.
	 * TransactionalRepositoryFactoryBeanSupport#doCreateRepositoryFactory()
	 */
	@Override
	protected RepositoryFactorySupport doCreateRepositoryFactory() {
//		return createRepositoryFactory(entityManager);
		return createRepositoryFactory(jdbcTemplate);
	}

	/**
	 * Returns a {@link RepositoryFactorySupport}.
	 * 
	 * @param entityManager
	 * @return
	 */
	protected RepositoryFactorySupport createRepositoryFactory(JdbcTemplate jdbcTemplate) {
		return new JdbcRepositoryFactory(jdbcTemplate, jdbcMappingContext);
	}
//	/**
//	 * Returns a {@link RepositoryFactorySupport}.
//	 * 
//	 * @param entityManager
//	 * @return
//	 */
//	protected RepositoryFactorySupport createRepositoryFactory(EntityManager entityManager) {
//		return new JpaRepositoryFactory(entityManager);
//	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.springframework.beans.factory.InitializingBean#afterPropertiesSet()
	 */
	@Override
	public void afterPropertiesSet() {

		Assert.notNull(jdbcTemplate, "JdbcTemplate must not be null!");
		super.afterPropertiesSet();
//		Assert.notNull(entityManager, "EntityManager must not be null!");
//		super.afterPropertiesSet();
	}
}
