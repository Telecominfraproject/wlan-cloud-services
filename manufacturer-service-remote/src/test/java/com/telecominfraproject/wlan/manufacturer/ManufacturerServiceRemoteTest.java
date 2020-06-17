package com.telecominfraproject.wlan.manufacturer;


import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.zip.GZIPOutputStream;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.util.Base64Utils;

import com.telecominfraproject.wlan.core.model.json.GenericResponse;
import com.telecominfraproject.wlan.datastore.exceptions.DsConcurrentModificationException;
import com.telecominfraproject.wlan.datastore.exceptions.DsEntityNotFoundException;
import com.telecominfraproject.wlan.manufacturer.models.ManufacturerDetailsRecord;
import com.telecominfraproject.wlan.manufacturer.models.ManufacturerOuiDetails;
import com.telecominfraproject.wlan.remote.tests.BaseRemoteTest;

/**
 * @author dtoptygin
 *
 */
@RunWith(SpringRunner.class)
@ActiveProfiles(profiles = {
        "integration_test",
        "no_ssl","http_digest_auth","rest-template-single-user-per-service-digest-auth",
        }) //NOTE: these profiles will be ADDED to the list of active profiles  
public class ManufacturerServiceRemoteTest extends BaseRemoteTest {

    
    @Autowired ManufacturerServiceRemote remoteInterface; 
    
    @Before public void urlSetup(){
        configureBaseUrl("tip.wlan.manufacturerServiceBaseUrl");
    }
    
    @Test
    public void testClientManufacturerDetailsCRUD() {
        final String companyName1 = "Complex Company Inc.";
        final String companyAlias1 = "Complex";                
        
        //CREATE test
        ManufacturerDetailsRecord manufacturerDetails = new ManufacturerDetailsRecord();
        manufacturerDetails.setManufacturerName(companyName1);
        manufacturerDetails.setManufacturerAlias(companyAlias1);
        
        ManufacturerDetailsRecord ret = remoteInterface.createManufacturerDetails(manufacturerDetails);
        
        //GET by id
        ret = remoteInterface.getById(ret.getId());    
        assertTrue(manufacturerDetails.equals(ret));
        
        // GET Alias starts with test.
        List<String> aliasList = remoteInterface.getAliasValuesThatBeginWith("Com", -1);
        assertTrue(aliasList.size() == 1);
        assertTrue(aliasList.get(0).equals(companyAlias1));
        aliasList = remoteInterface.getAliasValuesThatBeginWith("Sim", 2000);
        assertTrue(aliasList.size() == 0);
        
        //UPDATE test - success
        manufacturerDetails = ret;
        manufacturerDetails.setManufacturerAlias("New Alias");
        ret = remoteInterface.updateManufacturerDetails(manufacturerDetails);
        assertTrue(manufacturerDetails.equals(ret));
        
        //UPDATE test - fail because of concurrent modification exception
        try{
            ManufacturerDetailsRecord manufacturerDetailsConcurrentUpdate = manufacturerDetails.clone();
            manufacturerDetailsConcurrentUpdate.setLastModifiedTimestamp(manufacturerDetailsConcurrentUpdate.getLastModifiedTimestamp()-1);
            manufacturerDetailsConcurrentUpdate.setManufacturerAlias("This should not work");
            remoteInterface.updateManufacturerDetails(manufacturerDetailsConcurrentUpdate);
            fail("failed to detect concurrent modification");
        }catch (DsConcurrentModificationException e) {
            // expected it
        }
        
        //DELETE Test
        remoteInterface.deleteManufacturerDetails(ret.getId());
        
        try{
            remoteInterface.deleteManufacturerDetails(ret.getId());
            fail("failed to delete ClientManufacturerDetails");
        }catch (Exception e) {
            // expected it
        }
    }
    
