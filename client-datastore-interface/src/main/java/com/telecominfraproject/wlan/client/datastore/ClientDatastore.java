package com.telecominfraproject.wlan.client.datastore;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import com.telecominfraproject.wlan.client.info.models.ClientSessionCounts;
import com.telecominfraproject.wlan.client.models.Client;
import com.telecominfraproject.wlan.client.session.models.ClientSession;
import com.telecominfraproject.wlan.core.model.equipment.MacAddress;
import com.telecominfraproject.wlan.core.model.pagination.ColumnAndSort;
import com.telecominfraproject.wlan.core.model.pagination.PaginationContext;
import com.telecominfraproject.wlan.core.model.pagination.PaginationResponse;

/**
 * @author dtoptygin
 *
 */
public interface ClientDatastore {

    Client create(Client client);
    Client getOrNull(int customerId, MacAddress clientMac);
    Client update(Client client);
    Client delete(int customerId, MacAddress clientMac);
    void   delete(long createdBeforeTimestamp);
    
    /**
     * Retrieves a list of Client records that which have their mac address in the provided set.
     * 
     * @param clientIdSet
     * @return list of matching Client objects.
     */
    List<Client> get(int customerId, Set<MacAddress> clientMacSet);

    /**
     * <br>Retrieves all of the Client records that are mapped to the provided customerId and optional macSubstring filter.
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
     * @param macSubstring
     * @return next page of matching Client objects.
     */
    PaginationResponse<Client> getForCustomer(int customerId, String macSubstring, List<ColumnAndSort> sortBy, PaginationContext<Client> context);

    /**
     * @param customerId
     * @return list of Clients for the customer that are marked as blocked. This per-customer list of blocked clients is pushed to every AP, so it has to be limited in size. 
     */
    List<Client> getBlockedClients(int customerId);

    //
    // Client Session -related methods
    //
    
    ClientSession getSessionOrNull(int customerId, long equipmentId, MacAddress clientMac);
    ClientSession updateSession(ClientSession clientSession);
    
	default List<ClientSession> updateSessions(List<ClientSession> sessionList) {
		List<ClientSession> ret = new ArrayList<>();
		if (sessionList != null) {
			sessionList.forEach(s -> ret.add(updateSession(s)));
		}
		return ret;
	}

    
    ClientSession deleteSession(int customerId, long equipmentId, MacAddress clientMac);
    void deleteSessions(long createdBeforeTimestamp);
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
     * @param macSubstring - To match with MacAddress
	 *
     * @return next page of matching Client session objects.
     */
    PaginationResponse<ClientSession> getSessionsForCustomer(int customerId, Set<Long> equipmentIds, Set<Long> locationIds, String macSubstring, List<ColumnAndSort> sortBy, PaginationContext<ClientSession> context);

    /**
     * Get Client Session counts for the given customerId.
     * 
     * @param customerId
     * @return
     */
    ClientSessionCounts getSessionCounts(int customerId);
    
}
