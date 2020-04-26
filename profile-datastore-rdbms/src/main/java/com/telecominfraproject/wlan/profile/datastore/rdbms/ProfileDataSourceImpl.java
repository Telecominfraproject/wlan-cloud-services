package com.telecominfraproject.wlan.profile.datastore.rdbms;

import javax.sql.DataSource;

import com.telecominfraproject.wlan.core.server.jdbc.BaseJDbcDataSource;
import com.telecominfraproject.wlan.core.server.jdbc.BaseKeyColumnConverter;

/**
 * @author dtoptygin
 * SQL DataSource that is used by Profile Service
 */
public class ProfileDataSourceImpl extends BaseJDbcDataSource implements ProfileDataSourceInterface {
    
    public ProfileDataSourceImpl(DataSource targetDataSource, BaseKeyColumnConverter keyColumnConverter){
        super(targetDataSource, keyColumnConverter);
    }

}
