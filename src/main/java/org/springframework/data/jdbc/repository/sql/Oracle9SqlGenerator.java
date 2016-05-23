package org.springframework.data.jdbc.repository.sql;

import static java.lang.String.format;

import java.sql.DatabaseMetaData;
import java.sql.SQLException;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jdbc.repository.support.JdbcEntityInformation;


/**
 * modified
 * 
 * SQL Generator for Oracle up to 11g. If you have 12g or newer, then use
 * {@link SQL2008SqlGenerator}.
 *
 * @see <a href="https://explainextended.com/2009/05/06/oracle-row_number-vs-rownum/">
 *      Oracle: ROW_NUMBER vs ROWNUM</a>
 *  
 * <a href="https://github.com/jirutka/spring-data-jdbc-repository">link original source</a> 
 * 
 * @author Tomasz Nurkiewicz 
 * @author Jakub Jirutka
 * @author Jihwan Hwang
 *
 */
public class Oracle9SqlGenerator extends SimpleSqlGenerator {

    @Override
    public boolean isCompatible(DatabaseMetaData metadata) throws SQLException {
        return "Oracle".equals(metadata.getDatabaseProductName());
    }

    @Override
    public String selectAll(JdbcEntityInformation<?, ?> information, Pageable page) {
        Sort sort = page.getSort() != null ? page.getSort() : sortById(information);

        return format("SELECT t2__.* FROM ( "
                + "SELECT t1__.*, ROWNUM as rn__ FROM ( %s ) t1__ "
                + ") t2__ WHERE t2__.rn__ > %d AND ROWNUM <= %d",
            selectAll(information, sort), page.getOffset(), page.getPageSize());
    }
}
