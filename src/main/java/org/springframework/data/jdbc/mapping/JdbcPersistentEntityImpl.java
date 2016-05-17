package org.springframework.data.jdbc.mapping;

import org.springframework.data.mapping.model.BasicPersistentEntity;
import org.springframework.data.util.TypeInformation;

public class JdbcPersistentEntityImpl<T> extends BasicPersistentEntity<T, JdbcPersistentProperty> implements JdbcPersistentEntity<T> {

	public JdbcPersistentEntityImpl(TypeInformation<T> information) {
		super(information, null);
	}

	@Override
	protected JdbcPersistentProperty returnPropertyIfBetterIdPropertyCandidateOrNull(JdbcPersistentProperty property) {
		return property.isIdProperty() ? property : null;
	}
	
	
}
