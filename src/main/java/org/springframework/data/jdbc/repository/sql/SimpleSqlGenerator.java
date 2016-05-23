package org.springframework.data.jdbc.repository.sql;

import static java.lang.String.format;
import static org.springframework.util.StringUtils.collectionToDelimitedString;

import java.sql.DatabaseMetaData;
import java.sql.SQLException;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.domain.Sort.Order;
import org.springframework.data.jdbc.repository.support.JdbcEntityInformation;
import org.springframework.util.Assert;


/**
 * modified
 * 
 * SQL Generator compatible with SQL:99.
 *  
 * <a href="https://github.com/jirutka/spring-data-jdbc-repository">link original source</a> 
 * 
 * @author Tomasz Nurkiewicz 
 * @author Jakub Jirutka
 * @author Jihwan Hwang
 *
 */
public class SimpleSqlGenerator implements SqlGenerator {

    static final String
            AND = " AND ",
            OR = " OR ",
            COMMA = ", ",
            PARAM = " = ?";


    public boolean isCompatible(DatabaseMetaData metadata) throws SQLException {
        return true;
    }


    public String count(JdbcEntityInformation<?, ?> information) {
        return format("SELECT count(*) FROM %s", information.getEntityName());
    }

    public String deleteAll(JdbcEntityInformation<?, ?> information) {
        return format("DELETE FROM %s", information.getEntityName());
    }

    public String deleteById(JdbcEntityInformation<?, ?> information) {
        return deleteByIds(information, 1);
    }

    public String deleteByIds(JdbcEntityInformation<?, ?> information, int idsCount) {
        return deleteAll(information) + " WHERE " + idsPredicate(information, idsCount);
    }

    public String existsById(JdbcEntityInformation<?, ?> information) {
        return format("SELECT 1 FROM %s WHERE %s", information.getEntityName(), idPredicate(information));
    }

    public String insert(JdbcEntityInformation<?, ?> information, Map<String, Object> columns) {

        return format("INSERT INTO %s (%s) VALUES (%s)",
            information.getEntityName(),
            collectionToDelimitedString(columns.keySet(), COMMA),
            repeat("?", COMMA, columns.size()));
    }

    public String selectAll(JdbcEntityInformation<?, ?> information) {
        return format("SELECT %s FROM %s", "*", information.getEntityName());
    }

    public String selectAll(JdbcEntityInformation<?, ?> information, Pageable page) {
        Sort sort = page.getSort() != null ? page.getSort() : sortById(information);

        return format("SELECT t2__.* FROM ( "
                + "SELECT row_number() OVER (ORDER BY %s) AS rn__, t1__.* FROM ( %s ) t1__ "
                + ") t2__ WHERE t2__.rn__ BETWEEN %s AND %s",
            orderByExpression(sort), selectAll(information),
            page.getOffset() + 1, page.getOffset() + page.getPageSize());
    }

    public String selectAll(JdbcEntityInformation<?, ?> information, Sort sort) {
        return selectAll(information) + (sort != null ? orderByClause(sort) : "");
    }

    public String selectById(JdbcEntityInformation<?, ?> information) {
        return selectByIds(information, 1);
    }

    public String selectByIds(JdbcEntityInformation<?, ?> information, int idsCount) {
        return idsCount > 0
            ? selectAll(information) + " WHERE " + idsPredicate(information, idsCount)
            : selectAll(information);
    }

    public String update(JdbcEntityInformation<?, ?> information, Map<String, Object> columns) {

        return format("UPDATE %s SET %s WHERE %s",
            information.getEntityName(),
            formatParameters(columns.keySet(), COMMA),
            idPredicate(information));
    }


    protected String orderByClause(Sort sort) {
        return " ORDER BY " + orderByExpression(sort);
    }

    protected String orderByExpression(Sort sort) {
        StringBuilder sb = new StringBuilder();

        for (Iterator<Order> it = sort.iterator(); it.hasNext(); ) {
            Order order = it.next();
            sb.append(order.getProperty()).append(' ').append(order.getDirection());

            if (it.hasNext()) sb.append(COMMA);
        }
        return sb.toString();
    }

    protected Sort sortById(JdbcEntityInformation<?, ?> information) {
        return new Sort(Direction.ASC, information.getIdAttributeNames());
    }


    private String idPredicate(JdbcEntityInformation<?, ?> information) {
        return formatParameters(information.getIdAttributeNames(), AND);
    }

    private String idsPredicate(JdbcEntityInformation<?, ?> information, int idsCount) {
        Assert.isTrue(idsCount > 0, "idsCount must be greater than zero");

        List<String> idColumnNames = information.getIdAttributeNames();

        if (idsCount == 1) {
            return idPredicate(information);

        } else if (idColumnNames.size() > 1) {
            return repeat("(" + formatParameters(idColumnNames, AND) + ")", OR, idsCount);

        } else {
            return idColumnNames.get(0) + " IN (" + repeat("?", COMMA, idsCount) + ")";
        }
    }

    private String formatParameters(Collection<String> columns, String delimiter) {
        return collectionToDelimitedString(columns, delimiter, "", PARAM);
    }
    
    /**
     * Repeats the given String {@code count}-times to form a new String, with
     * the {@code separator} injected between.
     *
     * @param str The string to repeat.
     * @param separator The string to inject between.
     * @param count Number of times to repeat {@code str}; negative treated
     *              as zero.
     * @return A new String.
     */
    private String repeat(String str, String separator, int count) {
        StringBuilder sb = new StringBuilder((str.length() + separator.length()) * Math.max(count, 0));

        for (int n = 0; n < count; n++) {
            if (n > 0) sb.append(separator);
            sb.append(str);
        }
        return sb.toString();
    }
}