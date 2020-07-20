package com.telecominfraproject.wlan.firmware.datastore.inmemory;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.telecominfraproject.wlan.core.model.equipment.EquipmentType;
import com.telecominfraproject.wlan.datastore.exceptions.DsConcurrentModificationException;
import com.telecominfraproject.wlan.datastore.exceptions.DsEntityNotFoundException;
import com.telecominfraproject.wlan.datastore.inmemory.BaseInMemoryDatastore;
import com.telecominfraproject.wlan.firmware.datastore.FirmwareVersionDaoInterface;
import com.telecominfraproject.wlan.firmware.models.FirmwareVersion;

/**
 * @author dtoptygin
 *
 */
@Component
public class FirmwareVersionDatastoreInMemory extends BaseInMemoryDatastore implements FirmwareVersionDaoInterface {

    private static final Logger LOG = LoggerFactory.getLogger(FirmwareVersionDatastoreInMemory.class);

    private static final Map<Long, FirmwareVersion> idToFirmwareVersionMap = new ConcurrentHashMap<Long, FirmwareVersion>();
    
    private static final AtomicLong firmwareVersionIdCounter = new AtomicLong();    

    @Override
    public FirmwareVersion create(FirmwareVersion firmware) {
        
    	if(firmware.getModelId() == null) {
    		throw new IllegalArgumentException("modelId must be provided for the firmware");
    	}
    	
        FirmwareVersion firmwareCopy = firmware.clone();
        
        long id = firmwareVersionIdCounter.incrementAndGet();
        firmwareCopy.setId(id);
        firmwareCopy.setCreatedTimestamp(System.currentTimeMillis());
        firmwareCopy.setLastModifiedTimestamp(firmwareCopy.getCreatedTimestamp());
        idToFirmwareVersionMap.put(id, firmwareCopy);
        
        LOG.debug("Stored FirmwareVersion {}", firmwareCopy);
        
        return firmwareCopy.clone();
    }


    @Override
    public FirmwareVersion get(long firmwareId) {
        LOG.debug("Looking up FirmwareVersion for id {}", firmwareId);
        
        FirmwareVersion firmware = idToFirmwareVersionMap.get(firmwareId);
        
        if(firmware==null){
            LOG.debug("Cannot find FirmwareVersion for id {}", firmwareId);
            throw new DsEntityNotFoundException("Cannot find FirmwareVersion for id " + firmwareId);
        } else {
            LOG.debug("Found FirmwareVersion {}", firmware);
        }

        return firmware.clone();
    }
    
    @Override
    public FirmwareVersion getByName(String versionName) {
        FirmwareVersion ret = getByNameOrNull(versionName);
        if (ret == null) {
            throw new DsEntityNotFoundException("Cannot find FirmwareVersion for name " + versionName);
        }
        return ret;
    }

    @Override
    public FirmwareVersion getByNameOrNull(String versionName) {
        LOG.debug("Looking up FirmwareVersion for name {}", versionName);

        FirmwareVersion firmwareVersion = null;        
        for (FirmwareVersion v : idToFirmwareVersionMap.values()) {
            if (v.getVersionName().equals(versionName)) {
                firmwareVersion = v;
                break;
            }
        }

        FirmwareVersion ret = null;
        if (firmwareVersion == null) {
            LOG.debug("Cannot find FirmwareVersion for name {}", versionName);
        } else {
            LOG.debug("Found FirmwareVersion {}", firmwareVersion);
            ret = firmwareVersion.clone();
        }
        return ret;

    }
    
    @Override
    public FirmwareVersion update(FirmwareVersion firmware) {
    	if(firmware.getModelId() == null) {
    		throw new IllegalArgumentException("modelId must be provided for the firmware");
    	}

        FirmwareVersion existingFirmwareVersion = get(firmware.getId());
        
        if(existingFirmwareVersion.getLastModifiedTimestamp()!=firmware.getLastModifiedTimestamp()){
            LOG.debug("Concurrent modification detected for FirmwareVersion with id {} expected version is {} but version in db was {}", 
                    firmware.getId(),
                    firmware.getLastModifiedTimestamp(),
                    existingFirmwareVersion.getLastModifiedTimestamp()
                    );
            throw new DsConcurrentModificationException("Concurrent modification detected for FirmwareVersion with id " + firmware.getId()
                    +" expected version is " + firmware.getLastModifiedTimestamp()
                    +" but version in db was " + existingFirmwareVersion.getLastModifiedTimestamp()
                    );
            
        }
        
        FirmwareVersion firmwareCopy = firmware.clone();
        firmwareCopy.setLastModifiedTimestamp(getNewLastModTs(firmware.getLastModifiedTimestamp()));

        idToFirmwareVersionMap.put(firmwareCopy.getId(), firmwareCopy);
        
        LOG.debug("Updated FirmwareVersion {}", firmwareCopy);
        
        return firmwareCopy.clone();
    }

    @Override
    public FirmwareVersion delete(long firmwareId) {
        FirmwareVersion firmware = get(firmwareId);
        idToFirmwareVersionMap.remove(firmware.getId());
        
        LOG.debug("Deleted FirmwareVersion {}", firmware);
        
        return firmware.clone();
    }

    @Override
    public Map<EquipmentType, List<FirmwareVersion>> getAllGroupedByEquipmentType() {
    	
        EnumMap<EquipmentType, List<FirmwareVersion>> resultMap = new EnumMap<>(EquipmentType.class);
        
        for (EquipmentType et : EquipmentType.values()) {
            resultMap.put(et, new ArrayList<>());
        }
        
        for (FirmwareVersion v : idToFirmwareVersionMap.values()) {
            resultMap.get(v.getEquipmentType()).add(v.clone());
        }

        return resultMap;
    }

    @Override
    public List<FirmwareVersion> getAllFirmwareVersionsByEquipmentType(EquipmentType equipmentType, String modelId) {
    	List<FirmwareVersion> ret = new ArrayList<>();
    	
        for (FirmwareVersion v : idToFirmwareVersionMap.values()) {
        	if(v.getEquipmentType() == equipmentType && (modelId == null || v.getModelId().equals(modelId)) ){
        		ret.add(v.clone());
        	}
        }

        return ret;
    }
    
    @Override
    public List<String> getAllFirmwareModelIdsByEquipmentType(EquipmentType equipmentType) {
    	Set<String> ret = new HashSet<>();
    	
        for (FirmwareVersion v : idToFirmwareVersionMap.values()) {
        	if(v.getEquipmentType() == equipmentType ){
        		ret.add(v.getModelId());
        	}
        }

        return new ArrayList<>(ret);
    }
    
}
