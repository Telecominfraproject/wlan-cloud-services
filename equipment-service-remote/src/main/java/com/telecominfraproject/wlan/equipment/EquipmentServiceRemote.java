package com.telecominfraproject.wlan.equipment;

import java.util.Collections;
import java.util.List;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import com.telecominfraproject.wlan.core.client.BaseRemoteClient;
import com.telecominfraproject.wlan.core.model.equipment.EquipmentType;
import com.telecominfraproject.wlan.core.model.json.BaseJsonModel;
import com.telecominfraproject.wlan.core.model.json.GenericResponse;
import com.telecominfraproject.wlan.core.model.pagination.ColumnAndSort;
import com.telecominfraproject.wlan.core.model.pagination.PaginationContext;
import com.telecominfraproject.wlan.core.model.pagination.PaginationResponse;
import com.telecominfraproject.wlan.core.model.pair.PairLongLong;
import com.telecominfraproject.wlan.datastore.exceptions.DsDataValidationException;
import com.telecominfraproject.wlan.equipment.models.CustomerEquipmentCounts;
import com.telecominfraproject.wlan.equipment.models.Equipment;
import com.telecominfraproject.wlan.equipment.models.EquipmentDetails;
import com.telecominfraproject.wlan.equipment.models.bulkupdate.rrm.EquipmentRrmBulkUpdateRequest;

/**
 * @author dtoptygin
 *
 */
@Component
public class EquipmentServiceRemote extends BaseRemoteClient implements EquipmentServiceInterface {

    private static final Logger LOG = LoggerFactory.getLogger(EquipmentServiceRemote.class);
    
    private static final ParameterizedTypeReference<List<Equipment>> Equipment_LIST_CLASS_TOKEN = new ParameterizedTypeReference<List<Equipment>>() {};

    private static final ParameterizedTypeReference<PaginationResponse<PairLongLong>> PairLongLong_PaginationResponse_CLASS_TOKEN = new ParameterizedTypeReference<PaginationResponse<PairLongLong>>() {};

    private static final ParameterizedTypeReference<PaginationResponse<Equipment>> Equipment_PAGINATION_RESPONSE_CLASS_TOKEN = new ParameterizedTypeReference<PaginationResponse<Equipment>>() {};


    private String baseUrl;
            
    @Override
    public Equipment create(Equipment equipment) {
        
        LOG.debug("calling equipment.create {} ", equipment);

        if (BaseJsonModel.hasUnsupportedValue(equipment)) {
            LOG.error("Failed to create Equipment, unsupported value in {}", equipment);
            throw new DsDataValidationException("Equipment contains unsupported value");
        }

        HttpEntity<String> request = new HttpEntity<String>( equipment.toString(), headers );

        ResponseEntity<Equipment> responseEntity = restTemplate.postForEntity(
                getBaseUrl(),
                request, Equipment.class);
        
        Equipment ret = responseEntity.getBody();
        
        LOG.debug("completed equipment.create {} ", ret);
        
        return ret;
    }

    @Override
    public Equipment get(long equipmentId) {
        
        LOG.debug("calling equipment.get {} ", equipmentId);

        ResponseEntity<Equipment> responseEntity = restTemplate.getForEntity(
                getBaseUrl()
                +"?equipmentId={equipmentId}",
                Equipment.class, equipmentId);
        
        Equipment ret = responseEntity.getBody();
        
        LOG.debug("completed equipment.get {} ", ret);
        
        return ret;
    }

    @Override
    public Equipment getOrNull(long equipmentId) {
        
        LOG.debug("calling equipment.getOrNull {} ", equipmentId);

        ResponseEntity<Equipment> responseEntity = restTemplate.getForEntity(
                getBaseUrl()
                +"/orNull?equipmentId={equipmentId}",
                Equipment.class, equipmentId);
        
        Equipment ret = responseEntity.getBody();
        
        LOG.debug("completed equipment.getOrNull {} ", ret);
        
        return ret;
    }
    
    @Override
    public Equipment getByInventoryIdOrNull(String inventoryId) {
        LOG.debug("calling getByInventoryIdOrNull {} ", inventoryId);

        ResponseEntity<Equipment> responseEntity = restTemplate.getForEntity(
                getBaseUrl()
                +"/byInventoryIdOrNull?inventoryId={inventoryId}",
                Equipment.class, inventoryId);
        
        Equipment ret = responseEntity.getBody();
        
        LOG.debug("completed getByInventoryIdOrNull {} ", ret);
        
        return ret;
    }

    @Override
    public EquipmentDetails getDefaultEquipmentDetails(EquipmentType equipmentType) {
        
        LOG.debug("calling equipment.getDefaultEquipmentDetails {} ", equipmentType);

        ResponseEntity<EquipmentDetails> responseEntity = restTemplate.getForEntity(
                getBaseUrl()
                +"/defaultDetails?equipmentType={equipmentType}",
                EquipmentDetails.class, equipmentType);
        
        EquipmentDetails ret = responseEntity.getBody();
        
        LOG.debug("completed equipment.getDefaultEquipmentDetails {} ", ret);
        
        return ret;
    }
    
