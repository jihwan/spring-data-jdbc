package org.springframework.data.jdbc.domain.sample;

import java.util.List;

//import org.adrianwalker.multilinestring.Multiline;
import org.springframework.data.jdbc.repository.JdbcRepository;
import org.springframework.data.jdbc.repository.Query;

public interface FooDao extends JdbcRepository<Foo, Bar>, FooDaoCustom {
	

	
	
//	@Query(FooQuery.hello)
	@Query("select * from foo where name = ?")
	List<Foo> findByName(String name);
	
	
	
//	interface FooQuery {
//		/**
//		select * from foo where name = ?
//			 */
//		@Multiline
//		String hello = "a";
//	}

}
