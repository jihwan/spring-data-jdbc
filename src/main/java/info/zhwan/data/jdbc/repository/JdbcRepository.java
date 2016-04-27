package info.zhwan.data.jdbc.repository;

import java.io.Serializable;

import org.springframework.data.repository.PagingAndSortingRepository;

public interface JdbcRepository<T, ID extends Serializable> extends PagingAndSortingRepository<T, ID> {
}
