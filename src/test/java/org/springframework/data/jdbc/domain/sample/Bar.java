package org.springframework.data.jdbc.domain.sample;

import java.io.Serializable;

import javax.persistence.Embeddable;

import lombok.Data;

@Data @Embeddable
public class Bar implements Serializable {

	private static final long serialVersionUID = 1504236273862949567L;
	
	String a;
	String b;
	
	public Bar() {}
	
	public Bar(String a, String b) {
		super();
		this.a = a;
		this.b = b;
	}
}
