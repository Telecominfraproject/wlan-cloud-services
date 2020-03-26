package com.telecominfraproject.wlan.customer.datastore.rdbms;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import com.telecominfraproject.wlan.customer.models.Customer;

/**
 * @author dtoptygin
 *
 */

public class CustomerRowMapper implements RowMapper<Customer> {

    public Customer mapRow(ResultSet rs, int rowNum) throws SQLException {
        Customer customer = new Customer();
        int colIdx=1;
        customer.setId(rs.getInt(colIdx++));

        customer.setEmail(rs.getString(colIdx++));
        customer.setName(rs.getString(colIdx++));

        customer.setCreatedTimestamp(rs.getLong(colIdx++));
        customer.setLastModifiedTimestamp(rs.getLong(colIdx++));

        return customer;
    }
}
