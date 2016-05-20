package org.springframework.data.jdbc.domain.sample;

import lombok.Data;

@Data
public class Address {
	
	private String street;
	
	private String city;
	
	private Double no;
	
	public Address() {}
}