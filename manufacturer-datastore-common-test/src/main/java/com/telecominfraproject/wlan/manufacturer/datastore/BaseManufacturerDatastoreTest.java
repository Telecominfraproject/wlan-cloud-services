package com.telecominfraproject.wlan.manufacturer.datastore;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.zip.GZIPOutputStream;

import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.telecominfraproject.wlan.core.model.json.GenericResponse;
import com.telecominfraproject.wlan.datastore.exceptions.DsConcurrentModificationException;
import com.telecominfraproject.wlan.manufacturer.models.ManufacturerDetailsRecord;
import com.telecominfraproject.wlan.manufacturer.models.ManufacturerOuiDetails;

/**
 * @author mpreston
 *
 */
public abstract class BaseManufacturerDatastoreTest {
    private static final String TESTNAME = "testname";
    private static final String TESTALIAS = "testalias";

    @Autowired private ManufacturerDatastoreInterface manufacturerDatastore;

    @Before
    public void setUp() {
        // nothing
    }
    
    @Test
    public void testClientManufacturerDetailsCRUD() {
        final String companyName1 = "Complex Company Inc.";
        final String companyName2 = "Simple Co.";
        final String companyName3 = "Simplor Co.";
        final String companyAlias1 = "Complex";
        final String companyAlias2 = "Simple";
        final String companyAlias3 = "Simplor";
                
        
        //CREATE test
        ManufacturerDetailsRecord manufacturerDetails = new ManufacturerDetailsRecord();
        manufacturerDetails.setManufacturerName(companyName1);
        manufacturerDetails.setManufacturerAlias(companyAlias1);
        
        ManufacturerDetailsRecord ret = manufacturerDatastore.createManufacturerDetails(manufacturerDetails);
        
        //GET by id
        ret = manufacturerDatastore.getById(ret.getId());    
        assertTrue(manufacturerDetails.equals(ret));
        
        // GET by manufacturer
        List<ManufacturerDetailsRecord> resultList = manufacturerDatastore.getByManufacturer(companyName1, true);
        assertTrue(manufacturerDetails.equals(resultList.get(0)));
        
        resultList.clear();
        resultList = manufacturerDatastore.getByManufacturer("Compl", false);
        assertTrue(manufacturerDetails.equals(resultList.get(0)));
        
        resultList.clear();
        resultList = manufacturerDatastore.getByManufacturer("mpl", false);
        assertTrue(resultList.isEmpty());
        
        resultList.clear();
        resultList = manufacturerDatastore.getByManufacturer("bad stuff here", false);
        assertTrue(resultList.isEmpty());

        //UPDATE test - success
        manufacturerDetails = ret;
        manufacturerDetails.setManufacturerAlias("New Alias");
        ret = manufacturerDatastore.updateManufacturerDetails(manufacturerDetails);
        assertTrue(manufacturerDetails.equals(ret));
        
        //UPDATE test - fail because of concurrent modification exception
        try{
            ManufacturerDetailsRecord manufacturerDetailsConcurrentUpdate = manufacturerDetails.clone();
            manufacturerDetailsConcurrentUpdate.setLastModifiedTimestamp(manufacturerDetailsConcurrentUpdate.getLastModifiedTimestamp()-1);
            manufacturerDetailsConcurrentUpdate.setManufacturerAlias("This should not work");
            manufacturerDatastore.updateManufacturerDetails(manufacturerDetailsConcurrentUpdate);
            fail("failed to detect concurrent modification");
        }catch (DsConcurrentModificationException e) {
            // expected it
        }
        
        ManufacturerDetailsRecord manufacturerDetails2 = new ManufacturerDetailsRecord();
        manufacturerDetails2.setManufacturerName(companyName2);
        manufacturerDetails2.setManufacturerAlias(companyAlias2);
        ManufacturerDetailsRecord ret2 = manufacturerDatastore.createManufacturerDetails(manufacturerDetails2);
        
        ManufacturerDetailsRecord manufacturerDetails3 = new ManufacturerDetailsRecord();
        manufacturerDetails3.setManufacturerName(companyName3);
        manufacturerDetails3.setManufacturerAlias(companyAlias3);
        ManufacturerDetailsRecord ret3 = manufacturerDatastore.createManufacturerDetails(manufacturerDetails3);
        
        // GET by manufacturer partial list
        resultList.clear();
        resultList = manufacturerDatastore.getByManufacturer("Simp", false);
        assertEquals(2, resultList.size());
        
        resultList.clear();
        resultList = manufacturerDatastore.getByManufacturer("Simple", true);
        assertEquals(1, resultList.size());
        
        resultList.clear();
        resultList = manufacturerDatastore.getByManufacturer("Simple", false);
        assertEquals(1, resultList.size());

        //DELETE Test
        manufacturerDatastore.deleteManufacturerDetails(ret.getId());
        
        try{
            manufacturerDatastore.deleteManufacturerDetails(ret.getId());
            fail("failed to delete ClientManufacturerDetails");
        }catch (Exception e) {
            // expected it
        }
        
        // Clean Up.
        manufacturerDatastore.deleteManufacturerDetails(ret2.getId());
        manufacturerDatastore.deleteManufacturerDetails(ret3.getId());
    }
    