    @Test
    public void testOuiDatastoreOperations() throws Exception {
        final String companyName1 = "Complex Company Inc.";
        final String companyName2 = "Simple Co.";
        final String companyName3 = "Simplor Co.";
        
        final String companyAlias1 = "Complex";
        final String companyAlias2 = "Simple";
        final String companyAlias3 = null;
        
        final String oui1 = "0000a1";
        final String oui2 = "0000a2";
        final String oui3 = "0000a3";
             
        // CREATE test
        ManufacturerOuiDetails ouiDetails1 = new ManufacturerOuiDetails();
        ouiDetails1.setOui(oui1);
        ouiDetails1.setManufacturerName(companyName1);
        ouiDetails1.setManufacturerAlias(companyAlias1);
        
        ManufacturerOuiDetails ret1 = remoteInterface.createOuiDetails(ouiDetails1);
        assertTrue(ouiDetails1.equals(ret1));
        
        // GET by oui test
        ret1 = remoteInterface.getByOui(oui1);
        assertTrue(ouiDetails1.equals(ret1));
        
        // GET by manufacturer tests
        ManufacturerOuiDetails ouiDetails2 = new ManufacturerOuiDetails();
        ouiDetails2.setOui(oui2);
        ouiDetails2.setManufacturerName(companyName2);
        ouiDetails2.setManufacturerAlias(companyAlias2);
        ManufacturerOuiDetails ret2 = remoteInterface.createOuiDetails(ouiDetails2);
        
        ManufacturerOuiDetails ouiDetails3 = new ManufacturerOuiDetails();
        ouiDetails3.setOui(oui3);
        ouiDetails3.setManufacturerName(companyName3);
        ouiDetails3.setManufacturerAlias(companyAlias3);
        ManufacturerOuiDetails ret3 = remoteInterface.createOuiDetails(ouiDetails3);
        
        List<String> ouiResultList = remoteInterface.getOuiListForManufacturer(companyName1, true);
        assertTrue(ouiResultList.size() == 1);
        assertTrue(ouiDetails1.equals(remoteInterface.getByOui(ouiResultList.get(0))));
        
        ouiResultList = remoteInterface.getOuiListForManufacturer("Bad Name", true);
        assertTrue(ouiResultList.size() == 0);
        
        ouiResultList = remoteInterface.getOuiListForManufacturer("Compl", false);
        assertTrue(ouiResultList.size() == 1);
        assertTrue(ouiDetails1.equals(remoteInterface.getByOui(ouiResultList.get(0))));
        
        ouiResultList = remoteInterface.getOuiListForManufacturer("ompl", false);
        assertTrue(ouiResultList.size() == 0);
        
        ouiResultList = remoteInterface.getOuiListForManufacturer("Simp", false);
        assertTrue(ouiResultList.size() == 2);
        for (int i = 0 ; i < ouiResultList.size(); i++) {
            if (ouiResultList.get(i).equals(oui2)) {
                assertTrue(ouiDetails2.equals(remoteInterface.getByOui(ouiResultList.get(i))));
            } else if (ouiResultList.get(i).equals(oui3)) {
                assertTrue(ouiDetails3.equals(remoteInterface.getByOui(ouiResultList.get(i))));
            } else {
                fail("Unknown OUI was found: " + ouiResultList.get(i));
            }
        }
        
        // GET ALL Manufacturer data test
        List<ManufacturerOuiDetails> allManufacturerData = remoteInterface.getAllManufacturerData();
        assertEquals(3, allManufacturerData.size());
        for (int i = 0 ; i < allManufacturerData.size(); i++) {
            if (allManufacturerData.get(i).getOui().equals(oui2)) {
                assertTrue(ouiDetails2.equals(allManufacturerData.get(i)));
            } else if (allManufacturerData.get(i).getOui().equals(oui3)) {
                assertTrue(ouiDetails3.equals(allManufacturerData.get(i)));
            } else if (allManufacturerData.get(i).getOui().equals(oui1)) {
                assertTrue(ouiDetails1.equals(allManufacturerData.get(i)));
            } else {
                if (allManufacturerData.get(i).getManufacturerName().equals("testname") && allManufacturerData.get(i).getManufacturerAlias().equals("testalias")) {
                    // This is the test record from the test data resources.
                    continue;
                }
                fail("Unknown OUI was found: " + ouiResultList.get(i));
            }
        }
        
        // GET by OUI list test:
        Map<String, ManufacturerOuiDetails> ouiListSearchResult = remoteInterface.getManufacturerDetailsForOuiSet(null);
        assertTrue(ouiListSearchResult.size() == 0);
        
        Set<String> ouiList = new HashSet<>();
        ouiList.add(oui1);
        ouiListSearchResult = remoteInterface.getManufacturerDetailsForOuiSet(ouiList);
        assertTrue(ouiListSearchResult.size() == 1);
        assertTrue(ouiListSearchResult.get(oui1).equals(ret1));
        
        ouiList.add(oui2);
        ouiListSearchResult = remoteInterface.getManufacturerDetailsForOuiSet(ouiList);
        assertTrue(ouiListSearchResult.size() == 2);
        assertTrue(ouiListSearchResult.get(oui1).equals(ret1));
        assertTrue(ouiListSearchResult.get(oui2).equals(ret2));
        
        ouiList.add(oui3);
        ouiListSearchResult = remoteInterface.getManufacturerDetailsForOuiSet(ouiList);
        assertTrue(ouiListSearchResult.size() == 3);
        assertTrue(ouiListSearchResult.get(oui1).equals(ret1));
        assertTrue(ouiListSearchResult.get(oui2).equals(ret2));
        assertTrue(ouiListSearchResult.get(oui3).equals(ret3));
        
        for(int i = 0 ; i < 900; i++) {
            ouiList.add(String.format("%06d", i));
        }
        ouiListSearchResult = remoteInterface.getManufacturerDetailsForOuiSet(ouiList);
        assertEquals(3,ouiListSearchResult.size());
        assertTrue(ouiListSearchResult.get(oui1).equals(ret1));
        assertTrue(ouiListSearchResult.get(oui2).equals(ret2));
        assertTrue(ouiListSearchResult.get(oui3).equals(ret3));

        // CREATE without manufacturer fail test
        ManufacturerOuiDetails badCreate = new ManufacturerOuiDetails();
        badCreate.setOui(oui3);
        badCreate.setManufacturerName(null);
        badCreate.setManufacturerAlias(companyAlias2);
        try {
            remoteInterface.createOuiDetails(badCreate);
            fail("Should not be able to create OUI details with a null manufacturer name.");
        } catch (Exception e) {
            // Expected
        }
        
        badCreate.setManufacturerName("");
        try {
            remoteInterface.createOuiDetails(badCreate);
            fail("Should not be able to create OUI details with a blank manufacturer name.");
        } catch (Exception e) {
            // Expected
        }
        
        // CREATE test where OUI already exists.
        badCreate.setManufacturerName("Something that would work");
        try {
            remoteInterface.createOuiDetails(badCreate);
            fail("Should not be able to create OUI details for a OUI alread in the datastore.");
        } catch (Exception e) {
            // Expected
        }
        
        // UPDATE alias test
        ManufacturerOuiDetails update = new ManufacturerOuiDetails();
        update.setOui(oui1);
        update.setManufacturerName(companyName1);
        update.setManufacturerAlias("UpdatedAlias");
        remoteInterface.updateOuiAlias(update);
        ManufacturerOuiDetails aliasUpdate = remoteInterface.getByOui(oui1);
        assertTrue(aliasUpdate.getManufacturerAlias().equals("UpdatedAlias"));
        
        // GET Alias starts with test.
        List<String> aliasList = remoteInterface.getAliasValuesThatBeginWith("Upda", -1);
        assertTrue(aliasList.size() == 1);
        assertTrue(aliasList.get(0).equals("UpdatedAlias"));
        aliasList = remoteInterface.getAliasValuesThatBeginWith("Sim", 2000);
        assertTrue(aliasList.size() == 1);
        
        // DELETE test
        remoteInterface.deleteOuiDetails(ret1.getOui());
        
        try{
            remoteInterface.deleteOuiDetails(ret1.getOui());
            fail("failed to delete ClientOuiDetails");
        }catch (DsEntityNotFoundException e) {
            // expected it
        }
        
        // Clean up:
        remoteInterface.deleteOuiDetails(ret2.getOui());
        remoteInterface.deleteOuiDetails(ret3.getOui());
    }
    
