package org.springframework.data.jdbc.repository.sql;

import java.sql.DatabaseMetaData;
import java.sql.SQLException;
import java.util.Map;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jdbc.repository.support.JdbcEntityInformation;

/**
 * modified
 * 
 * <a href="https://github.com/jirutka/spring-data-jdbc-repository">link original source</a> 
 * 
 * @author Tomasz Nurkiewicz 
 * @author Jakub Jirutka
 * @author Jihwan Hwang
 *
 */
public interface SqlGenerator {

    /**
     * This method is used by {@link SqlGeneratorResolver} to select a right
     * SQL Generator.
     *
     * @param metadata The database metadata.
     * @return Whether is this generator compatible with the database described
     *         by the given {@code metadata}.
     */
    boolean isCompatible(DatabaseMetaData metadata) throws SQLException;


    String count(JdbcEntityInformation<?, ?> information);

    String deleteAll(JdbcEntityInformation<?, ?> information);

    String deleteById(JdbcEntityInformation<?, ?> information);

    String deleteByIds(JdbcEntityInformation<?, ?> information, int idsCount);

    String existsById(JdbcEntityInformation<?, ?> information);

    String insert(JdbcEntityInformation<?, ?> information, Map<String, Object> columns);

    String selectAll(JdbcEntityInformation<?, ?> information);

    String selectAll(JdbcEntityInformation<?, ?> information, Pageable page);

    String selectAll(JdbcEntityInformation<?, ?> information, Sort sort);

    String selectById(JdbcEntityInformation<?, ?> information);

    String selectByIds(JdbcEntityInformation<?, ?> information, int idsCount);

    String update(JdbcEntityInformation<?, ?> information, Map<String, Object> columns);
}
