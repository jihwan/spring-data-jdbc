package study.domain;

import lombok.Data;

@Data
public class Factory {
	
	String name;

	public Factory(String name) {
		super();
		this.name = name;
	}
}
