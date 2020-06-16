package com.telecominfraproject.wlan.firmware.datastore.inmemory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.telecominfraproject.wlan.datastore.exceptions.DsConcurrentModificationException;
import com.telecominfraproject.wlan.datastore.inmemory.BaseInMemoryDatastore;
import com.telecominfraproject.wlan.firmware.datastore.FirmwareTrackAssignmentDaoInterface;
import com.telecominfraproject.wlan.firmware.models.FirmwareTrackAssignmentDetails;
import com.telecominfraproject.wlan.firmware.models.FirmwareTrackAssignmentRecord;
import com.telecominfraproject.wlan.firmware.models.FirmwareTrackRecord;
import com.telecominfraproject.wlan.firmware.models.FirmwareVersion;

/**
 * @author dtoptygin
 *
 */
@Component
public class FirmwareTrackAssignmentDatastoreInMemory extends BaseInMemoryDatastore implements FirmwareTrackAssignmentDaoInterface {

    private static final Logger LOG = LoggerFactory.getLogger(FirmwareTrackAssignmentDatastoreInMemory.class);

    private final Map<Long, ConcurrentHashMap<Long, FirmwareTrackAssignmentRecord>> trackIdToFirmwareVersionIdMap = new ConcurrentHashMap<>();

	@Autowired 
	private FirmwareVersionDatastoreInMemory firmwareVersionDatastore;
	
	@Autowired 
	private FirmwareTrackDatastoreInMemory firmwareTrackDatastore;


	@Override
    public FirmwareTrackAssignmentRecord createOrUpdateFirmwareTrackAssignment(
            FirmwareTrackAssignmentRecord assignmentRecord) {
        FirmwareTrackRecord trackRecord = firmwareTrackDatastore.getFirmwareTrackById(assignmentRecord.getTrackRecordId());
        FirmwareVersion firmwareVersion = firmwareVersionDatastore.get(assignmentRecord.getFirmwareVersionRecordId());

        ConcurrentHashMap<Long, FirmwareTrackAssignmentRecord> assignments = trackIdToFirmwareVersionIdMap
                .get(trackRecord.getRecordId());
        if (assignments != null) {
            FirmwareTrackAssignmentRecord existing = assignments.get(firmwareVersion.getId());

            if (existing != null
                    && existing.getLastModifiedTimestamp() != assignmentRecord.getLastModifiedTimestamp()) {
                LOG.debug(
                        "Concurrent modification detected for Firmware Version Track assignment with trackId {} and versionId {} expected version is {} but version in db was {}",
                        assignmentRecord.getTrackRecordId(), assignmentRecord.getFirmwareVersionRecordId(),
                        assignmentRecord.getLastModifiedTimestamp(), existing.getLastModifiedTimestamp());
                throw new DsConcurrentModificationException(
                        "Concurrent modification detected for Firmware Version Track assignment with trackId "
                                + assignmentRecord.getTrackRecordId() + " and firmware record id "
                                + assignmentRecord.getFirmwareVersionRecordId() + " expected version is "
                                + assignmentRecord.getLastModifiedTimestamp() + " but version in db was "
                                + existing.getLastModifiedTimestamp());

            }
        } else {
            assignments = new ConcurrentHashMap<>();
            trackIdToFirmwareVersionIdMap.put(trackRecord.getRecordId(), assignments);
        }
        FirmwareTrackAssignmentRecord copy = assignmentRecord.clone();
        copy.setLastModifiedTimestamp(getNewLastModTs(assignmentRecord.getLastModifiedTimestamp()));
        assignments.put(copy.getFirmwareVersionRecordId(), copy);
        return copy;
    }

    @Override
    public FirmwareTrackAssignmentRecord deleteFirmwareTrackAssignment(long firmwareTrackRecordId,
            long firmwareVersionRecordId) {
        ConcurrentHashMap<Long, FirmwareTrackAssignmentRecord> assignments = trackIdToFirmwareVersionIdMap
                .get(firmwareTrackRecordId);
        FirmwareTrackAssignmentRecord ret = null;

        if (assignments != null) {
            ret = assignments.remove(firmwareVersionRecordId);
        }
        return ret;
    }

    @Override
    public void deleteFirmwareTrackAssignments(long firmwareTrackRecordId) {
        trackIdToFirmwareVersionIdMap.remove(firmwareTrackRecordId);
    }