    @Test
    public void testBadUpload() throws Exception {
        // Try to upload a invalid file
        GenericResponse result = remoteInterface.uploadOuiDataFile("This is Bad Data", new byte[] { 0x1, 0x2 });
        assertFalse("uploaded bad file", result.isSuccess());
    }

    @Test
    public void testPopulateOuiDatastore() throws Exception {
        Map<String, String> knownTestMacs = new HashMap<>();
        knownTestMacs.put("bc3aea", "GUANGDONG OPPO MOBILE TELECOMMUNICATIONS CORP.,LTD");
        knownTestMacs.put("e8bba8", "GUANGDONG OPPO MOBILE TELECOMMUNICATIONS CORP.,LTD");
        knownTestMacs.put("8c0ee3", "GUANGDONG OPPO MOBILE TELECOMMUNICATIONS CORP.,LTD");
        knownTestMacs.put("7c4ca5", "BSkyB Ltd");
        knownTestMacs.put("0012f2", "Brocade Communications Systems, Inc.");
        knownTestMacs.put("001bed", "Brocade Communications Systems, Inc.");
        knownTestMacs.put("002438", "Brocade Communications Systems, Inc.");
        
        String fileName = "test-oui.txt";
        InputStream inFile =  ManufacturerServiceRemoteTest.class.getResource(fileName).openStream();
        ByteArrayOutputStream compressedStream = new ByteArrayOutputStream();
        GZIPOutputStream gzOut = new GZIPOutputStream(compressedStream);
        byte[] buffer = new byte[512];
        while(inFile.available() > 0) {
            int read = inFile.read(buffer);
            gzOut.write(buffer, 0, read);
        }
        gzOut.finish();
        byte[] base64GzippedContent = Base64Utils.encode(compressedStream.toByteArray());

        remoteInterface.uploadOuiDataFile(fileName, base64GzippedContent);
        List<ManufacturerOuiDetails> entireMfrDatastore = remoteInterface.getAllManufacturerData();
        
        assertEquals(knownTestMacs.size(), entireMfrDatastore.size());
        for (ManufacturerOuiDetails record : entireMfrDatastore) {
            if (knownTestMacs.containsKey(record.getOui())) {
                if (record.getManufacturerName().equals(knownTestMacs.get(record.getOui()))) {
                    continue;
                }
                fail("Incorrect mfr name found. Expected: " + knownTestMacs.get(record.getOui()) + ", found: " + record.getManufacturerName());
            } else {
                if (record.getManufacturerName().equals("testname") && record.getManufacturerAlias().equals("testalias")) {
                    // This is the test record from the test data resources.
                    continue;
                }
                fail("Unknown OUI was found: " + record);
            }
        }
        
        // Populate it again with the same file, verify the results are the same
        remoteInterface.uploadOuiDataFile(fileName, base64GzippedContent);
        entireMfrDatastore.clear();
        
        entireMfrDatastore = remoteInterface.getAllManufacturerData();
        assertEquals(knownTestMacs.size(), entireMfrDatastore.size());
        for (ManufacturerOuiDetails record : entireMfrDatastore) {
            if (knownTestMacs.containsKey(record.getOui())) {
                if (record.getManufacturerName().equals(knownTestMacs.get(record.getOui()))) {
                    continue;
                }
                fail("Incorrect mfr name found. Expected: " + knownTestMacs.get(record.getOui()) + ", found: " + record.getManufacturerName());
            } else {
                if (record.getManufacturerName().equals("testname") && record.getManufacturerAlias().equals("testalias")) {
                    // This is the test record from the test data resources.
                    continue;
                }
                fail("Unknown OUI was found: " + record);
            }
        }
        
        // Clean up:
        for(ManufacturerOuiDetails record : entireMfrDatastore) {
            if (knownTestMacs.containsKey(record.getOui())) {
                remoteInterface.deleteOuiDetails(record.getOui());
            } else {
                if (record.getManufacturerName().equals("testname") && record.getManufacturerAlias().equals("testalias")) {
                    // This is the test record from the test data resources.
                    continue;
                }
                fail("Unknown OUI was found: " + record);
            }
        }
        
    }

}
