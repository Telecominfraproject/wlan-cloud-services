package com.telecominfraproject.wlan.routing.datastore.rdbms;

import javax.sql.DataSource;

import com.telecominfraproject.wlan.core.server.jdbc.BaseJDbcDataSource;
import com.telecominfraproject.wlan.core.server.jdbc.BaseKeyColumnConverter;

/**
 * @author dtoptygin
 * SQL DataSource that is used by EquipmentRoutingRecord Service
 */
public class RoutingDataSourceImpl extends BaseJDbcDataSource implements RoutingDataSourceInterface {
    
    public RoutingDataSourceImpl(DataSource targetDataSource, BaseKeyColumnConverter keyColumnConverter){
        super(targetDataSource, keyColumnConverter);
    }

}
