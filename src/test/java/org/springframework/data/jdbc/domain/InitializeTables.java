package org.springframework.data.jdbc.domain;

import org.adrianwalker.multilinestring.Multiline;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

/**
 * 
 * @author Jihwan Hwang
 * 
 * @see <a href="https://github.com/benelog/multiline">multiline awesome</a>
 */
@Component
public class InitializeTables implements InitializingBean {

	@Autowired
	JdbcTemplate jdbcTemplate;
	
	/**
	CREATE TABLE foo (
	a VARCHAR(255), 
	b VARCHAR(255),
	
	street VARCHAR(255),
	city VARCHAR(255),
	no number,
	 
	name VARCHAR(255)
	)
	*/
	@Multiline
	final String FOO_DDL = "";
	
	/**
	CREATE TABLE zoo (
	    id bigint not null auto_increment,
	    name VARCHAR(255)
	)
	*/
	@Multiline
	final String ZOO_DDL = "";
	
	
	@Override
	public void afterPropertiesSet() throws Exception {
		
		jdbcTemplate.execute("DROP TABLE foo IF EXISTS");
		jdbcTemplate.execute("DROP TABLE zoo IF EXISTS");
		
		StringBuilder fooddl = new StringBuilder();
		fooddl.append("CREATE TABLE foo (")
		.append("a VARCHAR(255),")
		.append("b VARCHAR(255),")
		.append("street VARCHAR(255),")
		.append("city VARCHAR(255),")
		.append("no number,")
		.append("name VARCHAR(255)")
		.append(")")
		;
		jdbcTemplate.execute(fooddl.toString());
		StringBuilder zooddl = new StringBuilder();
		zooddl.append("CREATE TABLE zoo (")
		.append("id bigint not null auto_increment,")
		.append("name VARCHAR(255)")
		.append(")")
		;
		jdbcTemplate.execute(zooddl.toString());
		
//		jdbcTemplate.execute(FOO_DDL);
//		jdbcTemplate.execute(ZOO_DDL);
	}
}
