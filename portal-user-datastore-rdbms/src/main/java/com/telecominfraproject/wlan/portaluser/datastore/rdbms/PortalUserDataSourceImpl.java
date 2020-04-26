package com.telecominfraproject.wlan.portaluser.datastore.rdbms;

import javax.sql.DataSource;

import com.telecominfraproject.wlan.core.server.jdbc.BaseJDbcDataSource;
import com.telecominfraproject.wlan.core.server.jdbc.BaseKeyColumnConverter;

/**
 * @author dtoptygin
 * SQL DataSource that is used by PortalUser Service
 */
public class PortalUserDataSourceImpl extends BaseJDbcDataSource implements PortalUserDataSourceInterface {
    
    public PortalUserDataSourceImpl(DataSource targetDataSource, BaseKeyColumnConverter keyColumnConverter){
        super(targetDataSource, keyColumnConverter);
    }

}
