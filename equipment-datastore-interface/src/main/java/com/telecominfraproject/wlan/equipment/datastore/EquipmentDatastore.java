package com.telecominfraproject.wlan.equipment.datastore;

import java.util.List;
import java.util.Set;

import com.telecominfraproject.wlan.core.model.equipment.EquipmentType;
import com.telecominfraproject.wlan.core.model.pagination.ColumnAndSort;
import com.telecominfraproject.wlan.core.model.pagination.PaginationContext;
import com.telecominfraproject.wlan.core.model.pagination.PaginationResponse;
import com.telecominfraproject.wlan.core.model.pair.PairLongLong;
import com.telecominfraproject.wlan.equipment.models.CustomerEquipmentCounts;
import com.telecominfraproject.wlan.equipment.models.Equipment;
import com.telecominfraproject.wlan.equipment.models.bulkupdate.rrm.EquipmentRrmBulkUpdateRequest;

/**
 * @author dtoptygin
 *
 */
public interface EquipmentDatastore {

    Equipment create(Equipment equipment);
    Equipment get(long equipmentId);
    Equipment getOrNull(long equipmentId);
    Equipment update(Equipment equipment);
    Equipment delete(long equipmentId);
    
    /**
     * Retrieves Equipment by inventory id
     * @param inventory Id
     * @return Equipment for the supplied id
     */
    Equipment getByInventoryIdOrNull(String inventoryId );

    
    /**
     * Retrieves a list of Equipment records that which have their Id in the provided set.
     * 
     * @param equipmentIdSet
     * @return list of matching Equipment objects.
     */
    List<Equipment> get(Set<Long> equipmentIdSet);

    /**
     * <br>Retrieves all of the Equipment records that are mapped to the provided customerId.
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
	 *<li> "name"
     *<br> 
     * @param customerId
     * @return next page of matching Equipment objects.
     */
    PaginationResponse<Equipment> getForCustomer(int customerId, List<ColumnAndSort> sortBy, PaginationContext<Equipment> context);

    /**
     * Same as getForCustomer with more filters
     * @param customerId
     * @param equipmentType
     * @param locationIds
     * @param profileIds
     * @param criteria (nameSubstring)
     * @return next page of matching equipment objects
     */
    PaginationResponse<Equipment> getForCustomer(int customerId, EquipmentType equipmentType, Set<Long> locationIds, Set<Long> profileIds, String criteria, List<ColumnAndSort> sortBy, PaginationContext<Equipment> context);

    /**
     * Find equipment ids for the equipment that refers (directly or indirectly) to the specified set of profile ids.
     * 
     * @param profileIds
     * @param context
     * @return paginated list of pairs (profileId, equipmentId)
     */
    PaginationResponse<PairLongLong> getEquipmentIdsByProfileIds(Set<Long> profileIds, PaginationContext<PairLongLong> context);

    /**
     * Find equipment ids for the equipment that resides in the specified set of location ids.
     * 
     * @param locationIds
     * @param context
     * @return paginated list of pairs (locationId, equipmentId)
     */
    PaginationResponse<PairLongLong> getEquipmentIdsByLocationIds(Set<Long> locationIds, PaginationContext<PairLongLong> context);

    /**
     * Update RRM-related properties on AP details for the collection of equipment
     * @param request - bulk update request
     * 
     * This method and functionality should be replaced by the RF profile. 
     * See RfConfiguration and RfElementConfiguration
     */
    void updateRrmBulk(EquipmentRrmBulkUpdateRequest request);
    
    CustomerEquipmentCounts getEquipmentCounts(int customerId);

}