	@Override
	public List<Equipment> get(Set<Long> equipmentIdSet) {
		
        LOG.debug("get({})", equipmentIdSet);

        if (equipmentIdSet == null || equipmentIdSet.isEmpty()) {
            return Collections.emptyList();
        }

        String setString = equipmentIdSet.toString().substring(1, equipmentIdSet.toString().length() - 1);
        
        try {
            ResponseEntity<List<Equipment>> responseEntity = restTemplate.exchange(
                    getBaseUrl() + "/inSet?equipmentIdSet={equipmentIdSet}", HttpMethod.GET,
                    null, Equipment_LIST_CLASS_TOKEN, setString);

            List<Equipment> result = responseEntity.getBody();
            if (null == result) {
                result = Collections.emptyList();
            }
            LOG.debug("get({}) return {} entries", equipmentIdSet, result.size());
            return result;
        } catch (Exception exp) {
            LOG.error("getAllInSet({}) exception ", equipmentIdSet, exp);
            throw exp;
        }

	}

	@Override
	public PaginationResponse<Equipment> getForCustomer(int customerId, List<ColumnAndSort> sortBy,
			PaginationContext<Equipment> context) {
		
        LOG.debug("calling getForCustomer( {}, {}, {} )", customerId, sortBy, context);

        ResponseEntity<PaginationResponse<Equipment>> responseEntity = restTemplate.exchange(
                getBaseUrl()
                        + "/forCustomer?customerId={customerId}&sortBy={sortBy}&paginationContext={context}",
                HttpMethod.GET, null, Equipment_PAGINATION_RESPONSE_CLASS_TOKEN, customerId, sortBy, context);

        PaginationResponse<Equipment> ret = responseEntity.getBody();
        LOG.debug("completed getForCustomer {} ", ret.getItems().size());

        return ret;
	}

	@Override
	public PaginationResponse<Equipment> getForCustomer(int customerId, EquipmentType equipmentType,
			Set<Long> locationIds, List<ColumnAndSort> sortBy, PaginationContext<Equipment> context) {
		
        LOG.debug("calling getForCustomer( {}, {}, {}, {}, {} )", customerId, equipmentType, locationIds,
                sortBy, context);

        String locationIdsStr = null;
        if (locationIds != null && !locationIds.isEmpty()) {
            locationIdsStr = locationIds.toString();
            // remove [] around the string, otherwise wil get:
            // Failed to convert value of type 'java.lang.String' to required
            // type 'java.util.Set'; nested exception is
            // java.lang.NumberFormatException: For input string: "[690]"
            locationIdsStr = locationIdsStr.substring(1, locationIdsStr.length() - 1);
        }

        ResponseEntity<PaginationResponse<Equipment>> responseEntity = restTemplate.exchange(
                getBaseUrl()
                        + "/forCustomerWithFilter?customerId={customerId}&equipmentType={equipmentType}&locationIds={locationIdsStr}&sortBy={sortBy}&paginationContext={context}",
                HttpMethod.GET, null, Equipment_PAGINATION_RESPONSE_CLASS_TOKEN, customerId, equipmentType,
                locationIdsStr, sortBy, context);

        PaginationResponse<Equipment> ret = responseEntity.getBody();
        LOG.debug("completed getForCustomer {} ", ret.getItems().size());

        return ret;
	}
	
	@Override
	public PaginationResponse<Equipment> searchByMacAndName(int customerId, String criteria,
			List<ColumnAndSort> sortBy, PaginationContext<Equipment> context) {
		
        LOG.debug("calling searchByMacAndName( {}, {}, {}, {} )", customerId, criteria,
                sortBy, context);

        ResponseEntity<PaginationResponse<Equipment>> responseEntity = restTemplate.exchange(
                getBaseUrl()
                        + "/searchByMacAndName?customerId={customerId}&criteria={criteria}&sortBy={sortBy}&paginationContext={context}",
                HttpMethod.GET, null, Equipment_PAGINATION_RESPONSE_CLASS_TOKEN, customerId, criteria, sortBy, context);

        PaginationResponse<Equipment> ret = responseEntity.getBody();
        LOG.debug("completed searchByMacAndName {} ", ret.getItems().size());

        return ret;
	}
	
    @Override
    public Equipment update(Equipment equipment) {
        
        LOG.debug("calling equipment.update {} ", equipment);

        if (BaseJsonModel.hasUnsupportedValue(equipment)) {
            LOG.error("Failed to update Equipment, unsupported value in  {}", equipment);
            throw new DsDataValidationException("Equipment contains unsupported value");
        }
        
        HttpEntity<String> request = new HttpEntity<String>( equipment.toString(), headers );

        ResponseEntity<Equipment> responseEntity = restTemplate.exchange(
                getBaseUrl(),
                HttpMethod.PUT, request, Equipment.class);
        
        Equipment ret = responseEntity.getBody();
        
        LOG.debug("completed equipment.update {} ", ret);
        
        return ret;
    }

