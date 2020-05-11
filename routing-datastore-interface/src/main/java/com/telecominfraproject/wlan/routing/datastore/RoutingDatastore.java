package com.telecominfraproject.wlan.routing.datastore;

import java.util.List;
import java.util.Set;

import com.telecominfraproject.wlan.core.model.pagination.ColumnAndSort;
import com.telecominfraproject.wlan.core.model.pagination.PaginationContext;
import com.telecominfraproject.wlan.core.model.pagination.PaginationResponse;
import com.telecominfraproject.wlan.core.model.service.GatewayType;
import com.telecominfraproject.wlan.routing.models.EquipmentGatewayRecord;
import com.telecominfraproject.wlan.routing.models.EquipmentRoutingRecord;

/**
 * @author dtoptygin
 *
 */
public interface RoutingDatastore {

    EquipmentRoutingRecord create(EquipmentRoutingRecord routing);
    EquipmentRoutingRecord get(long routingId);
    EquipmentRoutingRecord getOrNull(long routingId);
    EquipmentRoutingRecord update(EquipmentRoutingRecord routing);
    EquipmentRoutingRecord delete(long routingId);
    
    /**
     * Retrieves a list of EquipmentRoutingRecord records that which have their Id in the provided set.
     * 
     * @param routingIdSet
     * @return list of matching EquipmentRoutingRecord objects.
     */
    List<EquipmentRoutingRecord> get(Set<Long> routingIdSet);

    /**
     * <br>Retrieves all of the EquipmentRoutingRecord records that are mapped to the provided customerId.
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
	 *<li> "hostname"
     *<br> 
     * @param customerId
     * @return next page of matching EquipmentRoutingRecord objects.
     */
    PaginationResponse<EquipmentRoutingRecord> getForCustomer(int customerId, List<ColumnAndSort> sortBy, PaginationContext<EquipmentRoutingRecord> context);

    ///
    /**
     * Registers a new EquipmentGatewayRecord in the datastore.
     * 
     * @param EquipmentGatewayRecord
     * @return stored EquipmentGatewayRecord object
     * @throws RuntimeException
     *             if EquipmentGatewayRecord record already exists
     */
    EquipmentGatewayRecord registerGateway(EquipmentGatewayRecord equipmentGatewayRecord);

    /**
     * Retrieves EquipmentGatewayRecord by id
     * 
     * @param id
     * @return EquipmentGatewayRecord for the supplied id
     */
    EquipmentGatewayRecord getGateway(long id);

    /**
     * Retrieves EquipmentGatewayRecord by gatewayId
     * 
     * @param hostname
     * @return list of EquipmentGatewayRecord for the supplied hostname
     */
    List<EquipmentGatewayRecord> getGateway(String hostname);
    
    /**
     * Get all registered gateways of the given type
     * 
     * @param gatewayType
     * @return
     */
    List<EquipmentGatewayRecord> getGateway(GatewayType gatewayType);

    /**
     * Retrieves a list EquipmentRoutingRecords associated with the provided
     * Equipment id.
     * 
     * @param equipmentId
     * @return a list of EquipmentRoutingRecord objects associated with the
     *         supplied id.
     */
    List<EquipmentRoutingRecord> getRegisteredRouteList(long equipmentId);


    /**
     * Retrieves a list EquipmentGatewayRecord associated with the provided
     * Equipment id.
     * 
     * @param equipmentId
     * @return a list of EquipmentGatewayRecord objects associated with the
     *         supplied id.
     */
    List<EquipmentGatewayRecord> getRegisteredGatewayRecordList(long equipmentId);

    /**
     * Updates EquipmentGatewayRecord record
     * 
     * @param EquipmentGatewayRecord
     * @return updated EquipmentGatewayRecord object
     * @throws RuntimeException
     *             if EquipmentGatewayRecord record does not exist or if it
     *             was modified concurrently
     */
    EquipmentGatewayRecord updateGateway(EquipmentGatewayRecord equipmentGwRecord);

    /**
     * Deletes EquipmentGatewayRecord record
     * 
     * @param id
     * @return deleted EquipmentGatewayRecord object
     */
    EquipmentGatewayRecord deleteGateway(long id);

    /**
     * Deletes EquipmentGatewayRecord records associated with the gatewayId
     * 
     * @param hostname
     * @return deleted EquipmentGatewayRecord object associated with the
     *         hostname
     */
    List<EquipmentGatewayRecord> deleteGateway(String hostname);


}
