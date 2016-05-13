package study.domain;

import org.springframework.data.jdbc.repository.JdbcRepository;

public interface MachineDao extends JdbcRepository<Machine, String> {

}
