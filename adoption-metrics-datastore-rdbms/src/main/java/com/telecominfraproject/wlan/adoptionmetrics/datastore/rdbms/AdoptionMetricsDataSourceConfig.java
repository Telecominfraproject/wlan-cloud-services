package com.telecominfraproject.wlan.adoptionmetrics.datastore.rdbms;

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
@PropertySource({ "${adoption-metrics-ds.props:classpath:adoption-metrics-ds.properties}" })
public class AdoptionMetricsDataSourceConfig extends BaseDataSourceConfig {

    
    @Bean
    public AdoptionMetricsDataSourceInterface adoptionMetricsDataSourceInterface(){
        
        AdoptionMetricsDataSourceInterface ret = new AdoptionMetricsDataSourceImpl(getDataSource(), getKeyColumnConverter());
        return ret;
    }
    
    @Override
    public String getDataSourceName() {
        return "adoption-metrics-ds";
    }
}
