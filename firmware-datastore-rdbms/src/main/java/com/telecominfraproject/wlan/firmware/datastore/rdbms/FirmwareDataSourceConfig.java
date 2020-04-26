package com.telecominfraproject.wlan.firmware.datastore.rdbms;

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
@PropertySource({ "${firmware-ds.props:classpath:firmware-ds.properties}" })
public class FirmwareDataSourceConfig extends BaseDataSourceConfig {

    
    @Bean
    public FirmwareDataSourceInterface firmwareDataSourceInterface(){
        
        FirmwareDataSourceInterface ret = new FirmwareDataSourceImpl(getDataSource(), getKeyColumnConverter());
        return ret;
    }
    
    @Override
    public String getDataSourceName() {
        return "firmware-ds";
    }
}
