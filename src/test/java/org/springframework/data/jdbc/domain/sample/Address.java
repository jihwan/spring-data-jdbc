package org.springframework.data.jdbc.domain.sample;
import javax.persistence.Column;
import javax.persistence.Embeddable;

import lombok.Data;

/**
 * 
 */
@Embeddable @Data
public class Address {
	
	@Column()
	private String street;
	
	@Column()
	private String zipcode;
	
	@Column()
	private String city;
	
	@Column()
	private Double no;
}