package org.springframework.data.jdbc.repository.support;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.core.convert.support.GenericConversionService;
import org.springframework.data.jdbc.domain.JdbcPersistable;
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

public class JdbcPersistableEntityInformation<T extends JdbcPersistable<T, Serializable>, ID extends Serializable> 
	extends PersistentEntityInformation<T, ID>
	implements JdbcEntityInformation<T, ID> {
	
	final JdbcPersistentEntity<T> persistentEntity;
	final JdbcMappingContext jdbcMappingContext;
	final MetaExtractor metaExtractor;
	
	protected GenericConversionService conversionService = new GenericConversionService();

	public JdbcPersistableEntityInformation(JdbcPersistentEntity<T> entity, JdbcMappingContext jdbcMappingContext) {
		super(entity);
		this.persistentEntity = entity;
		this.jdbcMappingContext = jdbcMappingContext;
		
		this.metaExtractor = new MetaExtractor();
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
	public Map<String, JdbcPersistentProperty> getMetaInfo() {
		return this.metaExtractor.getMetaInfo();
	}
	
	@Override
	public JdbcPersistentEntity<T> getPersistentEntity() {
		return persistentEntity;
	}
	
	@Override
	public JdbcPersistentEntity<?> getPersistentEntity(Class<?> clazz) {
		return jdbcMappingContext.getPersistentEntity(clazz);
	}
	
	@Override
	public List<Object> getCompositeIdAttributeValue(Object id) {
		
		
		JdbcPersistentEntity<?> pe = getPersistentEntity(id.getClass());
		
		final List<Object> ids = new ArrayList<Object>();
		
		final PersistentPropertyAccessor accessor = 
				new ConvertingPropertyAccessor(pe.getPropertyAccessor(id), conversionService);
		
		pe.doWithProperties(new PropertyHandler<JdbcPersistentProperty>() {
			@Override
			public void doWithPersistentProperty(JdbcPersistentProperty persistentProperty) {
				Object value = accessor.getProperty(persistentProperty);
				ids.add(value);
			}
		});
		
		return ids;
	}
	
//	@Override
//	public Object[] getCompositeIdAttributeValue(Iterable<Serializable> ids) {
//		// TODO Auto-generated method stub
//		return null;
//	}
	
	class MetaExtractor {
		
		final Map<String, JdbcPersistentProperty> map = new HashMap<String, JdbcPersistentProperty>();
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
	
//	@Override
//	public JdbcPersistentEntity<T> getJdbcPersistentEntity() {
//		return this.persistentEntity;
//	}
	
//	@Override
//	public JdbcMappingContext getJdbcMappingContext() {
//		return this.jdbcMappingContext;
//	}
	
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