    @Test
    public void testOuiDatastoreOperations() {
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
        
        ManufacturerOuiDetails ret1 = manufacturerDatastore.createOuiDetails(ouiDetails1);
        assertTrue(ouiDetails1.equals(ret1));
        
        // Make sure the details object was properly created as well.
        ManufacturerDetailsRecord manufacturerDetailsRecord1 = manufacturerDatastore.getByManufacturer(companyName1, true).get(0);
        assertTrue(manufacturerDetailsRecord1.getManufacturerName().equals(companyName1));
        assertTrue(manufacturerDetailsRecord1.getManufacturerAlias().equals(companyAlias1));
        
        // GET by oui test
        ret1 = manufacturerDatastore.getByOui(oui1);
        assertTrue(ouiDetails1.equals(ret1));
        
        // GET mfr details id test
        long mfrDetailsId = manufacturerDatastore.getManufacturerDetialsId(oui1);
        ManufacturerDetailsRecord mfrDetialsRecord = manufacturerDatastore.getById(mfrDetailsId);
        assertTrue(mfrDetialsRecord.getManufacturerAlias().equals(ret1.getManufacturerAlias()));
        assertTrue(mfrDetialsRecord.getManufacturerName().equals(ret1.getManufacturerName()));
        
        // GET Alias starts with test.
        List<String> aliasList = manufacturerDatastore.getAliasValuesThatBeginWith("Com", -1);
        assertEquals(1, aliasList.size());
        assertTrue(aliasList.get(0).equals(companyAlias1));
        aliasList = manufacturerDatastore.getAliasValuesThatBeginWith("Sim", 2000);
        assertTrue(aliasList.isEmpty());
        
        
        // GET by manufacturer tests
        ManufacturerOuiDetails ouiDetails2 = new ManufacturerOuiDetails();
        ouiDetails2.setOui(oui2);
        ouiDetails2.setManufacturerName(companyName2);
        ouiDetails2.setManufacturerAlias(companyAlias2);
        ManufacturerOuiDetails ret2 = manufacturerDatastore.createOuiDetails(ouiDetails2);
        
        ManufacturerOuiDetails ouiDetails3 = new ManufacturerOuiDetails();
        ouiDetails3.setOui(oui3);
        ouiDetails3.setManufacturerName(companyName3);
        ouiDetails3.setManufacturerAlias(companyAlias3);
        ManufacturerOuiDetails ret3 = manufacturerDatastore.createOuiDetails(ouiDetails3);
        
        List<String> ouiResultList = manufacturerDatastore.getOuiListForManufacturer(companyName1, true);
        assertEquals(1, ouiResultList.size());
        assertTrue(ouiDetails1.equals(manufacturerDatastore.getByOui(ouiResultList.get(0))));
        
        ouiResultList = manufacturerDatastore.getOuiListForManufacturer("Bad Name", true);
        assertTrue(ouiResultList.isEmpty());
        
        ouiResultList = manufacturerDatastore.getOuiListForManufacturer("Compl", false);
        assertEquals(1, ouiResultList.size());
        assertTrue(ouiDetails1.equals(manufacturerDatastore.getByOui(ouiResultList.get(0))));
        
        ouiResultList = manufacturerDatastore.getOuiListForManufacturer("ompl", false);
        assertTrue(ouiResultList.isEmpty());
        
        ouiResultList = manufacturerDatastore.getOuiListForManufacturer("Simp", false);
        assertEquals(2, ouiResultList.size());
        for (int i = 0 ; i < ouiResultList.size(); i++) {
            if (ouiResultList.get(i).equals(oui2)) {
                assertTrue(ouiDetails2.equals(manufacturerDatastore.getByOui(ouiResultList.get(i))));
            } else if (ouiResultList.get(i).equals(oui3)) {
                assertTrue(ouiDetails3.equals(manufacturerDatastore.getByOui(ouiResultList.get(i))));
            } else {
                fail("Unknown OUI was found: " + ouiResultList.get(i));
            }
        }
        
        // GET ALL Manufacturer data test
        List<ManufacturerOuiDetails> allManufacturerData = manufacturerDatastore.getAllManufacturerData();
        assertEquals(3, allManufacturerData.size());
        for (int i = 0 ; i < allManufacturerData.size(); i++) {
            if (allManufacturerData.get(i).getOui().equals(oui2)) {
                assertTrue(ouiDetails2.equals(allManufacturerData.get(i)));
            } else if (allManufacturerData.get(i).getOui().equals(oui3)) {
                assertTrue(ouiDetails3.equals(allManufacturerData.get(i)));
            } else if (allManufacturerData.get(i).getOui().equals(oui1)) {
                assertTrue(ouiDetails1.equals(allManufacturerData.get(i)));
            } else {
                if (isTestRecord(allManufacturerData.get(i))) {
                    // This is the test record from the test data resources.
                    continue;
                }
                fail("Unknown OUI was found: " + ouiResultList.get(i));
            }
        }
        
        // GET by OUI list test:
        Map<String, ManufacturerOuiDetails> ouiListSearchResult = manufacturerDatastore.getManufacturerDetailsForOuiList(null);
        assertTrue(ouiListSearchResult.isEmpty());
        
        List<String> ouiList = new ArrayList<>();
        ouiList.add(oui1);
        ouiListSearchResult = manufacturerDatastore.getManufacturerDetailsForOuiList(ouiList);
        assertEquals(1, ouiListSearchResult.size());
        assertTrue(ouiListSearchResult.get(oui1).equals(ret1));
        
        ouiList.add(oui2);
        ouiListSearchResult = manufacturerDatastore.getManufacturerDetailsForOuiList(ouiList);
        assertEquals(2, ouiListSearchResult.size());
        assertTrue(ouiListSearchResult.get(oui1).equals(ret1));
        assertTrue(ouiListSearchResult.get(oui2).equals(ret2));
        
        ouiList.add(oui3);
        ouiListSearchResult = manufacturerDatastore.getManufacturerDetailsForOuiList(ouiList);
        assertEquals(3, ouiListSearchResult.size());
        assertTrue(ouiListSearchResult.get(oui1).equals(ret1));
        assertTrue(ouiListSearchResult.get(oui2).equals(ret2));
        assertTrue(ouiListSearchResult.get(oui3).equals(ret3));
        
        // CREATE without manufacturer fail test
        ManufacturerOuiDetails badCreate = new ManufacturerOuiDetails();
        badCreate.setOui(oui3);
        badCreate.setManufacturerName(null);
        badCreate.setManufacturerAlias(companyAlias2);
        try {
            ManufacturerOuiDetails badRet = manufacturerDatastore.createOuiDetails(badCreate);
            fail("Should not be able to create OUI details with a null manufacturer name: " + badRet);
        } catch (Exception e) {
            // Expected
        }
        
        badCreate.setManufacturerName("");
        try {
            ManufacturerOuiDetails badRet = manufacturerDatastore.createOuiDetails(badCreate);
            fail("Should not be able to create OUI details with a blank manufacturer name: " + badRet);
        } catch (Exception e) {
            // Expected
        }
        
        // DELETE test
        manufacturerDatastore.deleteOuiDetails(ret1.getOui());
        
        try{
            manufacturerDatastore.deleteOuiDetails(ret1.getOui());
            fail("failed to delete ClientOuiDetails");
        }catch (Exception e) {
            // expected it
        }
        
        
        // Clean up:
        manufacturerDatastore.deleteOuiDetails(ret2.getOui());
        manufacturerDatastore.deleteOuiDetails(ret3.getOui());
    }
    
