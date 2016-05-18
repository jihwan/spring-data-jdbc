package org.springframework.data.jdbc.domain.sample;

import org.adrianwalker.multilinestring.Multiline;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

@Component
public class InitializeTables implements InitializingBean {

	@Autowired
	JdbcTemplate jdbcTemplate;
	
	/**CREATE TABLE foo (
	a VARCHAR(255), 
	b VARCHAR(255),
	
	street VARCHAR(255),
	zipcode VARCHAR(255),
	city VARCHAR(255),
	no number,
	 
	name VARCHAR(255)
)*/
	@Multiline
	String fooDdl;
	
	
	@Override
	public void afterPropertiesSet() throws Exception {
		jdbcTemplate.execute("DROP TABLE foo IF EXISTS");
		jdbcTemplate.execute(fooDdl);
	}

}
