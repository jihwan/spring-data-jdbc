package info.zhwan.data.mapping;

import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;

import org.springframework.data.mapping.Association;
import org.springframework.data.mapping.PersistentEntity;
import org.springframework.data.mapping.model.AnnotationBasedPersistentProperty;
import org.springframework.data.mapping.model.SimpleTypeHolder;

public class JdbcPersistentProperty extends AnnotationBasedPersistentProperty<JdbcPersistentProperty> {

	public JdbcPersistentProperty(Field field, PropertyDescriptor propertyDescriptor,
			PersistentEntity<?, JdbcPersistentProperty> owner, SimpleTypeHolder simpleTypeHolder) {
		super(field, propertyDescriptor, owner, simpleTypeHolder);
		// TODO Auto-generated constructor stub
	}

	@Override
	protected Association<JdbcPersistentProperty> createAssociation() {
		// TODO Auto-generated method stub
		return null;
	}

}
