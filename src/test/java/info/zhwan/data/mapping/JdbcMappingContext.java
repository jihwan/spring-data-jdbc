package info.zhwan.data.mapping;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;

import org.springframework.data.mapping.context.AbstractMappingContext;
import org.springframework.data.mapping.model.BasicPersistentEntity;
import org.springframework.data.mapping.model.SimpleTypeHolder;
import org.springframework.data.util.TypeInformation;

public class JdbcMappingContext extends
		AbstractMappingContext<BasicPersistentEntity<Object, JdbcPersistentProperty>, JdbcPersistentProperty> {

	@Override
	@SuppressWarnings("unchecked")
	protected <S> BasicPersistentEntity<Object, JdbcPersistentProperty> createPersistentEntity(
			TypeInformation<S> typeInformation) {
		return new BasicPersistentEntity<Object, JdbcPersistentProperty>((TypeInformation<Object>) typeInformation);
	}

	@Override
	protected JdbcPersistentProperty createPersistentProperty(final Field field, final PropertyDescriptor descriptor,
			final BasicPersistentEntity<Object, JdbcPersistentProperty> owner, final SimpleTypeHolder simpleTypeHolder) {

		return new JdbcPersistentProperty(field, descriptor, owner, simpleTypeHolder);
	}
}