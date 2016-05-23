package org.springframework.data.jdbc.repository.sql;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jdbc.repository.support.JdbcEntityInformation;

import java.sql.DatabaseMetaData;
import java.sql.SQLException;
import java.util.List;

import static java.lang.String.format;
import static java.util.Arrays.asList;

/**
 * modified
 * 
 * SQL Generator for DB servers that support LIMIT ... OFFSET clause:
 * PostgreSQL, H2, HSQLDB, SQLite, MariaDB, and MySQL.
 *  
 * <a href="https://github.com/jirutka/spring-data-jdbc-repository">link original source</a> 
 * 
 * @author Tomasz Nurkiewicz 
 * @author Jakub Jirutka
 * @author Jihwan Hwang
 *
 */
public class LimitOffsetSqlGenerator extends SimpleSqlGenerator {

    private static final List<String> SUPPORTED_PRODUCTS =
        asList("PostgreSQL", "H2", "HSQL Database Engine", "MySQL");


    @Override
    public boolean isCompatible(DatabaseMetaData metadata) throws SQLException {
        return SUPPORTED_PRODUCTS.contains(metadata.getDatabaseProductName());
    }

    @Override
    public String selectAll(JdbcEntityInformation<?, ?> information, Pageable page) {
        return format("%s LIMIT %d OFFSET %d",
            selectAll(information, page.getSort()), page.getPageSize(), page.getOffset());
    }
}
