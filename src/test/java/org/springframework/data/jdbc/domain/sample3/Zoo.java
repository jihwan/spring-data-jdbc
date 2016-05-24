package org.springframework.data.jdbc.domain.sample3;

import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Persistent;

import lombok.Data;

@Persistent @Data
public class Zoo {

	@Id
	private String name;
	
	private String type;
}
