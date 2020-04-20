package com.telecominfraproject.wlan.equipment.datastore.rdbms;

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
@PropertySource({ "${equipment-ds.props:classpath:equipment-ds.properties}" })
public class EquipmentDataSourceConfig extends BaseDataSourceConfig {

    
    @Bean
    public EquipmentDataSourceInterface equipmentDataSourceInterface(){
        
        EquipmentDataSourceInterface ret = new EquipmentDataSourceImpl(getDataSource(), getKeyColumnConverter());
        return ret;
    }
    
    @Override
    public String getDataSourceName() {
        return "equipment-ds";
    }
}
