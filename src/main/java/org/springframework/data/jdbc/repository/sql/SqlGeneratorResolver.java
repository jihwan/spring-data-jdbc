package org.springframework.data.jdbc.repository.sql;
import java.sql.DatabaseMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.dao.DataAccessResourceFailureException;

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
public class SqlGeneratorResolver {

    private static final Logger LOG = LoggerFactory.getLogger(SqlGeneratorResolver.class);

    private final List<Class<?>> clazzs;

    /**
     * @param registerDefault Whether to register default (built-in) generators.
     * @see #getInstance()
     */
    public SqlGeneratorResolver() {
    	clazzs = new ArrayList<Class<?>>();
 
    	clazzs.add(LimitOffsetSqlGenerator.class);
    	clazzs.add(SQL2008SqlGenerator.class);
    	clazzs.add(Oracle9SqlGenerator.class);
    }


    /**
     * @param dataSource The DataSource for which to find compatible
     *        SQL Generator.
     * @return An SQL Generator compatible with the given {@code dataSource}.
     * @throws DataAccessResourceFailureException if exception is thrown when
     *         trying to obtain Connection or MetaData from the
     *         {@code dataSource}.
     * @throws IllegalStateException if no compatible SQL Generator is found.
     */
    public SqlGenerator resolveSqlGenerator(DataSource dataSource) {
    	
    	DatabaseMetaData metaData;
    	try {
    		metaData = dataSource.getConnection().getMetaData();
    	} catch (SQLException ex) {
    		throw new DataAccessResourceFailureException(
    				"Failed to retrieve database metadata", ex);
    	}
    	
    	for (Class<?> clazz : clazzs) {
			
    		SqlGenerator sqlGenerator = (SqlGenerator) BeanUtils.instantiate(clazz);
    		try {
    			if (sqlGenerator.isCompatible(metaData)) {
    				LOG.info("Using SQL Generator {} for dataSource {}",
    						sqlGenerator.getClass().getName(), dataSource.getClass());
    				
    				return sqlGenerator;
    			}
    		} catch (SQLException ex) {
    			LOG.warn("Exception occurred when invoking isCompatible() on {}",
    					sqlGenerator.getClass().getSimpleName(), ex);
    		}
		}

        // This should not happen, because registry should always contain one
        // "default" generator that returns true for every DatabaseMetaData.
        throw new IllegalStateException("No compatible SQL Generator found.");
    }

}