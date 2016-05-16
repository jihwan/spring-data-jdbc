package study.domain;

import java.util.List;

import org.springframework.data.jdbc.repository.JdbcRepository;

public interface FactoryDao extends JdbcRepository<Factory, String> {
	
	List<Factory> findByName(String name);
}
