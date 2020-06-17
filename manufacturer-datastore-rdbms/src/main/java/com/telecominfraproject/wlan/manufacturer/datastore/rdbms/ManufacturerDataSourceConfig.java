package com.telecominfraproject.wlan.manufacturer.datastore.rdbms;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Profile;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

import com.telecominfraproject.wlan.core.server.jdbc.BaseDataSourceConfig;

/**
 * @author mpreston
 *
 */
@Component
@Profile("!use_single_ds")
@PropertySource({ "${manufacturer-ds.props:classpath:manufacturer-ds.properties}" })
public class ManufacturerDataSourceConfig extends BaseDataSourceConfig {

    
    @Bean
    public ManufacturerDataSourceInterface manufacturerDataSourceInterface(){
        
        ManufacturerDataSourceInterface ret = new ManufacturerDataSourceImpl(getDataSource(),
                getKeyColumnConverter());
        return ret;
    }
    
    @Override
    public String getDataSourceName() {
        return "manufacturer-ds";
    }
}
