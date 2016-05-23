package org.springframework.data.jdbc.domain.sample2;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jdbc.domain.sample.Foo;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.util.Assert;

@Repository
public class FooDaoImpl implements FooDaoCustom {

	@Autowired
	JdbcTemplate jdbc;
	
	@Override
	public Foo customMethod(Foo foo) {
		
		Assert.notNull(foo);
		Assert.notNull(jdbc);
		return foo;
	}
}
