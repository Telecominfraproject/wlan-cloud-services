package com.telecominfraproject.wlan.adoptionmetrics.datastore.rdbms;

import javax.sql.DataSource;

import com.telecominfraproject.wlan.core.server.jdbc.BaseJDbcDataSource;
import com.telecominfraproject.wlan.core.server.jdbc.BaseKeyColumnConverter;

/**
 * @author dtoptygin
 * SQL DataSource that is used by AdoptionMetrics Service
 */
public class AdoptionMetricsDataSourceImpl extends BaseJDbcDataSource implements AdoptionMetricsDataSourceInterface {
    
    public AdoptionMetricsDataSourceImpl(DataSource targetDataSource, BaseKeyColumnConverter keyColumnConverter){
        super(targetDataSource, keyColumnConverter);
    }

}
