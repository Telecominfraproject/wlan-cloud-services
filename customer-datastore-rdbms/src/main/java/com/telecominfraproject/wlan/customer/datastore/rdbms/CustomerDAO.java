package com.telecominfraproject.wlan.customer.datastore.rdbms;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.telecominfraproject.wlan.core.model.pair.PairIntString;
import com.telecominfraproject.wlan.core.server.jdbc.BaseJdbcDao;
import com.telecominfraproject.wlan.customer.models.Customer;
import com.telecominfraproject.wlan.datastore.exceptions.DsConcurrentModificationException;
import com.telecominfraproject.wlan.datastore.exceptions.DsDuplicateEntityException;
import com.telecominfraproject.wlan.datastore.exceptions.DsEntityNotFoundException;

/**
 * @author dtop
 *
 */
@Repository
@Transactional(propagation = Propagation.MANDATORY)
public class CustomerDAO extends BaseJdbcDao {

    private static final String COL_ID = "id";

    private static final String[] GENERATED_KEY_COLS = { COL_ID };

    static final String[] ALL_COLUMNS_LIST = { COL_ID, 
    		"email", "name", "details", 
            "createdTimestamp", "lastModifiedTimestamp" };

    private static final Set<String> columnsToSkipForInsert = new HashSet<>(Arrays.asList(COL_ID));

    private static final Set<String> columnsToSkipForUpdate = new HashSet<>(
            Arrays.asList(COL_ID, "createdTimestamp"));

    public static final Set<String> ALL_COLUMNS_LOWERCASE = new HashSet<>();

    //use this for queries where multiple tables are involved
    public static final String ALL_COLUMNS_WITH_PREFIX;
    public static final String TABLE_PREFIX = "c.";
    
    public static final String ALL_COLUMNS;
    private static final String ALL_COLUMNS_FOR_INSERT;
    private static final String BIND_VARS_FOR_INSERT;
    private static final String ALL_COLUMNS_UPDATE;

    static {
        StringBuilder strbAllColumns = new StringBuilder(1024);
        StringBuilder strbAllColumnsWithPrefix = new StringBuilder(1024);
        StringBuilder strbAllColumnsForInsert = new StringBuilder(1024);
        StringBuilder strbBindVarsForInsert = new StringBuilder(128);
        StringBuilder strbColumnsForUpdate = new StringBuilder(512);
        
        for (String colName : ALL_COLUMNS_LIST) {

            ALL_COLUMNS_LOWERCASE.add(colName.toLowerCase());
            
            strbAllColumns.append(colName).append(",");
            strbAllColumnsWithPrefix.append(TABLE_PREFIX).append(colName).append(",");

            if (!columnsToSkipForInsert.contains(colName)) {
                strbAllColumnsForInsert.append(colName).append(",");
                strbBindVarsForInsert.append("?,");
            }

            if (!columnsToSkipForUpdate.contains(colName)) {
                strbColumnsForUpdate.append(colName).append("=?,");
            }

        }

        // remove trailing ','
        strbAllColumns.deleteCharAt(strbAllColumns.length() - 1);
        strbAllColumnsWithPrefix.deleteCharAt(strbAllColumnsWithPrefix.length() - 1);
        strbAllColumnsForInsert.deleteCharAt(strbAllColumnsForInsert.length() - 1);
        strbBindVarsForInsert.deleteCharAt(strbBindVarsForInsert.length() - 1);
        strbColumnsForUpdate.deleteCharAt(strbColumnsForUpdate.length() - 1);

        ALL_COLUMNS = strbAllColumns.toString();
        ALL_COLUMNS_WITH_PREFIX = strbAllColumnsWithPrefix.toString();
        ALL_COLUMNS_FOR_INSERT = strbAllColumnsForInsert.toString();
        BIND_VARS_FOR_INSERT = strbBindVarsForInsert.toString();
        ALL_COLUMNS_UPDATE = strbColumnsForUpdate.toString();

    }

    private static final String SQL_GET_BY_ID = "select " + ALL_COLUMNS + " from customer_info " + " where id = ?";

    private static final String SQL_GET_LASTMOD_BY_ID = "select lastModifiedTimestamp " + " from customer_info "
            + " where id = ?";

    private static final String SQL_GET_BATCH = "select id, name" + " from customer_info " + " where id > ? "
            + " order by id " + " limit ? ";


    private static final String SQL_INSERT = "insert into customer_info ( " + ALL_COLUMNS_FOR_INSERT + " ) values ( "
            + BIND_VARS_FOR_INSERT + " ) ";

    private static final String SQL_DELETE = "delete from customer_info where id = ? ";

    // last parameter will allow us to skip check for concurrent modification,
    // if necessary
    private static final String SQL_UPDATE = "update customer_info set " + ALL_COLUMNS_UPDATE + " where id = ? "
            + " and ( lastModifiedTimestamp = ? or ? = true) ";


    private static final String SQL_GET_ALL_NAME_OR_EMAIL_CONTAINS = "select " + ALL_COLUMNS
            + " from customer_info "
            + " where lower(name) like lower(?) or lower(email) like lower(?) or (cast ( id as varchar(25)) like (?)) limit ?";

    private static final String SQL_GET_ALL_IN_SET = "select " + ALL_COLUMNS + " from customer_info " + " where id in ";

    private static final RowMapper<Customer> customerRowMapper = new CustomerRowMapper();

    private static final Logger LOG = LoggerFactory.getLogger(CustomerDAO.class);

    @Autowired(required = false)
    public void setDataSource(CustomerDataSourceInterface dataSource) {
        setDataSource((DataSource) dataSource);
    }

