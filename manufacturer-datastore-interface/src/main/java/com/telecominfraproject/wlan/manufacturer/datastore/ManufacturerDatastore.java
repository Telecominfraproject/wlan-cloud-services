package com.telecominfraproject.wlan.manufacturer.datastore;

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
public interface ManufacturerDatastore {

    Manufacturer create(Manufacturer manufacturer);
    Manufacturer get(long manufacturerId);
    Manufacturer getOrNull(long manufacturerId);
    Manufacturer update(Manufacturer manufacturer);
    Manufacturer delete(long manufacturerId);
    
    /**
     * Retrieves a list of Manufacturer records that which have their Id in the provided set.
     * 
     * @param manufacturerIdSet
     * @return list of matching Manufacturer objects.
     */
    List<Manufacturer> get(Set<Long> manufacturerIdSet);

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
