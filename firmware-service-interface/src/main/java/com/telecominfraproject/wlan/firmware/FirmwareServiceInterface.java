package com.telecominfraproject.wlan.firmware;

import java.util.List;
import java.util.Set;

import com.telecominfraproject.wlan.core.model.pagination.ColumnAndSort;
import com.telecominfraproject.wlan.core.model.pagination.PaginationContext;
import com.telecominfraproject.wlan.core.model.pagination.PaginationResponse;

import com.telecominfraproject.wlan.firmware.models.Firmware;


/**
 * @author dtoptygin
 *
 */
public interface FirmwareServiceInterface {
    
    /**
     * Creates new Firmware
     *  
     * @param Firmware
     * @return stored Firmware object
     * @throws RuntimeException if Firmware record already exists
     */
    Firmware create(Firmware firmware );
    
    /**
     * Retrieves Firmware by id
     * @param firmwareId
     * @return Firmware for the supplied id
     * @throws RuntimeException if Firmware record does not exist
     */
    Firmware get(long firmwareId );

    /**
     * Retrieves Firmware by id
     * @param firmwareId
     * @return Firmware for the supplied id
     */
    Firmware getOrNull(long firmwareId );

    /**
     * Retrieves a list of Firmware records that which have their Id in the provided set.
     * 
     * @param firmwareIdSet
     * @return list of matching Firmware objects.
     */
    List<Firmware> get(Set<Long> firmwareIdSet);

    /**
     * Updates Firmware 
     * 
     * @param Firmware
     * @return updated Firmware object
     * @throws RuntimeException if Firmware record does not exist or if it was modified concurrently
     */
    Firmware update(Firmware firmware);
    
    /**
     * Deletes Firmware
     * 
     * @param firmwareId
     * @return deleted Firmware object
     */
    Firmware delete(long firmwareId );
    
    /**
     * <br>Retrieves all of the Firmware records that are mapped to the provided customerId.
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
     * @return next page of matching Firmware objects.
     */
    PaginationResponse<Firmware> getForCustomer(int customerId, List<ColumnAndSort> sortBy, PaginationContext<Firmware> context);

}