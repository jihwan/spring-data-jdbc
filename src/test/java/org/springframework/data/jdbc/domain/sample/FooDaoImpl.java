package org.springframework.data.jdbc.domain.sample;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;

public class FooDaoImpl implements FooDaoCustom {

	@Autowired
	JdbcTemplate jdbc;
	
	@Override
	public void customMethod(Foo foo) {
		System.err.println("inject jdbcTemplate " + jdbc);
		System.err.println("called customMethod " + foo);
	}

}
