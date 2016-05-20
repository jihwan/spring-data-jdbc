package org.springframework.data.jdbc.domain.sample;

import java.util.List;

import org.springframework.data.jdbc.domain.sample2.FooDaoCustom;
import org.springframework.data.jdbc.repository.JdbcRepository;
import org.springframework.data.jdbc.repository.Query;

public interface FooDao extends JdbcRepository<Foo, Bar>, FooDaoCustom {
	 
	@Query("select name, street, b, a, no from foo where name = ?")
	List<Foo> findByName(String name);
}
