package study.domain;

import javax.persistence.EmbeddedId;

import lombok.Data;

@Data
public class Foo {

	@EmbeddedId
	Bar id;
	
}
