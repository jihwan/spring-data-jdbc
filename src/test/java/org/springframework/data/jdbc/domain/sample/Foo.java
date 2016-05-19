package org.springframework.data.jdbc.domain.sample;

import javax.persistence.Embedded;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Transient;

import org.springframework.data.jdbc.domain.JdbcPersistable;

import lombok.Data;

@Entity
@Data
public class Foo
//	extends AbstractJdbcPersistable<Foo, Bar> {
	implements JdbcPersistable<Foo, Bar> {

	private static final long serialVersionUID = 8631471766926473778L;

	@Embedded
	Address addR;
	
	String name;

	public Foo() {}
	
	public static Foo instance(Bar bar) {
		Foo foo = new Foo(bar);
		return foo;
	}
	
/**
 * AbstractJdbcPersistable codes
 */	
//	public Foo(Bar bar) {
//		setId(bar);
//	}
//
//	public Address getAddR() {
//		return addR;
//	}
//	
//	public void setAddR(Address addR) {
//		this.addR = addR;
//	}
//	
//	public String getName() {
//		return name;
//	}
//	
//	public void setName(String name) {
//		this.name = name;
//	}
//
//	@Override
//	public String toString() {
//		return "Foo [addr=" + addR + ", name=" + name + ", toString()=" + super.toString() + "]";
//	}
	
	
	
/**
 * JdbcPersistable codes
 */
	transient boolean persisted;
	
	@EmbeddedId
	Bar bar;
	
	public Foo(Bar bar) {
		this.bar = bar;
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
