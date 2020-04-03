package com.telecominfraproject.wlan.location.datastore.rdbms;

import javax.sql.DataSource;

import com.telecominfraproject.wlan.core.server.jdbc.BaseJDbcDataSource;
import com.telecominfraproject.wlan.core.server.jdbc.BaseKeyColumnConverter;

/**
 * SQL DataSource that is used by Location Service
 * @author dtoptygin
 */
public class LocationDataSourceImpl extends BaseJDbcDataSource implements LocationDataSourceInterface {
    
    public LocationDataSourceImpl(DataSource targetDataSource, 
            BaseKeyColumnConverter keyColumnConverter){
        super(targetDataSource, keyColumnConverter);
    }

}
