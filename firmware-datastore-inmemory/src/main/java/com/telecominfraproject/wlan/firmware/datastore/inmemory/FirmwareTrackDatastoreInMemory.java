package com.telecominfraproject.wlan.firmware.datastore.inmemory;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.telecominfraproject.wlan.datastore.exceptions.DsConcurrentModificationException;
import com.telecominfraproject.wlan.datastore.exceptions.DsDuplicateEntityException;
import com.telecominfraproject.wlan.datastore.exceptions.DsEntityNotFoundException;
import com.telecominfraproject.wlan.datastore.inmemory.BaseInMemoryDatastore;
import com.telecominfraproject.wlan.firmware.datastore.FirmwareTrackDaoInterface;
import com.telecominfraproject.wlan.firmware.models.FirmwareTrackRecord;

/**
 * @author dtoptygin
 *
 */
@Component
public class FirmwareTrackDatastoreInMemory extends BaseInMemoryDatastore implements FirmwareTrackDaoInterface {

    private static final Logger LOG = LoggerFactory.getLogger(FirmwareTrackDatastoreInMemory.class);

    private static final Map<Long, FirmwareTrackRecord> idToFirmwareTrackMap = new ConcurrentHashMap<Long, FirmwareTrackRecord>();
    
    private static final AtomicLong firmwareTrackIdCounter = new AtomicLong();    

	@Autowired 
	private FirmwareTrackAssignmentDatastoreInMemory firmwareTrackAssignmentDatastore;

	@Autowired 
	private CustomerFirmwareTrackDatastoreInMemory customerFirmwareTrackDatastore;

    @Override
    public FirmwareTrackRecord createFirmwareTrack(FirmwareTrackRecord firmwareTrack) {
        
        if (null != getFirmwareTrackByNameOrNull(firmwareTrack.getTrackName())) {
            throw new DsDuplicateEntityException("duplicated track name");
        }

        FirmwareTrackRecord firmwareCopy = firmwareTrack.clone();
        
        long id = firmwareTrackIdCounter.incrementAndGet();
        firmwareCopy.setRecordId(id);
        firmwareCopy.setCreatedTimestamp(System.currentTimeMillis());
        firmwareCopy.setLastModifiedTimestamp(firmwareCopy.getCreatedTimestamp());
        idToFirmwareTrackMap.put(id, firmwareCopy);
        
        LOG.debug("Stored FirmwareTrackRecord {}", firmwareCopy);
        
        return firmwareCopy.clone();
    }


    @Override
    public FirmwareTrackRecord getFirmwareTrackById(long firmwareTrackId) {
        LOG.debug("Looking up FirmwareTrackRecord for id {}", firmwareTrackId);
        
        FirmwareTrackRecord firmwareTrack = idToFirmwareTrackMap.get(firmwareTrackId);
        
        if(firmwareTrack==null){
            LOG.debug("Cannot find FirmwareTrackRecord for id {}", firmwareTrackId);
            throw new DsEntityNotFoundException("Cannot find FirmwareTrackRecord for id " + firmwareTrackId);
        } else {
            LOG.debug("Found FirmwareTrackRecord {}", firmwareTrack);
        }

        return firmwareTrack.clone();
    }
    
    @Override
    public FirmwareTrackRecord getFirmwareTrackByName(String trackName) {
        FirmwareTrackRecord ret = getFirmwareTrackByNameOrNull(trackName);
        if (ret == null) {
            throw new DsEntityNotFoundException("Cannot find FirmwareTrackRecord for name " + trackName);
        }
        return ret;
    }

    @Override
    public FirmwareTrackRecord getFirmwareTrackByNameOrNull(String trackName) {
        LOG.debug("Looking up FirmwareTrackRecord for name {}", trackName);

        FirmwareTrackRecord firmwareTrack = null;        
        for (FirmwareTrackRecord v : idToFirmwareTrackMap.values()) {
            if (v.getTrackName().equals(trackName)) {
                firmwareTrack = v;
                break;
            }
        }

        FirmwareTrackRecord ret = null;
        if (firmwareTrack == null) {
            LOG.debug("Cannot find FirmwareTrackRecord for name {}", trackName);
        } else {
            LOG.debug("Found FirmwareTrackRecord {}", firmwareTrack);
            ret = firmwareTrack.clone();
        }
        return ret;

    }
    
    @Override
    public FirmwareTrackRecord updateFirmwareTrack(FirmwareTrackRecord firmwareTrack) {
        FirmwareTrackRecord existingFirmwareTrackRecord = getFirmwareTrackById(firmwareTrack.getRecordId());
        
        if(existingFirmwareTrackRecord.getLastModifiedTimestamp()!=firmwareTrack.getLastModifiedTimestamp()){
            LOG.debug("Concurrent modification detected for FirmwareTrackRecord with id {} expected version is {} but version in db was {}", 
                    firmwareTrack.getRecordId(),
                    firmwareTrack.getLastModifiedTimestamp(),
                    existingFirmwareTrackRecord.getLastModifiedTimestamp()
                    );
            throw new DsConcurrentModificationException("Concurrent modification detected for FirmwareTrackRecord with id " + firmwareTrack.getRecordId()
                    +" expected version is " + firmwareTrack.getLastModifiedTimestamp()
                    +" but version in db was " + existingFirmwareTrackRecord.getLastModifiedTimestamp()
                    );
            
        }
        
        FirmwareTrackRecord firmwareCopy = firmwareTrack.clone();
        firmwareCopy.setLastModifiedTimestamp(getNewLastModTs(firmwareTrack.getLastModifiedTimestamp()));

        idToFirmwareTrackMap.put(firmwareCopy.getRecordId(), firmwareCopy);
        
        LOG.debug("Updated FirmwareTrackRecord {}", firmwareCopy);
        
        return firmwareCopy.clone();
    }

    @Override
    public FirmwareTrackRecord deleteFirmwareTrackRecord(long firmwareTrackId) {
        FirmwareTrackRecord firmwareTrack = getFirmwareTrackById(firmwareTrackId);
        idToFirmwareTrackMap.remove(firmwareTrack.getRecordId());

        firmwareTrackAssignmentDatastore.deleteFirmwareTrackAssignments(firmwareTrackId);
        customerFirmwareTrackDatastore.deleteCustomerFirmwareTrackRecords(firmwareTrackId);
        
        LOG.debug("Deleted FirmwareTrackRecord {}", firmwareTrack);
        
        return firmwareTrack.clone();
    }


}
