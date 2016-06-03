package org.springframework.data.jdbc.domain.movie;

public interface CustomerRepository {

	Customer find(int reserverId);


}
