package com.telecominfraproject.wlan.firmware.datastore.rdbms;

import javax.sql.DataSource;

import com.telecominfraproject.wlan.core.server.jdbc.BaseJDbcDataSource;
import com.telecominfraproject.wlan.core.server.jdbc.BaseKeyColumnConverter;

/**
 * @author dtoptygin
 * SQL DataSource that is used by Firmware Service
 */
public class FirmwareDataSourceImpl extends BaseJDbcDataSource implements FirmwareDataSourceInterface {
    
    public FirmwareDataSourceImpl(DataSource targetDataSource, BaseKeyColumnConverter keyColumnConverter){
        super(targetDataSource, keyColumnConverter);
    }

}