    @Override
    public GenericResponse updateRrmBulk(EquipmentRrmBulkUpdateRequest bulkRequest) {
        
        LOG.debug("calling equipment.updateRrmBulk {} ", bulkRequest);

        HttpEntity<String> request = new HttpEntity<String>( bulkRequest.toString(), headers );

        ResponseEntity<GenericResponse> responseEntity = restTemplate.exchange(
                getBaseUrl() + "/rrmBulk",
                HttpMethod.PUT, request, GenericResponse.class);
        
        GenericResponse ret = responseEntity.getBody();
        
        LOG.debug("completed equipment.updateRrmBulk {} ", ret);
        
        return ret;
    }


    @Override
    public Equipment delete(long equipmentId) {
        
        LOG.debug("calling equipment.delete {} ", equipmentId);

        ResponseEntity<Equipment> responseEntity =  restTemplate.exchange(
                getBaseUrl()
                +"?equipmentId={equipmentId}", HttpMethod.DELETE,
                null, Equipment.class, equipmentId);
        
        Equipment ret = responseEntity.getBody();
        LOG.debug("completed equipment.delete {} ", ret);
        
        return ret;
    }    

    @Override
    public PaginationResponse<PairLongLong> getEquipmentIdsByProfileIds(Set<Long> profileIdSet,
    		PaginationContext<PairLongLong> context) {

        LOG.debug("calling getEquipmentIdsByProfileIds( {}, {} )", profileIdSet, context);

        if (profileIdSet == null || profileIdSet.isEmpty()) {
            throw new IllegalArgumentException("getEquipmentIdsByProfileIds - profileIds must be provided");
        }

        String setString = profileIdSet.toString().substring(1, profileIdSet.toString().length() - 1);
        
        try {
            ResponseEntity<PaginationResponse<PairLongLong>> responseEntity = restTemplate.exchange(
                    getBaseUrl() + "/equipmentIdsByProfileIds?profileIds={profileIds}&paginationContext={context}", HttpMethod.GET,
                    null, PairLongLong_PaginationResponse_CLASS_TOKEN, setString, context);

            PaginationResponse<PairLongLong> result = responseEntity.getBody();
            LOG.debug("getEquipmentIdsByProfileIds({},{}) return {} entries", profileIdSet, context, result.getItems().size());
            return result;
        } catch (Exception exp) {
            LOG.error("getEquipmentIdsByProfileIds({},{}) exception ", profileIdSet, context, exp);
            throw exp;
        }

    }
    
    @Override
    public PaginationResponse<PairLongLong> getEquipmentIdsByLocationIds(Set<Long> locationIdSet,
    		PaginationContext<PairLongLong> context) {

        LOG.debug("calling getEquipmentIdsByLocationIds( {}, {} )", locationIdSet, context);

        if (locationIdSet == null || locationIdSet.isEmpty()) {
            throw new IllegalArgumentException("getEquipmentIdsByLocationIds - locationIds must be provided");
        }

        String setString = locationIdSet.toString().substring(1, locationIdSet.toString().length() - 1);
        
        try {
            ResponseEntity<PaginationResponse<PairLongLong>> responseEntity = restTemplate.exchange(
                    getBaseUrl() + "/equipmentIdsByLocationIds?locationIds={locationIds}&paginationContext={context}", HttpMethod.GET,
                    null, PairLongLong_PaginationResponse_CLASS_TOKEN, setString, context);

            PaginationResponse<PairLongLong> result = responseEntity.getBody();
            LOG.debug("getEquipmentIdsByLocationIds({},{}) return {} entries", locationIdSet, context, result.getItems().size());
            return result;
        } catch (Exception exp) {
            LOG.error("getEquipmentIdsByLocationIds({},{}) exception ", locationIdSet, context, exp);
            throw exp;
        }

    }

    @Override
    public CustomerEquipmentCounts getEquipmentCounts(int customerId) {
        LOG.debug("calling getEquipmentCounts( {} )", customerId);

        try {
            ResponseEntity<CustomerEquipmentCounts> responseEntity = restTemplate.exchange(
                    getBaseUrl() + "/countsForCustomer?customerId={customerId}", HttpMethod.GET,
                    null, CustomerEquipmentCounts.class, customerId);

            CustomerEquipmentCounts result = responseEntity.getBody();
            LOG.debug("getEquipmentCounts({}) returns {} ", customerId, result);
            return result;
        } catch (Exception exp) {
            LOG.error("getEquipmentCounts({}) exception ", customerId, exp);
            throw exp;
        }

    }
    
    public String getBaseUrl() {
        if(baseUrl==null) {
            baseUrl = environment.getProperty("tip.wlan.equipmentServiceBaseUrl").trim()+"/api/equipment";
        }

    	return baseUrl;
    }


}