    @Override
    public List<FirmwareTrackAssignmentDetails> getFirmwareTrackDetails(String firmwareTrackName) {
        FirmwareTrackRecord trackRecord = firmwareTrackDatastore.getFirmwareTrackByName(firmwareTrackName);
        ConcurrentHashMap<Long, FirmwareTrackAssignmentRecord> assignmentRecords = trackIdToFirmwareVersionIdMap
                .get(trackRecord.getRecordId());
        List<FirmwareTrackAssignmentDetails> versions = new ArrayList<>();
        if (assignmentRecords != null) {
            for (FirmwareTrackAssignmentRecord a : assignmentRecords.values()) {
                FirmwareVersion versionRecord = firmwareVersionDatastore.get(a.getFirmwareVersionRecordId());
                FirmwareTrackAssignmentDetails version = new FirmwareTrackAssignmentDetails(a, versionRecord);
                versions.add(version);
            }
        }
        return versions;
    }

    @Override
    public FirmwareTrackAssignmentDetails getFirmwareTrackAssignmentDetails(long firmwareTrackRecordId,
            long firmwareVersionRecordId) {
        ConcurrentHashMap<Long, FirmwareTrackAssignmentRecord> assignmentRecords = trackIdToFirmwareVersionIdMap
                .get(firmwareTrackRecordId);
        FirmwareTrackAssignmentRecord rec = null;
        if (assignmentRecords != null) {
            rec = assignmentRecords.get(firmwareVersionRecordId);
        }
        FirmwareTrackAssignmentDetails ret = null;
        if (rec != null) {
            FirmwareVersion version = firmwareVersionDatastore.get(firmwareVersionRecordId);
            if (version != null) {
                ret = new FirmwareTrackAssignmentDetails(rec, version);
            }
        }
        return ret;
    }

    @Override
    public Map<String, FirmwareTrackAssignmentDetails> getAllDefaultFirmwareTrackAssignmentDetails(
            long firmwareTrackRecordId) {
        Map<String, FirmwareTrackAssignmentDetails> allDefaultFirmwareTrackAssignmentDetails = new HashMap<>();
        FirmwareTrackRecord trackRecord = firmwareTrackDatastore.getFirmwareTrackById(firmwareTrackRecordId);

        if (trackRecord != null) {
            List<FirmwareTrackAssignmentDetails> listOfVersions = getFirmwareTrackDetails(trackRecord.getTrackName());
            if (listOfVersions != null) {
                for (FirmwareTrackAssignmentDetails d : listOfVersions) {
                    if (d.isDefaultRevisionForTrack()) {
                        allDefaultFirmwareTrackAssignmentDetails.put(d.getModelId(), d);
                    }
                }
            }
        }

        return allDefaultFirmwareTrackAssignmentDetails;
    }

    @Override
    public FirmwareTrackAssignmentDetails getDefaultFirmwareTrackAssignmentDetailsForEquipmentModel(
            long firmwareTrackRecordId, String equipmentModel) {
        FirmwareTrackRecord r = firmwareTrackDatastore.getFirmwareTrackById(firmwareTrackRecordId);
        return getDefaultFirmwareTrackAssignmentDetailsForEquipmentModelByTrackName(r.getTrackName(), equipmentModel);
    }

    @Override
    public FirmwareTrackAssignmentDetails getDefaultFirmwareTrackAssignmentDetailsForEquipmentModelByTrackName(String trackName,
            String equipmentModel) {
        if (equipmentModel == null || equipmentModel.length() == 0) {
            return null;
        }

        FirmwareTrackRecord trackRecord = firmwareTrackDatastore.getFirmwareTrackByNameOrNull(trackName);
        if (trackRecord != null) {
            List<FirmwareTrackAssignmentDetails> listOfVersions = getFirmwareTrackDetails(trackName);
            if (listOfVersions != null) {
                for (FirmwareTrackAssignmentDetails d : listOfVersions) {
                    if (d.isDefaultRevisionForTrack() && equipmentModel.equals(d.getModelId())) {
                        return d;
                    }
                }
            }
        }

        return null;
    }

	public List<FirmwareTrackAssignmentDetails> getAllFirmwareVersionsByTrackId(long recordId) {
		// TODO Auto-generated method stub
		return null;
	}
    
}
