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
import com.telecominfraproject.wlan.alarm.models.AlarmCode;
import com.telecominfraproject.wlan.alarm.models.AlarmCounts;

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
	public Alarm getOrNull(int customerId, long equipmentId, AlarmCode alarmCode, long createdTimestamp) {
		return alarmDAO.getOrNull(customerId, equipmentId, alarmCode, createdTimestamp);
	}

	@Override
	public Alarm update(Alarm alarm) {
        return alarmDAO.update(alarm);
	}

	@Override
	public Alarm delete(int customerId, long equipmentId, AlarmCode alarmCode, long createdTimestamp) {
		return alarmDAO.delete(customerId, equipmentId, alarmCode, createdTimestamp);
	}

	@Override
	public List<Alarm> delete(int customerId, long equipmentId) {
		return alarmDAO.delete(customerId, equipmentId);
	}

	@Override
	public List<Alarm> get(int customerId, Set<Long> equipmentIdSet, Set<AlarmCode> alarmCodeSet,
			long createdAfterTimestamp) {
		return alarmDAO.get(customerId, equipmentIdSet, alarmCodeSet, createdAfterTimestamp);
	}

	@Override
	public PaginationResponse<Alarm> getForCustomer(int customerId, Set<Long> equipmentIdSet,
			Set<AlarmCode> alarmCodeSet, long createdAfterTimestamp, Boolean acknowledged, List<ColumnAndSort> sortBy,
			PaginationContext<Alarm> context) {
		
    	if(context == null) {
    		context = new PaginationContext<>();
    	}

		return alarmDAO.getForCustomer(customerId, equipmentIdSet,
				alarmCodeSet, createdAfterTimestamp, acknowledged, sortBy,
				context);
	}

	@Override
	public AlarmCounts getAlarmCounts(int customerId, Set<Long> equipmentIdSet, Set<AlarmCode> alarmCodeSet, Boolean acknowledged) {
		return alarmDAO.getAlarmCounts(customerId, equipmentIdSet, alarmCodeSet, acknowledged);
	}
}
