package com.telecominfraproject.wlan.manufacturer.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Base64Utils;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.telecominfraproject.wlan.core.model.json.GenericResponse;
import com.telecominfraproject.wlan.datastore.exceptions.DsEntityNotFoundException;
import com.telecominfraproject.wlan.manufacturer.datastore.ManufacturerDatastoreInterface;
import com.telecominfraproject.wlan.manufacturer.models.ManufacturerDetailsRecord;
import com.telecominfraproject.wlan.manufacturer.models.ManufacturerOuiDetails;

/**
 * @author mpreston
 *
 */
@RestController
@Transactional
@RequestMapping(value = "/api/manufacturer")
public class ManufacturerController {

    private static final Logger LOG = LoggerFactory.getLogger(ManufacturerController.class);

    @Autowired
    private ManufacturerDatastoreInterface manufacturerDatastore;

    public static class ListOfMfrOuiDetailRecords extends ArrayList<ManufacturerOuiDetails> {
        private static final long serialVersionUID = -8035392255039609079L;
    }

    @PostMapping(value = "/oui")
    public ManufacturerOuiDetails createOuiDetails(@RequestBody ManufacturerOuiDetails ouiDetails) {
        LOG.debug("Creating OUI details {} ", ouiDetails);

        ManufacturerOuiDetails ret = manufacturerDatastore.createOuiDetails(ouiDetails);

        LOG.debug("Created OUI details {} ", ouiDetails);
        return ret;
    }

    @DeleteMapping(value = "/oui")
    public ManufacturerOuiDetails deleteOuiDetails(@RequestParam String oui) {
        LOG.debug("Deleteing OUI details identified by OUI {} ", oui);

        ManufacturerOuiDetails ret = manufacturerDatastore.deleteOuiDetails(oui);

        LOG.debug("Removed OUI details {} ", ret);
        return ret;
    }

    @GetMapping(value = "/oui/forManufacturer")
    public List<String> getOuiListForManufacturer(@RequestParam String manufacturer, @RequestParam boolean exactMatch) {
        LOG.debug("Retrieving all OUIs for manufacturer {}, exactMatch={} ", manufacturer, exactMatch);

        List<String> ret = manufacturerDatastore.getOuiListForManufacturer(manufacturer, exactMatch);

        LOG.debug("Retrieved found the following OUIs for manufacturer {}:  {}", manufacturer, ret);
        return ret;
    }

    @GetMapping(value = "/oui")
    public ManufacturerOuiDetails getByOui(@RequestParam String oui) {
        LOG.debug("Retrieving OUI details for OUI {} ", oui);

        ManufacturerOuiDetails ret = manufacturerDatastore.getByOui(oui);

        LOG.debug("Retrieved OUI details {} ", ret);
        return ret;
    }

    @PostMapping
    public ManufacturerDetailsRecord createManufacturerDetails(
            @RequestBody ManufacturerDetailsRecord clientManufacturerDetails) {
        LOG.debug("Creating Manufacturing Details Record for {} ", clientManufacturerDetails);

        ManufacturerDetailsRecord ret = manufacturerDatastore.createManufacturerDetails(clientManufacturerDetails);

        LOG.debug("Created Manufacturer details {} ", ret);
        return ret;
    }

    @PutMapping
    public ManufacturerDetailsRecord updateManufacturerDetails(
            @RequestBody ManufacturerDetailsRecord clientManufacturerDetails) {
        LOG.debug("Updating Manufacturing Details Record {} ", clientManufacturerDetails);

        ManufacturerDetailsRecord ret = manufacturerDatastore.updateManufacturerDetails(clientManufacturerDetails);

        LOG.debug("Updated Manufacturer details {} ", ret);
        return ret;
    }

    @DeleteMapping
    public ManufacturerDetailsRecord deleteManufacturerDetails(@RequestParam long id) {
        LOG.debug("Removing Manufacturing Details Record {} ", id);

        ManufacturerDetailsRecord ret = manufacturerDatastore.deleteManufacturerDetails(id);

        LOG.debug("Removed Manufacturer details {} ", ret);
        return ret;
    }

    @GetMapping
    public ManufacturerDetailsRecord getById(@RequestParam long id) {
        LOG.debug("Retrieving Manufacturing Details Record for id {} ", id);

        ManufacturerDetailsRecord ret = manufacturerDatastore.getById(id);

        LOG.debug("Retrieved Manufacturer details {} ", ret);
        return ret;
    }

    @GetMapping(value = "/oui/all")
    public List<ManufacturerOuiDetails> getAllManufacturerData() {
        LOG.debug("Retrieving all Manufacturing Details Records");

        List<ManufacturerOuiDetails> ret = manufacturerDatastore.getAllManufacturerData();

        LOG.debug("Retrieved {} Manufacturing Details Records", ret.size());
        return ret;
    }

    @PostMapping(value = "/oui/upload", consumes = { MediaType.APPLICATION_OCTET_STREAM_VALUE })
    public GenericResponse uploadOuiDataFile(@RequestParam String fileName, @RequestBody byte[] gzippedContent) {
        LOG.debug("uploadOuiDataFile({})", fileName);
        GenericResponse ret;
        if (null == gzippedContent) {
            LOG.error("Unable to load OUI data file {}, missing file content", fileName);
            ret = new GenericResponse(false, "missing compressed data file content");
        } else {
            try {
                ret = manufacturerDatastore.uploadOuiDataFile(fileName, gzippedContent);
            } catch (IllegalArgumentException e) {
                ret = new GenericResponse(false, "invalid encoded data file content");
            }
        }
        LOG.debug("uploadOuiDataFile({}) returns {}", fileName, ret);
        return ret;
    }

    @GetMapping(value = "/oui/list")
    public Map<String, ManufacturerOuiDetails> getManufacturerDetailsForOuiList(
            @RequestParam List<String> ouiList) {
        LOG.debug("calling getManufacturerDetailsForOuiList ");
        return manufacturerDatastore.getManufacturerDetailsForOuiList(ouiList);
    }
    
    @PutMapping(value = "/oui/alias")
    public ManufacturerOuiDetails updateOuiAlias(@RequestBody ManufacturerOuiDetails ouiDetails) {
        LOG.debug("Updating alias for oui {} to {}", ouiDetails.getOui(), ouiDetails.getManufacturerAlias());
        long mfrDetailsId = manufacturerDatastore.getManufacturerDetialsId(ouiDetails.getOui());
        
        if (mfrDetailsId != -1) {
            ManufacturerDetailsRecord rec = manufacturerDatastore.getById(mfrDetailsId);
            rec.setManufacturerAlias(ouiDetails.getManufacturerAlias());
            manufacturerDatastore.updateManufacturerDetails(rec);
            return ouiDetails;
        }
        
        throw new DsEntityNotFoundException("Unable to update alias as no OUI is in the datastore for " + ouiDetails.getOui());
    }

    @GetMapping(value = "/oui/alias")
    public List<String> getStoredAliasValuesThatBeginWith(@RequestParam String prefix, @RequestParam int maxResults) {
        LOG.debug("Retrieving alias values that begin with {}", prefix);
        return manufacturerDatastore.getAliasValuesThatBeginWith(prefix, maxResults);
    }
}
