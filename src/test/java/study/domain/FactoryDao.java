package study.domain;

import org.springframework.data.jdbc.repository.JdbcRepository;

public interface FactoryDao extends JdbcRepository<Factory, String> {
}
