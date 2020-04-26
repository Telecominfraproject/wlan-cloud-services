package com.telecominfraproject.wlan.manufacturer;

import java.util.List;
import java.util.Set;

import com.telecominfraproject.wlan.core.model.pagination.ColumnAndSort;
import com.telecominfraproject.wlan.core.model.pagination.PaginationContext;
import com.telecominfraproject.wlan.core.model.pagination.PaginationResponse;

import com.telecominfraproject.wlan.manufacturer.models.Manufacturer;


/**
 * @author dtoptygin
 *
 */
public interface ManufacturerServiceInterface {
    
    /**
     * Creates new Manufacturer
     *  
     * @param Manufacturer
     * @return stored Manufacturer object
     * @throws RuntimeException if Manufacturer record already exists
     */
    Manufacturer create(Manufacturer manufacturer );
    
    /**
     * Retrieves Manufacturer by id
     * @param manufacturerId
     * @return Manufacturer for the supplied id
     * @throws RuntimeException if Manufacturer record does not exist
     */
    Manufacturer get(long manufacturerId );

    /**
     * Retrieves Manufacturer by id
     * @param manufacturerId
     * @return Manufacturer for the supplied id
     */
    Manufacturer getOrNull(long manufacturerId );

    /**
     * Retrieves a list of Manufacturer records that which have their Id in the provided set.
     * 
     * @param manufacturerIdSet
     * @return list of matching Manufacturer objects.
     */
    List<Manufacturer> get(Set<Long> manufacturerIdSet);

    /**
     * Updates Manufacturer 
     * 
     * @param Manufacturer
     * @return updated Manufacturer object
     * @throws RuntimeException if Manufacturer record does not exist or if it was modified concurrently
     */
    Manufacturer update(Manufacturer manufacturer);
    
    /**
     * Deletes Manufacturer
     * 
     * @param manufacturerId
     * @return deleted Manufacturer object
     */
    Manufacturer delete(long manufacturerId );
    
    /**
     * <br>Retrieves all of the Manufacturer records that are mapped to the provided customerId.
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
     * @return next page of matching Manufacturer objects.
     */
    PaginationResponse<Manufacturer> getForCustomer(int customerId, List<ColumnAndSort> sortBy, PaginationContext<Manufacturer> context);

}
