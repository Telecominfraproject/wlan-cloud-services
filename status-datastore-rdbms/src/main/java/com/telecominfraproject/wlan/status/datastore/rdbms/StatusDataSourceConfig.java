package com.telecominfraproject.wlan.status.datastore.rdbms;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Profile;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

import com.telecominfraproject.wlan.core.server.jdbc.BaseDataSourceConfig;

/**
 * @author dtoptygin
 *
 */
@Component
@Profile("!use_single_ds")
@PropertySource({ "${status-ds.props:classpath:status-ds.properties}" })
public class StatusDataSourceConfig extends BaseDataSourceConfig {

    
    @Bean
    public StatusDataSourceInterface statusDataSourceInterface(){
        
        StatusDataSourceInterface ret = new StatusDataSourceImpl(getDataSource(), getKeyColumnConverter());
        return ret;
    }
    
    @Override
    public String getDataSourceName() {
        return "status-ds";
    }
}
