package com.telecominfraproject.wlan.manufacturer.datastore.inmemory;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.zip.GZIPInputStream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.CollectionUtils;

import com.telecominfraproject.wlan.core.model.json.GenericResponse;
import com.telecominfraproject.wlan.datastore.exceptions.DsConcurrentModificationException;
import com.telecominfraproject.wlan.datastore.exceptions.DsDataValidationException;
import com.telecominfraproject.wlan.datastore.exceptions.DsDuplicateEntityException;
import com.telecominfraproject.wlan.datastore.exceptions.DsEntityNotFoundException;
import com.telecominfraproject.wlan.datastore.inmemory.BaseInMemoryDatastore;
import com.telecominfraproject.wlan.manufacturer.datastore.ManufacturerDatastoreInterface;
import com.telecominfraproject.wlan.manufacturer.datastore.ManufacturerDatastoreUtilities;
import com.telecominfraproject.wlan.manufacturer.models.ManufacturerDetailsRecord;
import com.telecominfraproject.wlan.manufacturer.models.ManufacturerOuiDetails;

/**
 * @author mpreston
 *
 */
@Configuration
public class ManufacturerDatastoreInMemory extends BaseInMemoryDatastore implements ManufacturerDatastoreInterface {

    private static final Logger LOG = LoggerFactory.getLogger(ManufacturerDatastoreInMemory.class);

    private final Map<String, Long> ouiToManufacturerDetailsMap = new ConcurrentHashMap<>();
    private final Map<Long, ManufacturerDetailsRecord> idToManufacturerDetailsRecordMap = new ConcurrentHashMap<>(); 

    private static AtomicLong clientManufacturerDetailsIdCounter = new AtomicLong(0);
    
    @Override
    public ManufacturerOuiDetails createOuiDetails(ManufacturerOuiDetails ouiDetails) {
        if (ouiDetails.getManufacturerName() == null || ouiDetails.getManufacturerName().isEmpty()) {
            throw new DsDataValidationException("Unable to create ManufacturerOuiDetails when no Manufacturer name is provided.");
        }
        
        Long existingDetailsId = ouiToManufacturerDetailsMap.get(ouiDetails.getOui().toLowerCase());
        if (existingDetailsId != null) {
            throw new DsDuplicateEntityException("Unable to create ManufacturerOuiDetails for an OUI that already exists.");
        }
        
        // Check to see if there is already Manufacturer details for this manufacturer:
        ManufacturerDetailsRecord manufacturerDetails = null;
        String manufacturerName = ManufacturerDatastoreUtilities.getByManufacturerName(ouiDetails.getOui(), ouiDetails.getManufacturerName());
        List<ManufacturerDetailsRecord> manufacturerDetailList = getByManufacturer(manufacturerName, true);
        if (!manufacturerDetailList.isEmpty()) {
            // we will use the one match the name, not the alias because alias can change.
            for (ManufacturerDetailsRecord detail: manufacturerDetailList) {
                if (detail.getManufacturerName().equalsIgnoreCase(manufacturerName)) {
                    manufacturerDetails = detail;
                    break;
                }
            }
        }
        
        if (null == manufacturerDetails) {
            // There was no existing manufacturer details entry. create that,
            // then create the OU entry.
            manufacturerDetails = new ManufacturerDetailsRecord();
            manufacturerDetails.setManufacturerName(manufacturerName);
            manufacturerDetails.setManufacturerAlias(ouiDetails.getManufacturerAlias());
            manufacturerDetails = createManufacturerDetails(manufacturerDetails);
        }
        
        ouiToManufacturerDetailsMap.put(ouiDetails.getOui().toLowerCase(), manufacturerDetails.getId());
        LOG.debug("Stored ClientOuiDetails {}", ouiDetails);
        return ouiDetails.clone();
    }


    @Override
    public ManufacturerOuiDetails deleteOuiDetails(String oui) {
        ManufacturerOuiDetails clientOuiDetails = getByOui(oui);
        ouiToManufacturerDetailsMap.remove(clientOuiDetails.getOui().toLowerCase());

        LOG.debug("Deleted ManufacturerOuiDetails {}", clientOuiDetails);

        return clientOuiDetails.clone();
    }


    @Override
    public List<String> getOuiListForManufacturer(String manufacturer, boolean exactMatch) {
        List<ManufacturerDetailsRecord> detailsRecordList = getByManufacturer(manufacturer, exactMatch);
        List<String> resultList = new ArrayList<>();
        
        for (Entry<String, Long> ouiEntry : ouiToManufacturerDetailsMap.entrySet()) {
            for (ManufacturerDetailsRecord record : detailsRecordList) {
                if (ouiEntry.getValue() == record.getId()) {
                    resultList.add(ouiEntry.getKey());
                }
            }
        }
        
        return resultList;
    }


