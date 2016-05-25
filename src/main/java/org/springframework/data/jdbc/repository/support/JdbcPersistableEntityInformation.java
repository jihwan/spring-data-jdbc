package org.springframework.data.jdbc.repository.support;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.springframework.core.convert.support.GenericConversionService;
import org.springframework.data.domain.Persistable;
import org.springframework.data.jdbc.mapping.JdbcMappingContext;
import org.springframework.data.jdbc.mapping.JdbcPersistentEntity;
import org.springframework.data.jdbc.mapping.JdbcPersistentEntityImpl;
import org.springframework.data.jdbc.mapping.JdbcPersistentProperty;
import org.springframework.data.mapping.Association;
import org.springframework.data.mapping.AssociationHandler;
import org.springframework.data.mapping.PersistentPropertyAccessor;
import org.springframework.data.mapping.PropertyHandler;
import org.springframework.data.mapping.model.ConvertingPropertyAccessor;
import org.springframework.data.repository.core.support.PersistentEntityInformation;
import org.springframework.util.Assert;

public class JdbcPersistableEntityInformation<T extends Persistable<ID>, ID extends Serializable> 
	extends PersistentEntityInformation<T, ID>
	implements JdbcEntityInformation<T, ID> {
	
	private final JdbcPersistentEntity<T> persistentEntity;
	private final JdbcMappingContext jdbcMappingContext;
	private final MetaExtractor metaExtractor;
	
	private final GenericConversionService conversionService = new GenericConversionService();

	public JdbcPersistableEntityInformation(JdbcPersistentEntity<T> entity, JdbcMappingContext jdbcMappingContext) {
		super(entity);
		this.persistentEntity = entity;
		this.jdbcMappingContext = jdbcMappingContext;
		
		this.metaExtractor = new MetaExtractor();
		
		
//		persistentEntity.getIdentifierAccessor(entity).
	}

	@Override
	public boolean isNew(T entity) {
		return entity.isNew();
	}
	
	@Override
	public String getEntityName() {
		return getJavaType().getSimpleName();
	}

	@Override
	public List<String> getIdAttributeNames() {
		return this.metaExtractor.getIdAttributeNames();
	}
	
	@Override
	public List<Object> getCompositeIdAttributeValue(final Serializable id) {
		
		JdbcPersistentEntity<?> idEntity = getPersistentEntity(id.getClass());
		
		final List<Object> ids = new ArrayList<Object>();
		
		final PersistentPropertyAccessor accessor = 
				new ConvertingPropertyAccessor(idEntity.getPropertyAccessor(id), conversionService);
		
		idEntity.doWithProperties(new PropertyHandler<JdbcPersistentProperty>() {
			@Override
			public void doWithPersistentProperty(JdbcPersistentProperty persistentProperty) {
				Object value = accessor.getProperty(persistentProperty);
				ids.add(value);
			}
		});
		
		return ids;
	}
	
	@Override
	public void setIdAttributeValue(final T entity, final Serializable id) {
		
		Assert.notNull(entity);
		Assert.notNull(id);
		
		PersistentPropertyAccessor accessor = 
				new ConvertingPropertyAccessor(this.persistentEntity.getPropertyAccessor(entity), conversionService);
		
		accessor.setProperty(this.persistentEntity.getIdProperty(), id);
	}
	
	@Override
	public Map<String, Object> removeIdAttribute(final Map<String, ?> entityMap) {
		
		Map<String, Object> retMap = new HashMap<String, Object>(entityMap);
		
		for (Iterator<String> iterator = retMap.keySet().iterator(); iterator.hasNext();) {
			String key = iterator.next();
			if (getIdAttributeNames().contains(key)) {
				iterator.remove();
			}
		}
		
		return retMap;
	}
	
	
	@Override
	public JdbcPersistentProperty getPropertyByFieldName(final String fieldName) {
		return this.metaExtractor.getMetaInfo().get(fieldName);
	}
	
	@Override
	public JdbcPersistentEntity<T> getRootPersistentEntity() {
		return persistentEntity;
	}
	
	@Override
	public JdbcPersistentEntity<?> getPersistentEntity(Class<?> clazz) {
		return jdbcMappingContext.getPersistentEntity(clazz);
	}
	
	
	class MetaExtractor {
		
		/**
		 * key is a Entity class real Field names.
		 */
		final Map<String, JdbcPersistentProperty> map = new HashMap<String, JdbcPersistentProperty>();
		
		// id fields
		final List<String> id = new ArrayList<String>();
		
		MetaExtractor() {
			execute();
		}
		
		List<String> getIdAttributeNames() {
			return this.id;
		}
		
		Map<String, JdbcPersistentProperty> getMetaInfo() {
			return this.map;
		}

		private void execute() {
			extractId(persistentEntity);
			extract(persistentEntity);
		}
		
		private void extractId(final JdbcPersistentEntity<?> e) {
			JdbcPersistentProperty idProperty = e.getIdProperty();
			
			if (idProperty.isAssociation()) {
				
				Class<?> actualType = idProperty.getActualType();
				JdbcPersistentEntityImpl<?> idEntity = jdbcMappingContext.getPersistentEntity(actualType);
				idEntity.doWithProperties(new PropertyHandler<JdbcPersistentProperty>() {
					@Override
					public void doWithPersistentProperty(JdbcPersistentProperty persistentProperty) {
						id.add(persistentProperty.getField().getName());
					}
				});
			}
			else {
				id.add(idProperty.getField().getName());
			}
		}
		
		private void extract(final JdbcPersistentEntity<?> e) {
			
			e.doWithProperties(new PropertyHandler<JdbcPersistentProperty>() {
				@Override
				public void doWithPersistentProperty(JdbcPersistentProperty persistentProperty) {
					map.put(persistentProperty.getField().getName().toLowerCase(), persistentProperty);
				}
			});
			
			e.doWithAssociations(new AssociationHandler<JdbcPersistentProperty>() {
				@Override
				public void doWithAssociation(Association<JdbcPersistentProperty> association) {
					JdbcPersistentProperty inverse = association.getInverse();
					JdbcPersistentEntity<?> acturalEntity = jdbcMappingContext.getPersistentEntity(inverse.getActualType());
					extract(acturalEntity);
				}
			});
		}
	}
}
