package com.telecominfraproject.wlan.alarm;

import java.util.List;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.telecominfraproject.wlan.core.model.pagination.ColumnAndSort;
import com.telecominfraproject.wlan.core.model.pagination.PaginationContext;
import com.telecominfraproject.wlan.core.model.pagination.PaginationResponse;

import com.telecominfraproject.wlan.alarm.controller.AlarmController;
import com.telecominfraproject.wlan.alarm.models.Alarm;

/**
 * @author dtoptygin
 *
 */
@Component
public class AlarmServiceLocal implements AlarmServiceInterface {

    @Autowired private AlarmController alarmController;
    private static final Logger LOG = LoggerFactory.getLogger(AlarmServiceLocal.class);

    
    @Override
    public Alarm create(Alarm alarm) {
        LOG.debug("calling alarmController.create {} ", alarm);
        return alarmController.create(alarm);
    }

    @Override
    public Alarm get(long alarmId) {
        LOG.debug("calling alarmController.get {} ", alarmId);
        return alarmController.get(alarmId);
    }
    
    @Override
    public Alarm getOrNull(long alarmId) {
        LOG.debug("calling alarmController.getOrNull {} ", alarmId);
        return alarmController.getOrNull(alarmId);
    }
    
    @Override
    public List<Alarm> get(Set<Long> alarmIdSet) {
        LOG.debug("calling alarmController.getAllInSet {} ", alarmIdSet);
        return alarmController.getAllInSet(alarmIdSet);
    }
    
    @Override
    public PaginationResponse<Alarm> getForCustomer(int customerId, List<ColumnAndSort> sortBy,
    		PaginationContext<Alarm> context) {
        LOG.debug("calling alarmController.getForCustomer {} ", customerId);
        return alarmController.getForCustomer(customerId, sortBy, context);
    }

    @Override
    public Alarm update(Alarm alarm) {
        LOG.debug("calling alarmController.update {} ", alarm);
        return alarmController.update(alarm);
    }

    @Override
    public Alarm delete(long alarmId) {
        LOG.debug("calling alarmController.delete {} ", alarmId);
        return alarmController.delete(alarmId);
    }

}
