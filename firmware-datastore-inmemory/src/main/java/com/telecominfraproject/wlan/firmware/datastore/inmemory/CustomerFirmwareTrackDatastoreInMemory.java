package com.telecominfraproject.wlan.firmware.datastore.inmemory;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.telecominfraproject.wlan.datastore.exceptions.DsConcurrentModificationException;
import com.telecominfraproject.wlan.datastore.exceptions.DsDuplicateEntityException;
import com.telecominfraproject.wlan.datastore.exceptions.DsEntityNotFoundException;
import com.telecominfraproject.wlan.datastore.inmemory.BaseInMemoryDatastore;
import com.telecominfraproject.wlan.firmware.datastore.CustomerFirmwareTrackDaoInterface;
import com.telecominfraproject.wlan.firmware.models.CustomerFirmwareTrackRecord;

/**
 * @author dtoptygin
 *
 */
@Component
public class CustomerFirmwareTrackDatastoreInMemory extends BaseInMemoryDatastore implements CustomerFirmwareTrackDaoInterface {

    private static final Logger LOG = LoggerFactory.getLogger(CustomerFirmwareTrackDatastoreInMemory.class);

    private final Map<Long, ConcurrentHashMap<Integer, CustomerFirmwareTrackRecord>> trackIdToCustomerSettingsMap = new ConcurrentHashMap<>();

	@Autowired 
	private FirmwareTrackDatastoreInMemory firmwareTrackDatastore;

	@Override
    public CustomerFirmwareTrackRecord createCustomerFirmwareTrackRecord(CustomerFirmwareTrackRecord record) {
		firmwareTrackDatastore.getFirmwareTrackById(record.getTrackRecordId());
        CustomerFirmwareTrackRecord existing = getCustomerFirmwareTrackRecord(record.getCustomerId());
        if (null != existing) {
            throw new DsDuplicateEntityException("Unique constraint violation");
        }

        CustomerFirmwareTrackRecord copy = record.clone();
        copy.setLastModifiedTimestamp(System.currentTimeMillis());
        ConcurrentHashMap<Integer, CustomerFirmwareTrackRecord> entriesForTrackId = trackIdToCustomerSettingsMap
                .get(record.getTrackRecordId());
        if (entriesForTrackId == null) {
            entriesForTrackId = new ConcurrentHashMap<>();
            trackIdToCustomerSettingsMap.put(record.getTrackRecordId(), entriesForTrackId);
        }
        entriesForTrackId.put(copy.getCustomerId(), copy);
        return copy.clone();
    }

    @Override
    public CustomerFirmwareTrackRecord updateCustomerFirmwareTrackRecord(CustomerFirmwareTrackRecord record) {
        CustomerFirmwareTrackRecord existing = getCustomerFirmwareTrackRecord(record.getCustomerId());

        if (existing == null) {
            throw new DsEntityNotFoundException();
        }
        if (existing.getLastModifiedTimestamp() != record.getLastModifiedTimestamp()) {
            LOG.debug(
                    "Concurrent modification detected for CustomerFirmwareTrackRecord with customerId {} expected version is {} but version in db was {}",
                    record.getCustomerId(), existing.getLastModifiedTimestamp());
            throw new DsConcurrentModificationException(
                    "Concurrent modification detected for CustomerFirmwareTrackRecord with customerId "
                            + record.getCustomerId() + " expected version is " + record.getLastModifiedTimestamp()
                            + " but version in db was " + existing.getLastModifiedTimestamp());

        }

        CustomerFirmwareTrackRecord copy = record.clone();
        copy.setLastModifiedTimestamp(getNewLastModTs(record.getLastModifiedTimestamp()));
        if (existing.getTrackRecordId() != record.getTrackRecordId()) {
            trackIdToCustomerSettingsMap.get(existing.getTrackRecordId()).remove(record.getCustomerId());
        }
        trackIdToCustomerSettingsMap.get(record.getTrackRecordId()).put(copy.getCustomerId(), copy);

        LOG.debug("Updated CustomerFirmwareTrackRecord {}", copy);

        return copy.clone();
    }

    @Override
    public CustomerFirmwareTrackRecord getCustomerFirmwareTrackRecord(int customerId) {
        LOG.debug("Looking up CustomerFirmwareTrackRecord for customerId", customerId);

        CustomerFirmwareTrackRecord found = null;
        for (ConcurrentHashMap<Integer, CustomerFirmwareTrackRecord> entrySet : trackIdToCustomerSettingsMap.values()) {
            found = entrySet.get(customerId);
            if (null != found) {
                break;
            }
        }

        CustomerFirmwareTrackRecord ret = null;
        if (found == null) {
            LOG.debug("Cannot find CustomerFirmwareTrackRecord for customerId", customerId);
        } else {
            LOG.debug("Found CustomerFirmwareTrackRecord {}", found);
            ret = found.clone();
        }

        return ret;
    }

    @Override
    public CustomerFirmwareTrackRecord deleteCustomerFirmwareTrackRecord(int customerId) {
        CustomerFirmwareTrackRecord record = getCustomerFirmwareTrackRecord(customerId);
        for (ConcurrentHashMap<Integer, CustomerFirmwareTrackRecord> entrySet : trackIdToCustomerSettingsMap.values()) {
            record = entrySet.remove(customerId);
            if (null != record) {
                break;
            }
        }

        LOG.debug("Deleted CustomerFirmwareTrackRecord {}", record);

        return record;
    }
    
    @Override
    public void deleteCustomerFirmwareTrackRecords(long firmwareTrackRecordId) {
    	trackIdToCustomerSettingsMap.remove(firmwareTrackRecordId);
    }

}
