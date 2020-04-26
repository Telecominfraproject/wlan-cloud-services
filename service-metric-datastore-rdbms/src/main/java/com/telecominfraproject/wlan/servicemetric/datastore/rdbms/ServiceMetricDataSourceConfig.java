package com.telecominfraproject.wlan.servicemetric.datastore.rdbms;

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
@PropertySource({ "${service-metric-ds.props:classpath:service-metric-ds.properties}" })
public class ServiceMetricDataSourceConfig extends BaseDataSourceConfig {

    
    @Bean
    public ServiceMetricDataSourceInterface serviceMetricDataSourceInterface(){
        
        ServiceMetricDataSourceInterface ret = new ServiceMetricDataSourceImpl(getDataSource(), getKeyColumnConverter());
        return ret;
    }
    
    @Override
    public String getDataSourceName() {
        return "service-metric-ds";
    }
}
