package org.springframework.data.jdbc.mapping;

import java.beans.PropertyDescriptor;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.Embedded;
import javax.persistence.EmbeddedId;
import javax.persistence.Id;
import javax.persistence.Transient;

import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.data.mapping.Association;
import org.springframework.data.mapping.PersistentEntity;
import org.springframework.data.mapping.model.AnnotationBasedPersistentProperty;
import org.springframework.data.mapping.model.SimpleTypeHolder;
import org.springframework.data.util.ClassTypeInformation;
import org.springframework.data.util.TypeInformation;

public class JdbcPersistentPropertyImpl extends AnnotationBasedPersistentProperty<JdbcPersistentProperty> implements JdbcPersistentProperty {

	private static final Collection<Class<? extends Annotation>> ASSOCIATION_ANNOTATIONS;
	private static final Collection<Class<? extends Annotation>> ID_ANNOTATIONS;
//	private static final Collection<Class<? extends Annotation>> UPDATEABLE_ANNOTATIONS;

	static {

		Set<Class<? extends Annotation>> annotations = new HashSet<Class<? extends Annotation>>();
//		annotations.add(OneToMany.class);
//		annotations.add(OneToOne.class);
//		annotations.add(ManyToMany.class);
//		annotations.add(ManyToOne.class);
		annotations.add(Embedded.class);
//
		ASSOCIATION_ANNOTATIONS = Collections.unmodifiableSet(annotations);

		annotations = new HashSet<Class<? extends Annotation>>();
		annotations.add(Id.class);
		annotations.add(EmbeddedId.class);

		ID_ANNOTATIONS = Collections.unmodifiableSet(annotations);

//		annotations = new HashSet<Class<? extends Annotation>>();
//		annotations.add(Column.class);
//		annotations.add(OrderColumn.class);
//
//		UPDATEABLE_ANNOTATIONS = Collections.unmodifiableSet(annotations);
	}
	
	private final TypeInformation<?> associationTargetType;
	
	public JdbcPersistentPropertyImpl(Field field, PropertyDescriptor propertyDescriptor,
			PersistentEntity<?, JdbcPersistentProperty> owner, SimpleTypeHolder simpleTypeHolder) {
		super(field, propertyDescriptor, owner, simpleTypeHolder);
		
		this.associationTargetType = isAssociation() ? detectAssociationTargetType() : null;
	}
	
	@Override
	public Class<?> getActualType() {
		return associationTargetType == null ? super.getActualType() : associationTargetType.getType();
	}
	
	@Override
	public Iterable<? extends TypeInformation<?>> getPersistentEntityType() {
		return associationTargetType == null ? super.getPersistentEntityType() : Collections
				.singleton(associationTargetType);
	}

	@Override
	protected Association<JdbcPersistentProperty> createAssociation() {
		return new Association<JdbcPersistentProperty>(this, null);
	}
	
	@Override
	public boolean isAssociation() {
		for (Class<? extends Annotation> annotationType : ASSOCIATION_ANNOTATIONS) {
			if (findAnnotation(annotationType) != null) {
				return true;
			}
		}

		if (getType().isAnnotationPresent(Embeddable.class)) {
			return true;
		}
		
//		// annotation 없이, pojo 정의 시...
//		// 이 코드는 상황을 봐서 지우야 할 수도 있다!
//		if (isEntity()) {
//			return true;
//		}

		return false;
	}
	
	@Override
	public boolean isIdProperty() {

		for (Class<? extends Annotation> annotation : ID_ANNOTATIONS) {
			if (isAnnotationPresent(annotation)) {
				return true;
			}
		}

		return false;
	}
	
	@Override
	public boolean isTransient() {

		// transient modifier가 있을 경우
		if( getField() != null && Modifier.isTransient(getField().getModifiers()) ) {
			return true;
		}
		
		return isAnnotationPresent(Transient.class) || super.isTransient();
	}
	
	@Override
	public boolean isColumn() {
		
		if (isTransient() || isIdProperty()) {
			return false;
		}
//		if (isTransient() || isAssociation() || isIdProperty()) {
//			return false;
//		}
		
		if (isAnnotationPresent(Column.class)) {
			return true;
		}
		
		// 일반 field일 경우에도 Column 취급 한다.
		return true;
	}
	
	@Override
	public boolean isOwnerEntity() {
		
		
		if (getOwner() != null) {
			return true;
		}
		else {
			return false;
		}
	}
	
	private TypeInformation<?> detectAssociationTargetType() {

		for (Class<? extends Annotation> associationAnnotation : ASSOCIATION_ANNOTATIONS) {

			Annotation annotation = findAnnotation(associationAnnotation);
			Object targetEntity = AnnotationUtils.getValue(annotation, "targetEntity");

			if (targetEntity != null && !void.class.equals(targetEntity)) {
				return ClassTypeInformation.from((Class<?>) targetEntity);
			}
		}

		return null;
	}
}