    @Override
    public ManufacturerOuiDetails getByOui(String oui) {
        Long detailsId = ouiToManufacturerDetailsMap.get(oui.toLowerCase());
        if (null == detailsId) {
            LOG.debug("Unable to find ManufacturerDetailsRecord with oui {}", oui);
            throw new DsEntityNotFoundException("ManufacturerDetailsRecord not found for OUI " + oui);
        }
        ManufacturerOuiDetails result = new ManufacturerOuiDetails();
        result.setOui(oui);
        ManufacturerDetailsRecord detailsRecord = idToManufacturerDetailsRecordMap.get(detailsId);
        
        result.setManufacturerName(detailsRecord.getManufacturerName());
        result.setManufacturerAlias(detailsRecord.getManufacturerAlias());
        
        LOG.debug("Found ManufacturerOuiDetails {}", result);
        return result;
    }


    @Override
    public ManufacturerDetailsRecord createManufacturerDetails(
            ManufacturerDetailsRecord clientManufacturerDetails) {
        long id = clientManufacturerDetailsIdCounter.incrementAndGet();
        clientManufacturerDetails.setId(id);
        clientManufacturerDetails.setCreatedTimestamp(System.currentTimeMillis());
        clientManufacturerDetails.setLastModifiedTimestamp(clientManufacturerDetails.getCreatedTimestamp());
        idToManufacturerDetailsRecordMap.put(id, clientManufacturerDetails);
        ManufacturerDetailsRecord clientManufacturerDetailsCopy = clientManufacturerDetails.clone();

        LOG.debug("Stored Manufacturer details {}", clientManufacturerDetailsCopy);

        return clientManufacturerDetailsCopy; 
    }


    @Override
    public ManufacturerDetailsRecord updateManufacturerDetails(
            ManufacturerDetailsRecord clientManufacturerDetails) {
        ManufacturerDetailsRecord existingClientManufacturerDetails = getById(clientManufacturerDetails.getId());

        if(existingClientManufacturerDetails.getLastModifiedTimestamp()!=clientManufacturerDetails.getLastModifiedTimestamp()){
            LOG.debug("Concurrent modification detected for ManufacturerDetailsRecord with id {} expected version is {} but version in db was {}", 
                    clientManufacturerDetails.getId(),
                    clientManufacturerDetails.getLastModifiedTimestamp(),
                    existingClientManufacturerDetails.getLastModifiedTimestamp()
                    );
            throw new DsConcurrentModificationException("Concurrent modification detected for ManufacturerDetailsRecord with id " + clientManufacturerDetails.getId()
                    +" expected version is " + clientManufacturerDetails.getLastModifiedTimestamp()
                    +" but version in db was " + existingClientManufacturerDetails.getLastModifiedTimestamp()
                    );

        }

        ManufacturerDetailsRecord clientManufacturerDetailsCopy = clientManufacturerDetails.clone();
        clientManufacturerDetailsCopy.setLastModifiedTimestamp(getNewLastModTs(clientManufacturerDetails.getLastModifiedTimestamp()));
        idToManufacturerDetailsRecordMap.put(clientManufacturerDetailsCopy.getId(), clientManufacturerDetailsCopy);

        LOG.debug("Updated ManufacturerDetailsRecord {}", clientManufacturerDetailsCopy);

        return clientManufacturerDetailsCopy.clone();
    }


    @Override
    public ManufacturerDetailsRecord deleteManufacturerDetails(long id) {
        ManufacturerDetailsRecord clientManufacturerDetails = getById(id);
        idToManufacturerDetailsRecordMap.remove(clientManufacturerDetails.getId());
        LOG.debug("Deleted ManufacturerDetailsRecord {}", clientManufacturerDetails);
        return clientManufacturerDetails;
    }


    @Override
    public ManufacturerDetailsRecord getById(long id) {
        ManufacturerDetailsRecord existingRecord = idToManufacturerDetailsRecordMap.get(id);
        
        if (existingRecord == null) {
            LOG.debug("Unable to find ManufacturerDetailsRecord with id {}", id);
            throw new DsEntityNotFoundException("ManufacturerDetailsRecord not found " + id);
        }
        LOG.debug("Found ManufacturerDetailsRecord {}", existingRecord);
        return existingRecord.clone();
    }


