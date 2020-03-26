package com.telecominfraproject.wlan.customer.datastore.rdbms;

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
@PropertySource({ "${customer-info-ds.props:classpath:customer-info-ds.properties}" })
public class CustomerDataSourceConfig extends BaseDataSourceConfig {

    
    @Bean
    public CustomerDataSourceInterface customerInfoDataSourceInterface(){
        
        CustomerDataSourceInterface ret = new CustomerDataSourceImpl(getDataSource());
        return ret;
    }
    
    @Override
    public String getDataSourceName() {
        return "customer-info-ds";
    }

}
