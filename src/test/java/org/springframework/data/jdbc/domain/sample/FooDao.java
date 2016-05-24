package org.springframework.data.jdbc.domain.sample;

import java.util.List;

import org.springframework.data.jdbc.domain.sample2.FooDaoCustom;
import org.springframework.data.jdbc.repository.JdbcRepository;
import org.springframework.data.jdbc.repository.Modifying;
import org.springframework.data.jdbc.repository.Query;
import org.springframework.transaction.annotation.Transactional;

@Transactional(readOnly=true)
public interface FooDao extends JdbcRepository<Foo, Bar>, FooDaoCustom {
	 
	@Query("select name, street, b, a, no from foo where name = ?")
	List<Foo> findByName(String name);
	
	@Transactional
	@Modifying
	@Query("update foo set name = ? where a = ? and b = ?")
	int updateByName(String name, String barA, String barB);
	
	@Transactional
	@Modifying
	@Query("delete from foo where name = ?")
	int deleteByName(String name);
	
	@Transactional
	@Modifying
	@Query("delete from foo where name = 'aaaa'")
	int deleteDummy();
}
