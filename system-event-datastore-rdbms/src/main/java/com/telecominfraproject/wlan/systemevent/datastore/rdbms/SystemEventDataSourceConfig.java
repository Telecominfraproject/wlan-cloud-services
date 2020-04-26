package com.telecominfraproject.wlan.systemevent.datastore.rdbms;

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
@PropertySource({ "${system-event-ds.props:classpath:system-event-ds.properties}" })
public class SystemEventDataSourceConfig extends BaseDataSourceConfig {

    
    @Bean
    public SystemEventDataSourceInterface systemEventDataSourceInterface(){
        
        SystemEventDataSourceInterface ret = new SystemEventDataSourceImpl(getDataSource(), getKeyColumnConverter());
        return ret;
    }
    
    @Override
    public String getDataSourceName() {
        return "system-event-ds";
    }
}
