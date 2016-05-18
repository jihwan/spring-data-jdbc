package org.springframework.data.jdbc.domain.sample;

import javax.persistence.Embedded;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Transient;

import org.springframework.data.jdbc.domain.JdbcPersistable;

import lombok.Data;

@Entity @Data
public class Foo implements JdbcPersistable<Foo, Bar> {

	transient boolean persisted;
	
	@EmbeddedId
	Bar bar;
	
	@Embedded
	Address addr;
	
	String name;

	public Foo() {}
	
	public Foo(Bar bar) {
		this.bar = bar;
	}
	
	public static Foo instance(Bar bar) {
		Foo foo = new Foo(bar);
		return foo;
	}

	@Override
	public Bar getId() {
		return this.bar;
	}

	@Override @Transient
	public boolean isNew() {
		return !persisted;
	}

	@Override
	public Foo persist(boolean persisted) {
		this.persisted = persisted;
		return this;
	}
}
