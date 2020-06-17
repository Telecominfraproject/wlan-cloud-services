package com.telecominfraproject.wlan.manufacturer;

import java.util.List;
import java.util.Map;
import java.util.Set;

import com.telecominfraproject.wlan.core.model.json.GenericResponse;
import com.telecominfraproject.wlan.manufacturer.models.ManufacturerDetailsRecord;
import com.telecominfraproject.wlan.manufacturer.models.ManufacturerOuiDetails;

/**
 * @author mpreston
 *
 */
public interface ManufacturerInterface {

    /**
     * Create a mapping of OUI to the Manufacturer details.
     * 
     * @param ouiDetails - object containing the parameters for the OUI to Manufacturer Details mapping.
     * 
     * @return a copy of the stored OUI to Manufacturer Details mapping.
     */
    ManufacturerOuiDetails createOuiDetails(ManufacturerOuiDetails ouiDetails);
    
    /**
     * Removes a mapping of OUI to Manufacturer Details from the datastore.
     * 
     * @param oui - the unique OUI which identifies the Manufacturer details to be removed.
     * 
     * @return a copy of the removed OUI to Manufacturer Details mapping.
     */
    ManufacturerOuiDetails deleteOuiDetails(String oui);
    
    /**
     * Retrieves a list of all OUI values that are mapped to the passed in Manufacturer name.
     * 
     * @param manufacturer - name of the manufacturer for which the OUIs will be retrieved.
     * @param exactMatch - boolean flag determining if the search should be conducted for only an 
     * exact match of the passed in Manufacturer name, or if the search should match any record that
     * begins with the passed in search string.
     * 
     * @return a list of all OUIs that match the passed in Manufacturer name.
     */
    List<String> getOuiListForManufacturer(String manufacturer, boolean exactMatch);
    
    /**
     * Retrieves the OUI to Manufacturer Details mapping specified by the passed in OUI.
     * 
     * @param oui - unique identifier of a mapping between OUI and Manufacturer details.
     * 
     * @return a copy of the OUI to Manufacturer details specified by the passed in OUI.
     */
    ManufacturerOuiDetails getByOui(String oui);
    
    
    
    /**
     * Stores a Manufacturer Details record in the datasstore.
     * 
     * @param clientManufacturerDetails - object containing the Manufacturer Details to be stored.
     * 
     * @return a copy of the Manufacturer Details stored.
     */
    ManufacturerDetailsRecord createManufacturerDetails(ManufacturerDetailsRecord clientManufacturerDetails);
    
    /**
     * Updates a Manufacturer Details record in the datasstore.
     * 
     * @param clientManufacturerDetails - object containing the Manufacturer Details to be updated.
     * 
     * @return a copy of the updated Manufacturer Details.
     */
    ManufacturerDetailsRecord updateManufacturerDetails(ManufacturerDetailsRecord clientManufacturerDetails);
    
    /**
     * Removes a Manufacturer Details record from the datastore.
     * 
     * @param id - unique identifier of the Manufacturer Details to be removed.
     * 
     * @return a copy of the removed Manufacturer Details record.
     */
    ManufacturerDetailsRecord deleteManufacturerDetails(long id);
    
    /**
     * Retrieves a Manufacturer Details record identified by the passed in id.
     * 
     * @param id - unique identifier of a Manufacturer Details record.
     * 
     * @return a copy of the Manufacturer Details record stored in the datastore 
     * that is identified by the passed in id.
     */
    ManufacturerDetailsRecord getById(long id);


    /**
     * Retrieves the entire contents of the Manufacturer Management Datastore.
     * @return result
     */
    List<ManufacturerOuiDetails> getAllManufacturerData();
    
    
    /**
     * Appends any records from the passed in file to the Manufacturer Details datastore.
     * Only adds in new records.
     * @param fileName 
     * @param base64GzippedContent base64 encoded gzipped file
     * 
     * @return result
     * 
     */
    GenericResponse uploadOuiDataFile(String fileName, byte[] base64GzippedContent);
    
    /**
     * Retrieve all the OUI details for a specific list of OUI values.
     * @param ouiSet - set of OUI values for which ManfacturerDetails shall be retrieved.
     * 
     * @return a map of the OUI string to the Manufacturer details for the provided list of OUI values.
     */
    Map<String, ManufacturerOuiDetails> getManufacturerDetailsForOuiSet(Set<String> ouiSet);
    
    
    /**
     * Update the alias of an OUI record in the datastore using the passed in object 
     * as the new value.
     * 
     * @return - the newly updated and persisted OUI details.
     */
    ManufacturerOuiDetails updateOuiAlias(ManufacturerOuiDetails ouiDetails);
    
    
    /**
     * Retrieves a list of Alias values persisted in the datastore that start with the passed in prefix. 
     * 
     * @param prefix
     * @param maxResults - number of results to return. if maxResults is greater than 1000, then 1000 results will be returned.

     */
    List<String> getAliasValuesThatBeginWith(String prefix, int maxResults);
}
