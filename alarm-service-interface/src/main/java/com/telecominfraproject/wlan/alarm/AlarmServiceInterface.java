package com.telecominfraproject.wlan.alarm;

import java.util.List;
import java.util.Set;

import com.telecominfraproject.wlan.core.model.json.GenericResponse;
import com.telecominfraproject.wlan.core.model.pagination.ColumnAndSort;
import com.telecominfraproject.wlan.core.model.pagination.PaginationContext;
import com.telecominfraproject.wlan.core.model.pagination.PaginationResponse;

import com.telecominfraproject.wlan.alarm.models.Alarm;
import com.telecominfraproject.wlan.alarm.models.AlarmCode;
import com.telecominfraproject.wlan.alarm.models.AlarmCounts;


/**
 * @author dtoptygin
 *
 */
public interface AlarmServiceInterface {
    
    /**
     * Creates new Alarm
     *  
     * @param Alarm
     * @return stored Alarm object
     * @throws RuntimeException if Alarm record already exists
     */
    Alarm create(Alarm alarm );
    
    /**
     * Retrieves Alarm by id
     * @param customerId
     * @param equipmentId
     * @param alarmCode
     * @param createdTimestamp
     * @return Alarm for the supplied id
     */
    Alarm getOrNull(int customerId, long equipmentId, AlarmCode alarmCode, long createdTimestamp);

    /**
     * Retrieves a list of Alarm records for the given equipment Ids and alarm codes.
     * 
     * @param customerId
     * @param equipmentIdSet - must not be null or empty
     * @param alarmCodeSet - null or empty means include all alarm codes
     * @param createdAfterTimestamp
     * @return list of matching Alarm objects.
     * @throws IllegalArgumentException if supplied equipmentIdSet is null or empty
     */
    List<Alarm> get(int customerId, Set<Long> equipmentIdSet, Set<AlarmCode> alarmCodeSet, long createdAfterTimestamp);

    default List<Alarm> get(int customerId, Set<Long> equipmentIdSet, Set<AlarmCode> alarmCodeSet){
    	return get(customerId, equipmentIdSet, alarmCodeSet, -1);
    }

    /**
     * Updates Alarm 
     * 
     * @param Alarm
     * @return updated Alarm object
     * @throws RuntimeException if Alarm record does not exist or if it was modified concurrently
     */
    Alarm update(Alarm alarm);
    
    /**
     * Deletes Alarm
     * 
     * @param customerId
     * @param equipmentId
     * @param alarmCode
     * @param createdTimestamp
     * @return deleted Alarm object
     */
    Alarm delete(int customerId, long equipmentId, AlarmCode alarmCode, long createdTimestamp);

    /**
     * Deletes Alarm
     * 
     * @param customerId
     * @param equipmentId
     * @return deleted Alarm object
     */
    List<Alarm> delete(int customerId, long equipmentId);

    /**
     * <br>Retrieves all of the Alarm records that are mapped to the provided customerId,  equipment Ids and alarm codes.
     * Results are returned in pages.
     * 
     * <br>When changing sort order or filters, pagination should be restarted again from the first page. 
     * Old pagination contexts would be invalid and should not be used in that case. 
     * <br>The only time when a caller should be interacting with the properties of the paginationContext is during the 
     * call to the first page by setting property maxItemsPerPage. 
     * <br>If initial context is not provided, then the maxItemsPerPage will be set to 20.
     * <br>If sortBy is not provided, then the data will be ordered by equipmentId and createdTimestamp.
     * <ul>Allowed columns for sorting are: 
	 *<li>  "equipmentId"
	 *<li>  "alarmCode"
	 *<li>  "createdTimestamp"
     *<br> 
     * @param customerId
     * @param equipmentIdSet - null or empty means include all equipment
     * @param alarmCodeSet - null or empty means include all alarm codes
     * @param createdAfterTimestamp
     * @return next page of matching Alarm objects.
     */
    PaginationResponse<Alarm> getForCustomer(int customerId, Set<Long> equipmentIdSet, Set<AlarmCode> alarmCodeSet, long createdAfterTimestamp, List<ColumnAndSort> sortBy, PaginationContext<Alarm> context);

    /**
     * @param customerId
     * @param equipmentIdSet - if empty, then only total counts of all alarms for customer per alarm code will be counted
     * @param alarmCodeSet - can be empty, in which case all alarm codes will be counted
     * @return alarm counts for the given filters
     */
    AlarmCounts getAlarmCounts(int customerId, Set<Long> equipmentIdSet, Set<AlarmCode> alarmCodeSet);

    /**
     * In some datastores (i.e. Cassandra) it makes sense to store counts of alarms in a separate table for fast retrieval by UI.
     * Unfortunately, there are some situations where those counters get out of sync with the real alarm counts.
     * This method is here to re-calculate the counter values and bring them in-sync with the real alarms.
     */
    GenericResponse resetAlarmCounters() ;
    
}
