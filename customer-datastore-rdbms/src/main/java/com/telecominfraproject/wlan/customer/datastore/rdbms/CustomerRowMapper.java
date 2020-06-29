package com.telecominfraproject.wlan.customer.datastore.rdbms;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.RowMapper;

import com.telecominfraproject.wlan.core.model.json.BaseJsonModel;
import com.telecominfraproject.wlan.customer.models.Customer;
import com.telecominfraproject.wlan.customer.models.CustomerDetails;

/**
 * @author dtoptygin
 *
 */

public class CustomerRowMapper implements RowMapper<Customer> {

    private static final Logger LOG = LoggerFactory.getLogger(CustomerRowMapper.class);

    public Customer mapRow(ResultSet rs, int rowNum) throws SQLException {
        Customer customer = new Customer();
        int colIdx=1;
        customer.setId(rs.getInt(colIdx++));

        customer.setEmail(rs.getString(colIdx++));
        customer.setName(rs.getString(colIdx++));

        byte[] zippedBytes = rs.getBytes(colIdx++);
        if (zippedBytes !=null) {
            try {
            	CustomerDetails details = BaseJsonModel.fromZippedBytes(zippedBytes, CustomerDetails.class);
                customer.setDetails(details);
            } catch (RuntimeException exp) {
                LOG.error("Failed to decode CustomerDetails from database for id = {}", customer.getId());
            }
        }

        customer.setCreatedTimestamp(rs.getLong(colIdx++));
        customer.setLastModifiedTimestamp(rs.getLong(colIdx++));

        return customer;
    }
}
