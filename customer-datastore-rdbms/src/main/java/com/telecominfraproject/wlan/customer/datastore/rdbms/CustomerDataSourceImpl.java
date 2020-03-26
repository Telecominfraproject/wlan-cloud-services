package com.telecominfraproject.wlan.customer.datastore.rdbms;

import javax.sql.DataSource;

import org.springframework.jdbc.datasource.DelegatingDataSource;

/**
 * @author dtoptygin
 * SQL DataSource that is used by Customer data store
 */
public class CustomerDataSourceImpl extends DelegatingDataSource implements CustomerDataSourceInterface {
    
    public CustomerDataSourceImpl(DataSource targetDataSource){
        super(targetDataSource);
    }

}
