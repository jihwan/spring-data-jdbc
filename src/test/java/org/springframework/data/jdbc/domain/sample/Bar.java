package org.springframework.data.jdbc.domain.sample;

import java.io.Serializable;

import javax.persistence.Embeddable;

import lombok.Data;

@Data @Embeddable
public class Bar implements Serializable {

	String a;
	String b;
	
	public Bar(String a, String b) {
		super();
		this.a = a;
		this.b = b;
	}
}
