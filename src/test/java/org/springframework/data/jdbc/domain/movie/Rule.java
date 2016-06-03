package org.springframework.data.jdbc.domain.movie;

public interface Rule {

	boolean match(Showing showing);
}
