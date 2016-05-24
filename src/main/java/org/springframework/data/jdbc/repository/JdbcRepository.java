package org.springframework.data.jdbc.repository;

import java.io.Serializable;
import java.util.List;

import org.springframework.data.domain.Sort;
import org.springframework.data.repository.NoRepositoryBean;
import org.springframework.data.repository.PagingAndSortingRepository;

@NoRepositoryBean
public interface JdbcRepository<T, ID extends Serializable> extends PagingAndSortingRepository<T, ID> {
	
	List<T> findAll();
	
	List<T> findAll(Sort sort);
	
	List<T> findAll(Iterable<ID> ids);
	
	<S extends T> List<S> save(Iterable<S> entities);
	
	<S extends T> S insert(S entity);
	
	<S extends T> S update(S entity);
	
	/**
	 * 이코드의 문제점은, custom Entity class의 persisted 상태 제어가 불가능 하다는 점이다.
	 * 따라서, {@link JdbcRepository#delete(Entity)} 사용 권장 한다.
	 */
	void delete(ID id);
}
