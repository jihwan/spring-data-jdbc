package org.springframework.data.jdbc.repository.sql;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jdbc.repository.support.JdbcEntityInformation;

import java.sql.DatabaseMetaData;
import java.sql.SQLException;

import static java.lang.String.format;

/**
 * modified
 * 
 * SQL Generator for DB servers that support the SQL:2008 standard OFFSET
 * feature: Apache Derby, Microsoft SQL Server 2012, and Oracle 12c.
 * 
 * <a href="https://github.com/jirutka/spring-data-jdbc-repository">link original source</a> 
 * 
 * @author Tomasz Nurkiewicz 
 * @author Jakub Jirutka
 * @author Jihwan Hwang
 *
 */
public class SQL2008SqlGenerator extends SimpleSqlGenerator {

    @Override
    public boolean isCompatible(DatabaseMetaData metadata) throws SQLException {
        String productName = metadata.getDatabaseProductName();
        int majorVersion = metadata.getDatabaseMajorVersion();

        return "Apache Derby".equals(productName)
            || "Oracle".equals(productName) && majorVersion >= 12
            || "Microsoft SQL Server".equals(productName) && majorVersion >= 11;  // >= 2012
    }

    @Override
    public String selectAll(JdbcEntityInformation<?, ?> information, Pageable page) {
        Sort sort = page.getSort() != null ? page.getSort() : sortById(information);

        return format("%s OFFSET %d ROWS FETCH NEXT %d ROW ONLY",
            selectAll(information, sort), page.getOffset(), page.getPageSize());
    }
}