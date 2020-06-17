package com.telecominfraproject.wlan.manufacturer.datastore.rdbms;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.zip.GZIPInputStream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

import com.telecominfraproject.wlan.core.model.json.GenericResponse;
import com.telecominfraproject.wlan.datastore.exceptions.DsDataValidationException;
import com.telecominfraproject.wlan.manufacturer.datastore.ManufacturerDatastoreInterface;
import com.telecominfraproject.wlan.manufacturer.datastore.ManufacturerDatastoreUtilities;
import com.telecominfraproject.wlan.manufacturer.models.ManufacturerDetailsRecord;
import com.telecominfraproject.wlan.manufacturer.models.ManufacturerOuiDetails;


/**
 * @author mpreston
 *
 */
@Configuration
public class ManufacturerDatastoreRdbms implements ManufacturerDatastoreInterface {
    
    private static final Logger LOG = LoggerFactory.getLogger(ManufacturerDatastoreRdbms.class);

    @Autowired ManufacturerOuiDAO manufacturerOuiDAO;
    @Autowired ManufacturerDetailsDAO manufacturerDetailsDAO;

    @Override
    public ManufacturerOuiDetails createOuiDetails(ManufacturerOuiDetails ouiDetails) {
        LOG.trace("createOuiDetails({})", ouiDetails);
        if (ouiDetails.getManufacturerName() == null || ouiDetails.getManufacturerName().isEmpty()) {
            throw new DsDataValidationException("Unable to create ClientOuiDetails when no Manufacturer name is provided.");
        }
        
        // Check to see if there is already an entry for this OUI:
        ManufacturerOuiDetails result = manufacturerOuiDAO.getByOui(ouiDetails.getOui());
        if (result == null) {
            result = createNewOuiDetails(ouiDetails);
        }
        LOG.trace("createOuiDetails({}) returns {}", ouiDetails, result);
        return result;
    }

    @Override
    public ManufacturerOuiDetails deleteOuiDetails(String oui) {
        return manufacturerOuiDAO.delete(oui);
    }

    @Override
    public List<String> getOuiListForManufacturer(String manufacturer, boolean exactMatch) {
        return manufacturerOuiDAO.getOuiListForManufacturer(manufacturer, exactMatch);
    }

    @Override
    public ManufacturerOuiDetails getByOui(String oui) {
        return manufacturerOuiDAO.getByOui(oui);
    }

    @Override
    public ManufacturerDetailsRecord createManufacturerDetails(ManufacturerDetailsRecord clientManufacturerDetails) {
        return manufacturerDetailsDAO.create(clientManufacturerDetails);
    }

    @Override
    public ManufacturerDetailsRecord updateManufacturerDetails(ManufacturerDetailsRecord clientManufacturerDetails) {
        return manufacturerDetailsDAO.update(clientManufacturerDetails);
    }

    @Override
    public ManufacturerDetailsRecord deleteManufacturerDetails(long id) {
        return manufacturerDetailsDAO.delete(id);
    }

    @Override
    public ManufacturerDetailsRecord getById(long id) {
        return manufacturerDetailsDAO.getById(id);
    }

    @Override
    public List<ManufacturerDetailsRecord> getByManufacturer(String manufacturer, boolean exactMatch) {
        return manufacturerDetailsDAO.getByManufacturer(manufacturer, exactMatch);
    }

    @Override
    public List<ManufacturerOuiDetails> getAllManufacturerData() {
        return manufacturerOuiDAO.getAllManufacturerData();
    }

    @Override
    public GenericResponse uploadOuiDataFile(String filePath, byte[] gzipContent) {
        Pattern ouiPattern = Pattern.compile("^[0-9A-F]{6}");
        Pattern companyPattern = Pattern.compile("[\t ]([^\t]+)$");
        long totalProcessed = 0;
        long importedEntry = 0;
        try {
            InputStream in = new GZIPInputStream(new ByteArrayInputStream(gzipContent));
            BufferedReader input = new BufferedReader(new InputStreamReader(in));

            while (input.ready()) {
                String line = input.readLine();
                Matcher matcher = ouiPattern.matcher(line);

                if (matcher.find()) {
                    ++totalProcessed;
                    String prefix = line.substring(matcher.start(), matcher.end());
                    ManufacturerOuiDetails result = manufacturerOuiDAO.getByOui(prefix);
                    if (null != result) {
                        // already exists
                        continue;
                    }

                    Matcher companyMatcher = companyPattern.matcher(line);

                    if (!companyMatcher.find()) {
                        continue;
                    }
                    String company = line.substring(companyMatcher.start() + 1, companyMatcher.end());
                    ManufacturerOuiDetails addDetails = new ManufacturerOuiDetails();
                    addDetails.setOui(prefix);
                    addDetails.setManufacturerName(company.trim());

                    createNewOuiDetails(addDetails);
                    ++importedEntry;
                }
            }
        } catch (Exception e) {
            return new GenericResponse(false, e.getLocalizedMessage());
        }

        return new GenericResponse(true, "Successfully processed " + totalProcessed
                + " OUI record(s), imported " + importedEntry + " OUI record(s)");
    }

    @Override
    public Map<String, ManufacturerOuiDetails> getManufacturerDetailsForOuiList(List<String> ouiList) {
        return manufacturerOuiDAO.getManufacturerDetailsForOuiList(ouiList);
    }

    @Override
    public long getManufacturerDetialsId(String oui) {
        return manufacturerOuiDAO.getManufacturerDetialsId(oui);
    }

    @Override
    public List<String> getAliasValuesThatBeginWith(String prefix, int maxResults) {
        return manufacturerDetailsDAO.getStoresAliasValuesThatBeginWith(prefix, maxResults);
    }

    private ManufacturerOuiDetails createNewOuiDetails(ManufacturerOuiDetails ouiDetails) {
        // Check to see if there is already Manufacturer details for this
        // manufacturer:
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
            // There was no existing manufacturer details entry. create
            // that,
            // then create the OU entry.
            manufacturerDetails = new ManufacturerDetailsRecord();
            manufacturerDetails.setManufacturerName(manufacturerName);
            manufacturerDetails.setManufacturerAlias(ouiDetails.getManufacturerAlias());
            manufacturerDetails = manufacturerDetailsDAO.create(manufacturerDetails);
        }
        return manufacturerOuiDAO.create(ouiDetails, manufacturerDetails.getId());
    }

}