    public Customer create(final Customer customer) {

        KeyHolder keyHolder = new GeneratedKeyHolder();
        final long ts = System.currentTimeMillis();
        try {
            jdbcTemplate.update(new PreparedStatementCreator() {
                public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {
                    PreparedStatement ps = connection.prepareStatement(SQL_INSERT,
                            keyColumnConverter.getKeyColumnName(GENERATED_KEY_COLS));
                    int colIdx = 1;

                    ps.setString(colIdx++, customer.getEmail().toLowerCase());
                    ps.setString(colIdx++, customer.getName());
                  	ps.setBytes(colIdx++, (customer.getDetails()!=null)?customer.getDetails().toZippedBytes():null);
                    
                    ps.setLong(colIdx++, ts);
                    ps.setLong(colIdx++, ts);

                    return ps;
                }
            }, keyHolder);
        } catch (DuplicateKeyException e) {
            throw new DsDuplicateEntityException(e);
        }

        // keyHolder.getKey() now contains the generated key
        customer.setId(((Integer) keyHolder.getKeys().get(COL_ID)));
        customer.setCreatedTimestamp(ts);
        customer.setLastModifiedTimestamp(ts);

        LOG.debug("Stored Customer {}", customer);

        return new Customer(customer);
    }

    public Customer get(int id) {

        LOG.debug("Looking up customer {}", id);
        try {
            Customer customer = this.jdbcTemplate.queryForObject(SQL_GET_BY_ID, customerRowMapper, id);
            LOG.debug("Found customer {}", customer);

            return customer;
        } catch (EmptyResultDataAccessException e) {
            throw new DsEntityNotFoundException(e);
        }

    }

    @Transactional(noRollbackFor = { EmptyResultDataAccessException.class })
    public Customer getOrNull(int id) {

        LOG.debug("Looking up customer {}", id);
        try {
            Customer customer = this.jdbcTemplate.queryForObject(SQL_GET_BY_ID, customerRowMapper, id);

            LOG.debug("Found customer {}", customer);

            return customer;
        } catch (EmptyResultDataAccessException e) {
            return null;
        }

    }


    public List<PairIntString> getAll(int batchSize, int continueAfterCustomerId) {
        List<PairIntString> results;
        
        results = this.jdbcTemplate.query(SQL_GET_BATCH, new RowMapper<PairIntString>() {
                @Override
                public PairIntString mapRow(ResultSet rs, int rowNum) throws SQLException {
                    return new PairIntString(rs.getInt(1), rs.getString(2));
                }
            }, continueAfterCustomerId, batchSize);
            
        return results;
    }

    public List<Customer> find(String filterStr, int maxResults) {
        String filter = "%" + filterStr.toLowerCase() + "%";
        List<Customer> results = this.jdbcTemplate.query(SQL_GET_ALL_NAME_OR_EMAIL_CONTAINS, customerRowMapper,
                filter, filter, filter, maxResults);

        return results;
    }

    public Customer delete(int id) {
        Customer customer = get(id);

        this.jdbcTemplate.update(SQL_DELETE, id);

        LOG.debug("Deleted {}", customer);

        return customer;
    }


    public List<Customer> getCustomersInSet(Set<Integer> customerIdSet) {
        LOG.debug("getCustomersInSet()");

        if (customerIdSet == null || customerIdSet.isEmpty()) {
            return Collections.emptyList();
        }

        String set = customerIdSet.toString();
        set = "(" + set.substring(1, set.length() - 1) + ")";
        String query = SQL_GET_ALL_IN_SET + set;
        List<Customer> results = this.jdbcTemplate.query(query, customerRowMapper);

        LOG.debug("getCustomersInSet() returns {} record(s)", results.size());
        return results;
    }


    public Customer update(Customer customer) {
        long incomingLastModifiedTs = customer.getLastModifiedTimestamp();
        long newLastModifiedTs = System.currentTimeMillis();

        int updateCount;
        updateCount = this.jdbcTemplate.update(SQL_UPDATE,
                // customer.getId(), - not updating this one
                customer.getEmail(),
                customer.getName(),
                (customer.getDetails()!=null)?customer.getDetails().toZippedBytes():null ,
                
                //customer.getCreatedTimestamp(), - not updating this one 
                newLastModifiedTs,
                
                // use id for update operation
                customer.getId(),
                // use lastModifiedTimestamp for data protection against concurrent modifications
                incomingLastModifiedTs, isSkipCheckForConcurrentUpdates());

        if (updateCount == 0) {
            try {

                if (isSkipCheckForConcurrentUpdates()) {
                    // in this case we did not request protection against concurrent updates,
                    // so the updateCount is 0 because record in db was not found
                    throw new EmptyResultDataAccessException(1);
                }

                // find out if record could not be updated because it does not
                // exist or because it was modified concurrently
                long recordTimestamp = this.jdbcTemplate.queryForObject(SQL_GET_LASTMOD_BY_ID, Long.class,
                        customer.getId());

                LOG.debug(
                        "Concurrent modification detected for customer with id {} expected version is {} but version in db was {}",
                        customer.getId(), incomingLastModifiedTs, recordTimestamp);
                throw new DsConcurrentModificationException("Concurrent modification detected for customer with id "
                        + customer.getId() + " expected version is " + incomingLastModifiedTs
                        + " but version in db was " + recordTimestamp);

            } catch (EmptyResultDataAccessException e) {
                LOG.debug("Cannot find customer {}", customer.getId());
                throw new DsEntityNotFoundException("Customer not found " + customer.getId());
            }
        }

        Customer result = get(customer.getId());
        
        LOG.debug("Updated {}", result);

        return result;
    }

}
