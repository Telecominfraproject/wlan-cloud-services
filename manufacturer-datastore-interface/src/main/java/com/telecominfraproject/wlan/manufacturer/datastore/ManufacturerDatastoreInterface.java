package com.telecominfraproject.wlan.manufacturer.datastore;

import java.util.List;
import java.util.Map;

import com.telecominfraproject.wlan.core.model.json.GenericResponse;
import com.telecominfraproject.wlan.manufacturer.models.ManufacturerDetailsRecord;
import com.telecominfraproject.wlan.manufacturer.models.ManufacturerOuiDetails;

/**
 * @author mpreston
 *
 */
public interface ManufacturerDatastoreInterface {
    
    ManufacturerOuiDetails createOuiDetails(ManufacturerOuiDetails ouiDetails);
    ManufacturerOuiDetails deleteOuiDetails(String oui);
    List<String> getOuiListForManufacturer(String manufacturer, boolean exactMatch);
    ManufacturerOuiDetails getByOui(String oui);
    
    ManufacturerDetailsRecord createManufacturerDetails(ManufacturerDetailsRecord clientManufacturerDetails);
    ManufacturerDetailsRecord updateManufacturerDetails(ManufacturerDetailsRecord clientManufacturerDetails);
    ManufacturerDetailsRecord deleteManufacturerDetails(long id);
    ManufacturerDetailsRecord getById(long id);
    List<ManufacturerDetailsRecord> getByManufacturer(String manufacturer, boolean exactMatch);
    
    List<ManufacturerOuiDetails> getAllManufacturerData();
    GenericResponse uploadOuiDataFile(String filePath, byte[] gzipContent);
    Map<String, ManufacturerOuiDetails> getManufacturerDetailsForOuiList(List<String> ouiList);
    
    long getManufacturerDetialsId(String oui);
    List<String> getAliasValuesThatBeginWith(String prefix, int maxResults);
}
