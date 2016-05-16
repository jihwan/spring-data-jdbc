package org.springframework.data.jdbc.repository.support;

import java.io.Serializable;

import org.springframework.data.jdbc.domain.JdbcPersistable;
import org.springframework.data.jdbc.mapping.JdbcPersistentEntity;
import org.springframework.data.repository.core.support.PersistentEntityInformation;

public class JdbcPersistableEntityInformation<T extends JdbcPersistable<T, Serializable>, ID extends Serializable> 
	extends PersistentEntityInformation<T, ID>
	implements JdbcEntityInformation<T, ID> {
	
	JdbcPersistentEntity<T> entity;
	
	public JdbcPersistableEntityInformation(JdbcPersistentEntity<T> entity) {
		super(entity);
		this.entity = entity;
	}
	
	@Override
	public boolean isNew(T entity) {
		return entity.isNew();
	}

//	@Override
//	public SingularAttribute<? super T, ?> getIdAttribute() {
//		return null;
//	}
//
//	@Override
//	public boolean hasCompositeId() {
//		// TODO Auto-generated method stub
//		return false;
//	}
//
//	@Override
//	public Iterable<String> getIdAttributeNames() {
//		// TODO Auto-generated method stub
//		return null;
//	}
//
//	@Override
//	public Object getCompositeIdAttributeValue(Serializable id, String idAttribute) {
//		// TODO Auto-generated method stub
//		return null;
//	}
}
