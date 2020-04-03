package com.telecominfraproject.wlan.location.datastore.rdbms;

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
@PropertySource({ "${location-info-ds.props:classpath:location-info-ds.properties}" })
public class LocationDataSourceConfig extends BaseDataSourceConfig {

    
    @Bean
    public LocationDataSourceInterface locationDataSourceInterface(){
        
        LocationDataSourceInterface ret = new LocationDataSourceImpl(getDataSource(), 
                getKeyColumnConverter());
        return ret;
    }
    
    @Override
    public String getDataSourceName() {
        return "location-info-ds";
    }
}
