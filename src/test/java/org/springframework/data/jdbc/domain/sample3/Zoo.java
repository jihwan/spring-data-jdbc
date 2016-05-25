package org.springframework.data.jdbc.domain.sample3;

import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Persistent;
import org.springframework.data.domain.Persistable;

import lombok.Data;

@Persistent @Data
public class Zoo implements Persistable<Long> {

	private static final long serialVersionUID = -6427178169835379151L;

	@Id
	private Long id;
	
	private String name;

	@Override
	public Long getId() {
		return this.id;
	}

	@Override
	public boolean isNew() {
		return id == null;
	}
}
