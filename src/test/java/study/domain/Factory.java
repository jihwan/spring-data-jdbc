package study.domain;

import org.springframework.data.annotation.Id;

import lombok.Data;

@Data
public class Factory {
	
	@Id
	String name;

	public Factory(String name) {
		super();
		this.name = name;
	}
}