    @Override
    public List<ManufacturerDetailsRecord> getByManufacturer(String manufacturer, boolean exactMatch) {
        List<ManufacturerDetailsRecord> resultList = new ArrayList<>();
        
        for (ManufacturerDetailsRecord record : idToManufacturerDetailsRecordMap.values()) {
            if (exactMatch) {
                if (record.getManufacturerName().equals(manufacturer)) {
                    resultList.add(record.clone());
                } else if (record.getManufacturerAlias() != null && record.getManufacturerAlias().equals(manufacturer)) {
                    resultList.add(record.clone());
                }
            } else {
                if (record.getManufacturerName().startsWith(manufacturer)) {
                    resultList.add(record.clone());
                } else if (record.getManufacturerAlias() != null && record.getManufacturerAlias().startsWith(manufacturer)) {
                    resultList.add(record.clone());
                }
            }
        }
        
        LOG.debug("Found {} ManufacturerDetailsRecords", resultList.size());
        
        return resultList;
    }


    @Override
    public List<ManufacturerOuiDetails> getAllManufacturerData() {
        List<ManufacturerOuiDetails> result = new ArrayList<>();
        for (Entry<String, Long> entry : ouiToManufacturerDetailsMap.entrySet()) {
            long detailsId = entry.getValue();
            ManufacturerOuiDetails addItem = new ManufacturerOuiDetails();
            addItem.setOui(entry.getKey());
            
            ManufacturerDetailsRecord detailsRecord = idToManufacturerDetailsRecordMap.get(detailsId);
            addItem.setManufacturerName(detailsRecord.getManufacturerName());
            addItem.setManufacturerAlias(detailsRecord.getManufacturerAlias());
            result.add(addItem);
        }
        
        LOG.debug("Found {} ManufacturerOuiDetails", result.size());
        return result;
    }


    @Override
    public GenericResponse uploadOuiDataFile(String filePath, byte[] gzipContent) {
        Pattern ouiPattern = Pattern.compile("^[0-9A-F]{6}");
        Pattern companyPattern = Pattern.compile("[\t ]([^\t]+)$");

        try
        {
            InputStream in = new GZIPInputStream(new ByteArrayInputStream(gzipContent));
            BufferedReader input = new BufferedReader(new InputStreamReader(in));

            while(input.ready())
            {
                String line = input.readLine();
                Matcher matcher = ouiPattern.matcher(line);

                if(matcher.find())
                {
                    String prefix = line.substring(matcher.start(), matcher.end());
                    Matcher companyMatcher = companyPattern.matcher(line);

                    if(companyMatcher.find())
                    {
                        String company = line.substring(companyMatcher.start() + 1, companyMatcher.end());
                        ManufacturerOuiDetails addDetails = new ManufacturerOuiDetails();
                        addDetails.setOui(prefix);
                        addDetails.setManufacturerName(company);
                        createOuiDetails(addDetails);
                    }
                }
            }
        }
        catch(Exception e)
        {
            GenericResponse response = new GenericResponse();
            response.setSuccess(false);
            response.setMessage("Failed to load " + filePath + " resource for Mac datastore population: " + e.getLocalizedMessage());
            return response;
        }
        
        GenericResponse response = new GenericResponse();
        response.setSuccess(true);
        return response;
    }


    @Override
    public Map<String, ManufacturerOuiDetails> getManufacturerDetailsForOuiList(List<String> ouiList) {
        Map<String, ManufacturerOuiDetails> result = new HashMap<>();
        if (CollectionUtils.isEmpty(ouiList)) {
            return result;
        }
        for (Entry<String, Long> entry : ouiToManufacturerDetailsMap.entrySet()) {
            if (ouiList.contains(entry.getKey())) {
                long detailsId = entry.getValue();
                ManufacturerOuiDetails addItem = new ManufacturerOuiDetails();
                addItem.setOui(entry.getKey());
                
                ManufacturerDetailsRecord detailsRecord = idToManufacturerDetailsRecordMap.get(detailsId);
                addItem.setManufacturerName(detailsRecord.getManufacturerName());
                addItem.setManufacturerAlias(detailsRecord.getManufacturerAlias());
                result.put(entry.getKey(), addItem); 
            }
        }
        
        LOG.debug("Found {} ManufacturerOuiDetails for OUI list", result.size());
        return result;
    }


    @Override
    public long getManufacturerDetialsId(String oui) {
        Long retrievedResult = ouiToManufacturerDetailsMap.get(oui);
        
        if (retrievedResult != null) {
            return retrievedResult;
        } 
        
        return -1;
    }


    @Override
    public List<String> getAliasValuesThatBeginWith(String prefix, int maxResults) {
        List<String> result = new ArrayList<>();
        
        if (maxResults < 0 || maxResults > 1000) {
            maxResults = 1000;
        }
        
        for (ManufacturerDetailsRecord rec : idToManufacturerDetailsRecordMap.values()) {
            if (result.size() >= maxResults) {
                break;
            }
            if (rec.getManufacturerAlias() != null && rec.getManufacturerAlias().startsWith(prefix)) {
                result.add(rec.getManufacturerAlias());
            }
        }
        
        return result;
    }


}
