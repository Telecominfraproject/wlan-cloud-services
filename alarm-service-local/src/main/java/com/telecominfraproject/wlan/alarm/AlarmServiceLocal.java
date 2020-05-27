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
import com.telecominfraproject.wlan.alarm.models.AlarmCode;

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
	public Alarm getOrNull(int customerId, long equipmentId, AlarmCode alarmCode, long createdTimestamp) {
		return alarmController.getOrNull(customerId, equipmentId, alarmCode, createdTimestamp);
	}


	@Override
	public List<Alarm> get(int customerId, Set<Long> equipmentIdSet, Set<AlarmCode> alarmCodeSet,
			long createdAfterTimestamp) {
		return alarmController.getAllForEquipment(customerId, equipmentIdSet, alarmCodeSet, createdAfterTimestamp);
	}


	@Override
	public Alarm update(Alarm alarm) {
		return alarmController.update(alarm);
	}


	@Override
	public Alarm delete(int customerId, long equipmentId, AlarmCode alarmCode, long createdTimestamp) {
		return alarmController.delete(customerId, equipmentId, alarmCode, createdTimestamp);
	}


	@Override
	public List<Alarm> delete(int customerId, long equipmentId) {
		return alarmController.deleteForEquipment(customerId, equipmentId);
	}


	@Override
	public PaginationResponse<Alarm> getForCustomer(int customerId, Set<Long> equipmentIdSet,
			Set<AlarmCode> alarmCodeSet, long createdAfterTimestamp, List<ColumnAndSort> sortBy,
			PaginationContext<Alarm> context) {
		
		return alarmController.getForCustomer(customerId, equipmentIdSet,
				alarmCodeSet, createdAfterTimestamp, sortBy, context);
	}

}
