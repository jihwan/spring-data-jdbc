package study.domain;

import javax.persistence.Entity;
import javax.persistence.Id;

import lombok.Data;

@Data @Entity
public class Machine {
	
	@Id
	String name;

	public Machine(String name) {
		super();
		this.name = name;
	}
}
