package study.domain;

import javax.persistence.Embeddable;

import lombok.Data;

@Data @Embeddable
public class Bar {

	String a;
	String b;
	
	public Bar(String a, String b) {
		super();
		this.a = a;
		this.b = b;
	}
}
