package org.springframework.data.jdbc.domain.sample;

import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Persistent;
import org.springframework.data.annotation.Reference;
import org.springframework.data.annotation.Transient;
import org.springframework.data.jdbc.domain.JdbcPersistable;

import lombok.Data;

@Persistent @Data
public class Foo implements JdbcPersistable<Foo, Bar> {

	private static final long serialVersionUID = 8631471766926473778L;

	private transient boolean persisted;
	
	@Id
	Bar bar;
	
	@Reference
	Address addR;
	
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
	public void persist(boolean persisted) {
		this.persisted = persisted;
	}
}
