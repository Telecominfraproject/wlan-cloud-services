package com.telecominfraproject.wlan.servicemetric.datastore.rdbms;

import javax.sql.DataSource;

import com.telecominfraproject.wlan.core.server.jdbc.BaseJDbcDataSource;
import com.telecominfraproject.wlan.core.server.jdbc.BaseKeyColumnConverter;

/**
 * @author dtoptygin
 * SQL DataSource that is used by ServiceMetric Service
 */
public class ServiceMetricDataSourceImpl extends BaseJDbcDataSource implements ServiceMetricDataSourceInterface {
    
    public ServiceMetricDataSourceImpl(DataSource targetDataSource, BaseKeyColumnConverter keyColumnConverter){
        super(targetDataSource, keyColumnConverter);
    }

}
