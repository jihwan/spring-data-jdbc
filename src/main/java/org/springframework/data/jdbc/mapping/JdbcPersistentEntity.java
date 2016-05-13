package org.springframework.data.jdbc.mapping;

import org.springframework.data.mapping.PersistentEntity;

public interface JdbcPersistentEntity<T> extends PersistentEntity<T, JdbcPersistentProperty> {
}
