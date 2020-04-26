package com.telecominfraproject.wlan.systemevent.datastore.rdbms;

import javax.sql.DataSource;

import com.telecominfraproject.wlan.core.server.jdbc.BaseJDbcDataSource;
import com.telecominfraproject.wlan.core.server.jdbc.BaseKeyColumnConverter;

/**
 * @author dtoptygin
 * SQL DataSource that is used by SystemEvent Service
 */
public class SystemEventDataSourceImpl extends BaseJDbcDataSource implements SystemEventDataSourceInterface {
    
    public SystemEventDataSourceImpl(DataSource targetDataSource, BaseKeyColumnConverter keyColumnConverter){
        super(targetDataSource, keyColumnConverter);
    }

}
