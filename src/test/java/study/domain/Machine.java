package study.domain;

import lombok.Data;

@Data
public class Machine {
	
	String name;

	public Machine(String name) {
		super();
		this.name = name;
	}
}
