package com.telecominfraproject.wlan.manufacturer.datastore.rdbms;

import javax.sql.DataSource;

import com.telecominfraproject.wlan.core.server.jdbc.BaseJDbcDataSource;
import com.telecominfraproject.wlan.core.server.jdbc.BaseKeyColumnConverter;

/**
 * @author mpreston
 * SQL DataSource that is used by Manufacturer Service
 */
public class ManufacturerDataSourceImpl extends BaseJDbcDataSource implements ManufacturerDataSourceInterface {
    
    public ManufacturerDataSourceImpl(DataSource targetDataSource, BaseKeyColumnConverter keyColumnConverter){
        super(targetDataSource, keyColumnConverter);
    }

}
