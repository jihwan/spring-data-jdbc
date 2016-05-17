package org.springframework.data.jdbc.domain.sample;

import java.util.List;

import org.adrianwalker.multilinestring.Multiline;
import org.springframework.data.jdbc.repository.JdbcRepository;
import org.springframework.data.jdbc.repository.Query;

public interface FooDao extends JdbcRepository<Foo, Bar> {
	
	/**
select * from foo where name = ?
	 */
	@Multiline
	String hello = "";
	
	
//	@Query(hello)
	@Query("select * from foo where name = ?")
	List<Foo> findByName(String name);

}
