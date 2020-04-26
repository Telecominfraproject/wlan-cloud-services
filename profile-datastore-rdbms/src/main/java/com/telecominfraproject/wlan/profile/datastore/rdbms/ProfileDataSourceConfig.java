package com.telecominfraproject.wlan.profile.datastore.rdbms;

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
@PropertySource({ "${profile-ds.props:classpath:profile-ds.properties}" })
public class ProfileDataSourceConfig extends BaseDataSourceConfig {

    
    @Bean
    public ProfileDataSourceInterface profileDataSourceInterface(){
        
        ProfileDataSourceInterface ret = new ProfileDataSourceImpl(getDataSource(), getKeyColumnConverter());
        return ret;
    }
    
    @Override
    public String getDataSourceName() {
        return "profile-ds";
    }
}