    @Test
    public void testBadUpload() {
        // Try to upload a invalid file
        GenericResponse result = manufacturerDatastore.uploadOuiDataFile("This is Bad Data", new byte[] { 0x1, 0x2 });
        assertFalse("uploaded bad file", result.isSuccess());
    }
    
    @Test
    public void testPopulateOuiDatastore() throws IOException {
        Map<String, String> knownTestMacs = new HashMap<>();
        knownTestMacs.put("bc3aea", "GUANGDONG OPPO MOBILE TELECOMMUNICATIONS CORP.,LTD");
        knownTestMacs.put("e8bba8", "GUANGDONG OPPO MOBILE TELECOMMUNICATIONS CORP.,LTD");
        knownTestMacs.put("8c0ee3", "GUANGDONG OPPO MOBILE TELECOMMUNICATIONS CORP.,LTD");
        knownTestMacs.put("7c4ca5", "BSkyB Ltd");
        knownTestMacs.put("0012f2", "Brocade Communications Systems, Inc.");
        knownTestMacs.put("001bed", "Brocade Communications Systems, Inc.");
        knownTestMacs.put("002438", "Brocade Communications Systems, Inc.");
        knownTestMacs.put("8ce748", "Private (8CE748)");
        knownTestMacs.put("0084ed", "Private (0084ED)");
        
        final String filePath = "test-oui.txt";
        InputStream inFile = BaseManufacturerDatastoreTest.class.getResource(filePath).openStream();
        ByteArrayOutputStream compressedStream = new ByteArrayOutputStream();
        GZIPOutputStream gzOut = new GZIPOutputStream(compressedStream);
        byte[] buffer = new byte[512];
        while(inFile.available() > 0) {
            int read = inFile.read(buffer);
            gzOut.write(buffer, 0, read);
        }
        gzOut.finish();
        manufacturerDatastore.uploadOuiDataFile(filePath, compressedStream.toByteArray());
        List<ManufacturerOuiDetails> entireMfrDatastore = manufacturerDatastore.getAllManufacturerData();
        assertNotNull(entireMfrDatastore);
        assertEquals(knownTestMacs.size(), entireMfrDatastore.size());
        for (ManufacturerOuiDetails record : entireMfrDatastore) {
            if (knownTestMacs.containsKey(record.getOui())) {
                if (record.getManufacturerName().equals(knownTestMacs.get(record.getOui()))) {
                    continue;
                }
                fail("Incorrect mfr name found. Expected: " + knownTestMacs.get(record.getOui()) + ", found: " + record.getManufacturerName());
            } else {
                if (isTestRecord(record)) {
                    // This is the test record from the test data resources.
                    continue;
                }
                fail("Unknown OUI was found: " + record);
            }
        }
        
        // Populate it again with the same file, verify the results are the same
        manufacturerDatastore.uploadOuiDataFile(filePath, compressedStream.toByteArray());
        entireMfrDatastore.clear();
        
        entireMfrDatastore = manufacturerDatastore.getAllManufacturerData();
        assertEquals(knownTestMacs.size(), entireMfrDatastore.size());
        for (ManufacturerOuiDetails record : entireMfrDatastore) {
            if (knownTestMacs.containsKey(record.getOui())) {
                if (record.getManufacturerName().equals(knownTestMacs.get(record.getOui()))) {
                    continue;
                }
                fail("Incorrect mfr name found. Expected: " + knownTestMacs.get(record.getOui()) + ", found: " + record.getManufacturerName());
            } else {
                if (isTestRecord(record)) {
                    // This is the test record from the test data resources.
                    continue;
                }
                fail("Unknown OUI was found: " + record);
            }
        }
        
        // Clean up:
        for(ManufacturerOuiDetails record : entireMfrDatastore) {
            if (knownTestMacs.containsKey(record.getOui())) {
                manufacturerDatastore.deleteOuiDetails(record.getOui());
            } else {
                if (isTestRecord(record)) {
                    // This is the test record from the test data resources.
                    continue;
                }
                fail("Unknown OUI was found: " + record);
            }
        }
        
    }

    private boolean isTestRecord(ManufacturerOuiDetails record) {
        return (record != null) && TESTNAME.equals(record.getManufacturerName())
                && TESTALIAS.equals(record.getManufacturerAlias());
    }
}
