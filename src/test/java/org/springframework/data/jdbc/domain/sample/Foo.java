package org.springframework.data.jdbc.domain.sample;

import javax.persistence.Embedded;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;

import org.springframework.data.jdbc.domain.AbstractJdbcPersistable;

import lombok.Data;

@Entity @Data
public class Foo extends AbstractJdbcPersistable<Foo, Bar> {

	@EmbeddedId
	Bar id;
	
	@Embedded
	Address addr;
	
	String name;
	
	public static Foo instance(Bar bar) {
		Foo foo = new Foo(bar);
		return foo;
	}

	public Foo(Bar bar) {
		this.id = bar;
	}
}
