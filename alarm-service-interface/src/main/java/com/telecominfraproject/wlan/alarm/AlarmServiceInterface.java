package com.telecominfraproject.wlan.alarm;

import java.util.List;
import java.util.Set;

import com.telecominfraproject.wlan.core.model.pagination.ColumnAndSort;
import com.telecominfraproject.wlan.core.model.pagination.PaginationContext;
import com.telecominfraproject.wlan.core.model.pagination.PaginationResponse;

import com.telecominfraproject.wlan.alarm.models.Alarm;


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
     * @param alarmId
     * @return Alarm for the supplied id
     * @throws RuntimeException if Alarm record does not exist
     */
    Alarm get(long alarmId );

    /**
     * Retrieves Alarm by id
     * @param alarmId
     * @return Alarm for the supplied id
     */
    Alarm getOrNull(long alarmId );

    /**
     * Retrieves a list of Alarm records that which have their Id in the provided set.
     * 
     * @param alarmIdSet
     * @return list of matching Alarm objects.
     */
    List<Alarm> get(Set<Long> alarmIdSet);

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
     * @param alarmId
     * @return deleted Alarm object
     */
    Alarm delete(long alarmId );
    
    /**
     * <br>Retrieves all of the Alarm records that are mapped to the provided customerId.
     * Results are returned in pages.
     * 
     * <br>When changing sort order or filters, pagination should be restarted again from the first page. 
     * Old pagination contexts would be invalid and should not be used in that case. 
     * <br>The only time when a caller should be interacting with the properties of the paginationContext is during the 
     * call to the first page by setting property maxItemsPerPage. 
     * <br>If initial context is not provided, then the maxItemsPerPage will be set to 20.
     * <br>If sortBy is not provided, then the data will be ordered by id.
     * <ul>Allowed columns for sorting are: 
	 *<li>  "id"
	 *<li> "sampleStr"
     *<br> 
     * @param customerId
     * @return next page of matching Alarm objects.
     */
    PaginationResponse<Alarm> getForCustomer(int customerId, List<ColumnAndSort> sortBy, PaginationContext<Alarm> context);

}
