package com.telecominfraproject.wlan.alarm.datastore.rdbms;

import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

import com.telecominfraproject.wlan.core.model.pagination.ColumnAndSort;
import com.telecominfraproject.wlan.core.model.pagination.PaginationContext;
import com.telecominfraproject.wlan.core.model.pagination.PaginationResponse;

import com.telecominfraproject.wlan.alarm.datastore.AlarmDatastore;
import com.telecominfraproject.wlan.alarm.models.Alarm;

/**
 * @author dtoptygin
 *
 */
@Configuration
public class AlarmDatastoreRdbms implements AlarmDatastore {

    @Autowired AlarmDAO alarmDAO;

    @Override
    public Alarm create(Alarm alarm) {
        return alarmDAO.create(alarm);
    }

    @Override
    public Alarm get(long alarmId) {
        return alarmDAO.get(alarmId);
    }

    @Override
    public Alarm getOrNull(long alarmId) {
        return alarmDAO.getOrNull(alarmId);
    }
    
    @Override
    public Alarm update(Alarm alarm) {
        return alarmDAO.update(alarm);
    }

    @Override
    public Alarm delete(long alarmId) {
        return alarmDAO.delete(alarmId);
    }
    
    @Override
    public List<Alarm> get(Set<Long> alarmIdSet) {
    	return alarmDAO.get(alarmIdSet);
    }
    
    @Override
    public PaginationResponse<Alarm> getForCustomer(int customerId, List<ColumnAndSort> sortBy,
    		PaginationContext<Alarm> context) {
    	return alarmDAO.getForCustomer( customerId, sortBy, context);
    }
}
