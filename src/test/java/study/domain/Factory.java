package study.domain;

import javax.persistence.Entity;
import javax.persistence.Id;

import lombok.Data;

@Data @Entity
public class Factory {
	
	@Id
	String name;

	public Factory(String name) {
		super();
		this.name = name;
	}
}
