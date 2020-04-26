package com.telecominfraproject.wlan.alarm.datastore.rdbms;

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
@PropertySource({ "${alarm-ds.props:classpath:alarm-ds.properties}" })
public class AlarmDataSourceConfig extends BaseDataSourceConfig {

    
    @Bean
    public AlarmDataSourceInterface alarmDataSourceInterface(){
        
        AlarmDataSourceInterface ret = new AlarmDataSourceImpl(getDataSource(), getKeyColumnConverter());
        return ret;
    }
    
    @Override
    public String getDataSourceName() {
        return "alarm-ds";
    }
}
