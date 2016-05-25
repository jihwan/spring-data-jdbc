package org.springframework.data.jdbc.domain.sample3;

import org.springframework.data.jdbc.repository.JdbcRepository;
import org.springframework.transaction.annotation.Transactional;

@Transactional(readOnly=true)
public interface ZooDao extends JdbcRepository<Zoo, Long> {
}
