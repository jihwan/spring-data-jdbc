package org.springframework.data.jdbc.domain.movie;

public class Customer {

	Long id;
	
	String name;

	public Customer() {
	}

	public Customer(String name) {
		super();
		this.name = name;
	}
}
