package com.telecominfraproject.wlan.manufacturer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.CollectionUtils;

import com.telecominfraproject.wlan.core.model.json.GenericResponse;
import com.telecominfraproject.wlan.manufacturer.controller.ManufacturerController;
import com.telecominfraproject.wlan.manufacturer.models.ManufacturerDetailsRecord;
import com.telecominfraproject.wlan.manufacturer.models.ManufacturerOuiDetails;

/**
 * @author mpreston
 *
 */
@Configuration
public class ManufacturerServiceLocal implements ManufacturerInterface {
    @Autowired
    private ManufacturerController mfrController;
    private static final Logger LOG = LoggerFactory.getLogger(ManufacturerServiceLocal.class);
    
    @Override
    public ManufacturerOuiDetails createOuiDetails(ManufacturerOuiDetails ouiDetails) {
        LOG.debug("calling mfrController.createOuiDetails {} ", ouiDetails);
        return mfrController.createOuiDetails(ouiDetails);
    }
    
    @Override
    public ManufacturerOuiDetails deleteOuiDetails(String oui) {
        LOG.debug("calling mfrController.deleteOuiDetails {} ", oui);
        return mfrController.deleteOuiDetails(oui);
    }
    
    @Override
    public List<String> getOuiListForManufacturer(String manufacturer, boolean exactMatch) {
        LOG.debug("calling mfrController.getOuiListForManufacturer {}, {} ", manufacturer, exactMatch);
        return mfrController.getOuiListForManufacturer(manufacturer, exactMatch);
    }
    
    @Override
    public ManufacturerOuiDetails getByOui(String oui) {
        LOG.debug("calling mfrController.getByOui {} ", oui);
        return mfrController.getByOui(oui);
    }
    
    @Override
    public ManufacturerDetailsRecord createManufacturerDetails(ManufacturerDetailsRecord clientManufacturerDetails) {
        LOG.debug("calling mfrController.createManufacturerDetails {} ", clientManufacturerDetails);
        return mfrController.createManufacturerDetails(clientManufacturerDetails);
    }
    
    @Override
    public ManufacturerDetailsRecord updateManufacturerDetails(ManufacturerDetailsRecord clientManufacturerDetails) {
        LOG.debug("calling mfrController.updateManufacturerDetails {} ", clientManufacturerDetails);
        return mfrController.updateManufacturerDetails(clientManufacturerDetails);
    }
    
    @Override
    public ManufacturerDetailsRecord deleteManufacturerDetails(long id) {
        LOG.debug("calling mfrController.deleteManufacturerDetails {} ", id);
        return mfrController.deleteManufacturerDetails(id);
    }
    
    @Override
    public ManufacturerDetailsRecord getById(long id) {
        LOG.debug("calling mfrController.getById {} ", id);
        return mfrController.getById(id);
    }
    
    @Override
    public List<ManufacturerOuiDetails> getAllManufacturerData() {
        LOG.debug("calling mfrController.getAllManufacturerData()");
        return mfrController.getAllManufacturerData();
    }

    @Override
    public GenericResponse uploadOuiDataFile(String fileName, byte[] base64GzippedContent) {
        return mfrController.uploadOuiDataFile(fileName, base64GzippedContent);
    }

    @Override
    public Map<String, ManufacturerOuiDetails> getManufacturerDetailsForOuiSet(Set<String> ouiSet) {
        LOG.debug("calling mfrController.getManufacturerDetailsForOuiList ");
        if (CollectionUtils.isEmpty(ouiSet)) {
            return new HashMap<>();
        }
        
        return mfrController.getManufacturerDetailsForOuiList(new ArrayList<>(ouiSet));
    }

    @Override
    public ManufacturerOuiDetails updateOuiAlias(ManufacturerOuiDetails ouiDetails) {
        return mfrController.updateOuiAlias(ouiDetails);
    }

    @Override
    public List<String> getAliasValuesThatBeginWith(String prefix, int maxResults) {
        return mfrController.getStoredAliasValuesThatBeginWith(prefix, maxResults);
    }
    
}