package com.telecominfraproject.wlan.client;

import java.util.List;
import java.util.Set;

import com.telecominfraproject.wlan.core.model.equipment.MacAddress;
import com.telecominfraproject.wlan.core.model.pagination.ColumnAndSort;
import com.telecominfraproject.wlan.core.model.pagination.PaginationContext;
import com.telecominfraproject.wlan.core.model.pagination.PaginationResponse;

import com.telecominfraproject.wlan.client.models.Client;
import com.telecominfraproject.wlan.client.session.models.ClientSession;


/**
 * @author dtoptygin
 *
 */
public interface ClientServiceInterface {
    
    /**
     * Creates new Client
     *  
     * @param Client
     * @return stored Client object
     * @throws RuntimeException if Client record already exists
     */
    Client create(Client client );
    
    /**
     * Retrieves Client by mac
     * @param customerId
     * @param clientMac
     * @return Client for the supplied customer and MAC
     */
    Client getOrNull(int customerId, MacAddress clientMac);
    
    /**
     * Retrieves a list of Client records that which have their macs in the provided set.
     * 
     * @param clientMacSet
     * @return list of matching Client objects.
     */
    List<Client> get(int customerId, Set<MacAddress> clientMacSet);

    /**
     * Updates Client 
     * 
     * @param Client
     * @return updated Client object
     * @throws RuntimeException if Client record does not exist or if it was modified concurrently
     */
    Client update(Client client);
    
    /**
     * Deletes Client
     * 
     * @param customerId
     * @param clientMac
     * @return deleted Client object
     */
    Client delete(int customerId, MacAddress clientMac );
    
    /**
     * <br>Retrieves all of the Client records that are mapped to the provided customerId.
     * Results are returned in pages.
     * 
     * <br>When changing sort order or filters, pagination should be restarted again from the first page. 
     * Old pagination contexts would be invalid and should not be used in that case. 
     * <br>The only time when a caller should be interacting with the properties of the paginationContext is during the 
     * call to the first page by setting property maxItemsPerPage. 
     * <br>If initial context is not provided, then the maxItemsPerPage will be set to 20.
     * <br>If sortBy is not provided, then the data will be ordered by id.
     * <ul>Allowed columns for sorting are: 
	 *<li>  "macAddress"
     *<br> 
     * @param customerId
     * @return next page of matching Client objects.
     */
    PaginationResponse<Client> getForCustomer(int customerId, List<ColumnAndSort> sortBy, PaginationContext<Client> context);

    /**
     * @param customerId
     * @return list of Clients for the customer that are marked as blocked 
     */
    //List<Client> getBlockedClients(int customerId);

    //
    // Client Session -related methods
    //
    
    ClientSession getSessionOrNull(int customerId, long equipmentId, MacAddress clientMac);
    ClientSession updateSession(ClientSession clientSession);
    List<ClientSession> updateSessions(List<ClientSession> clientSession);

    ClientSession deleteSession(int customerId, long equipmentId, MacAddress clientMac);
    
    /**
     * Retrieves a list of Client sessions that which have their mac address in the provided set.
     * 
     * @param clientMacSet
     * @return list of matching Client session objects.
     */
    List<ClientSession> getSessions(int customerId, Set<MacAddress> clientMacSet);

    /**
     * <br>Retrieves all of the Client sessions that are mapped to the provided customerId.
     * Results are returned in pages.
     * 
     * <br>When changing sort order or filters, pagination should be restarted again from the first page. 
     * Old pagination contexts would be invalid and should not be used in that case. 
     * <br>The only time when a caller should be interacting with the properties of the paginationContext is during the 
     * call to the first page by setting property maxItemsPerPage. 
     * <br>If initial context is not provided, then the maxItemsPerPage will be set to 20.
     * <br>If sortBy is not provided, then the data will be ordered by id.
     * <ul>Allowed columns for sorting are: 
	 *<li>  "macAddress"
	 *<li>  "equipmentId"
     *<br> 
     * @param customerId
     * @param equipmentIds - set of equipment ids for which to retrieve session objects. Empty set or null means retrieve for all customer's equipment.
     * @param locationIds - set of location ids for which to retrieve session objects. Empty set or null means retrieve for all customer's locations. 
	 *
     * @return next page of matching Client session objects.
     */
     PaginationResponse<ClientSession> getSessionsForCustomer(int customerId, Set<Long> equipmentIds, Set<Long> locationIds, List<ColumnAndSort> sortBy, PaginationContext<ClientSession> context);

}
