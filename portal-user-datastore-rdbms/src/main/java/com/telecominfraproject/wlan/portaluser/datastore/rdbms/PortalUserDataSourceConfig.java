package com.telecominfraproject.wlan.portaluser.datastore.rdbms;

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
@PropertySource({ "${portal-user-ds.props:classpath:portal-user-ds.properties}" })
public class PortalUserDataSourceConfig extends BaseDataSourceConfig {

    
    @Bean
    public PortalUserDataSourceInterface portalUserDataSourceInterface(){
        
        PortalUserDataSourceInterface ret = new PortalUserDataSourceImpl(getDataSource(), getKeyColumnConverter());
        return ret;
    }
    
    @Override
    public String getDataSourceName() {
        return "portal-user-ds";
    }
}
