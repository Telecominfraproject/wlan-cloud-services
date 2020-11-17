package com.telecominfraproject.wlan.adoptionmetrics;


import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import com.telecominfraproject.wlan.adoptionmetrics.models.ServiceAdoptionMetrics;
import com.telecominfraproject.wlan.core.model.utils.DateTimeUtils;
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
public class AdoptionMetricsServiceRemoteTest extends BaseRemoteTest {

    
    @Autowired AdoptionMetricsServiceRemote remoteInterface; 
    
    @Before public void urlSetup(){
        configureBaseUrl("tip.wlan.adoptionMetricsServiceBaseUrl");
    }
    
    @Test
    public void testUpdateServiceAdoptionMetrics() {
        int customerId_1 = getNextCustomerId();
        long locationId_1 = getNextLocationId();
        long equipmentId_1 = getNextEquipmentId();

        int customerId_2 = getNextCustomerId();
        long locationId_2 = getNextLocationId();
        long equipmentId_2 = getNextEquipmentId();

        long locationId_3 = getNextLocationId();
        long equipmentId_3 = getNextEquipmentId();

        List<ServiceAdoptionMetrics> samList = new ArrayList<>();
        
        samList.add(new ServiceAdoptionMetrics(2020, 1, 2, 11, customerId_1, locationId_1, equipmentId_1, 10, 20, 30));
        samList.add(new ServiceAdoptionMetrics(2020, 1, 2, 11, customerId_1, locationId_1, equipmentId_2, 12, 22, 32));
        samList.add(new ServiceAdoptionMetrics(2020, 1, 2, 11, customerId_1, locationId_2, equipmentId_2, 52, 62, 72));
        samList.add(new ServiceAdoptionMetrics(2020, 1, 2, 12, customerId_1, locationId_1, equipmentId_1, 100, 200, 300));
        samList.add(new ServiceAdoptionMetrics(2020, 1, 2, 12, customerId_1, locationId_1, equipmentId_2, 120, 220, 320));
        samList.add(new ServiceAdoptionMetrics(2020, 1, 2, 12, customerId_2, locationId_3, equipmentId_3, 42, 52, 62));
        
        //create initial service_metrics_collection_config
        remoteInterface.update(samList);

        //verify daily service_metrics_collection_config for equipmentId_1
        List<ServiceAdoptionMetrics> ret = remoteInterface.get(2020, Collections.singleton(equipmentId_1));
        assertEquals(2, ret.size());
        ret.forEach((m) -> {
            assertEquals(1, m.getMonth() );
            assertEquals(2, m.getWeekOfYear() );
            assertTrue(m.getDayOfYear() == 11 || m.getDayOfYear() == 12);
            assertEquals(customerId_1, m.getCustomerId() );
            assertEquals(locationId_1, m.getLocationId() );
            assertEquals(equipmentId_1, m.getEquipmentId() );
            
            assertTrue(m.getNumUniqueConnectedMacs() == 10 || m.getNumUniqueConnectedMacs() == 100);
            assertTrue(m.getNumBytesUpstream() == 20 || m.getNumBytesUpstream() == 200);
            assertTrue(m.getNumBytesDownstream() == 30 || m.getNumBytesDownstream() == 300);
        } );

        
        //verify daily service_metrics_collection_config for equipmentId_2
        ret = remoteInterface.get(2020, Collections.singleton(equipmentId_2));
        assertEquals(3, ret.size());
        ret.forEach((m) -> {
            assertEquals(1, m.getMonth() );
            assertEquals(2, m.getWeekOfYear() );
            assertTrue(m.getDayOfYear() == 11 || m.getDayOfYear() == 12);
            assertEquals(customerId_1, m.getCustomerId() );
            assertTrue(m.getLocationId() == locationId_1 || m.getLocationId() == locationId_2);
            assertEquals(equipmentId_2, m.getEquipmentId() );
            
            assertTrue(m.getNumUniqueConnectedMacs() == 12 || m.getNumUniqueConnectedMacs() == 52 || m.getNumUniqueConnectedMacs() == 120 );
        } );

        ret = remoteInterface.getAggregatePerLocationPerDay(2020, Collections.singleton(locationId_2));
        assertEquals(1, ret.size());
        ret.forEach((m) -> {
            assertEquals(1, m.getMonth() );
            assertEquals(2, m.getWeekOfYear() );
            assertTrue(m.getDayOfYear() == 11 );
            assertEquals(customerId_1, m.getCustomerId() );
            assertTrue(m.getLocationId() == locationId_2);
            assertEquals(0, m.getEquipmentId() );
            
            assertTrue(m.getNumUniqueConnectedMacs() == 52);
            assertTrue(m.getNumBytesUpstream() == 62);
            assertTrue(m.getNumBytesDownstream() == 72);
        } );

        ret = remoteInterface.getAggregatePerLocationPerDay(2020, Collections.singleton(locationId_1));
        assertEquals(2, ret.size());
        ret.forEach((m) -> {
            assertEquals(1, m.getMonth() );
            assertEquals(2, m.getWeekOfYear() );
            assertTrue(m.getDayOfYear() == 11 || m.getDayOfYear() == 12);
            assertEquals(customerId_1, m.getCustomerId() );
            assertTrue(m.getLocationId() == locationId_1);
            assertEquals(0, m.getEquipmentId() );
            
            assertTrue(m.getNumUniqueConnectedMacs() == 22 || m.getNumUniqueConnectedMacs() == 220);
        } );

        ret = remoteInterface.getAggregatePerCustomerPerDay(2020, Collections.singleton(customerId_1));
        assertEquals(2, ret.size());
        ret.forEach((m) -> {
            assertEquals(1, m.getMonth() );
            assertEquals(2, m.getWeekOfYear() );
            assertTrue(m.getDayOfYear() == 11 || m.getDayOfYear() == 12);
            assertEquals(customerId_1, m.getCustomerId() );
            assertEquals(0, m.getLocationId());
            assertEquals(0, m.getEquipmentId() );
            
            assertTrue(m.getNumUniqueConnectedMacs() == 74 || m.getNumUniqueConnectedMacs() == 220);
        } );

        ret = remoteInterface.getAllPerDay(2020);
        assertEquals(2, ret.size());
        ret.forEach((m) -> {
            assertEquals(1, m.getMonth() );
            assertEquals(2, m.getWeekOfYear() );
            assertTrue(m.getDayOfYear() == 11 || m.getDayOfYear() == 12);
            assertEquals(0, m.getCustomerId() );
            assertEquals(0, m.getLocationId());
            assertEquals(0, m.getEquipmentId() );
            
            assertTrue(m.getNumUniqueConnectedMacs() == 74 || m.getNumUniqueConnectedMacs() == 262);
        } );

        ret = remoteInterface.getAllPerMonth(2020);
        assertEquals(1, ret.size());
        ret.forEach((m) -> {
            assertEquals(1, m.getMonth() );
            assertEquals(-1, m.getWeekOfYear() );
            assertEquals(-1, m.getDayOfYear());
            assertEquals(0, m.getCustomerId() );
            assertEquals(0, m.getLocationId());
            assertEquals(0, m.getEquipmentId() );
            
            assertTrue(m.getNumUniqueConnectedMacs() == 336);
        } );

        ret = remoteInterface.getAllPerWeek(2020);
        assertEquals(1, ret.size());
        ret.forEach((m) -> {
            assertEquals(1, m.getMonth() );
            assertEquals(2, m.getWeekOfYear() );
            assertEquals(-1, m.getDayOfYear());
            assertEquals(0, m.getCustomerId() );
            assertEquals(0, m.getLocationId());
            assertEquals(0, m.getEquipmentId() );
            
            assertEquals(336, m.getNumUniqueConnectedMacs());
            assertEquals(576, m.getNumBytesUpstream());
            assertEquals(816, m.getNumBytesDownstream());
            
        } );

        //update one of the entries
        //original entry:
        //samList.add(new ServiceAdoptionMetrics(2020, 1, 2, 12, customerId_2, locationId_3, equipmentId_3, 42, 52, 62));
        //new updated entry:
        samList.clear();
        samList.add(new ServiceAdoptionMetrics(2020, 1, 2, 12, customerId_2, locationId_3, equipmentId_3, 142, 100, 200));
        
        remoteInterface.update(samList);

        ret = remoteInterface.getAllPerWeek(2020);
        assertEquals(1, ret.size());
        ret.forEach((m) -> {
            assertEquals(1, m.getMonth() );
            assertEquals(2, m.getWeekOfYear() );
            assertEquals(-1, m.getDayOfYear());
            assertEquals(0, m.getCustomerId() );
            assertEquals(0, m.getLocationId());
            assertEquals(0, m.getEquipmentId() );
            
            assertEquals(436, m.getNumUniqueConnectedMacs()); // NumUniqueConnectedMacs does not accumulate
            assertEquals(676, m.getNumBytesUpstream()); // this one get incremented during the update
            assertEquals(1016, m.getNumBytesDownstream()); // this one get incremented during the update
            
        } );

    }
    
    @Test
    public void testUpdateUniqueMacs() {
        int customerId_1 = getNextCustomerId();
        long locationId_1 = getNextLocationId();
        long equipmentId_1 = getNextEquipmentId();
    
        Set<Long> clientMacSet_1 = new HashSet<>();
        clientMacSet_1.add(1L);
        clientMacSet_1.add(2L);
        clientMacSet_1.add(3L);
        
        long timestampMs = System.currentTimeMillis();
        
        int year = DateTimeUtils.getYear(timestampMs);
        int dayOfYear = DateTimeUtils.getDayOfYear(timestampMs);
        
        //check that initial count of unique macs for the day is 0
        long uniqMacs = remoteInterface.getUniqueMacsCount(year, dayOfYear, customerId_1, locationId_1, equipmentId_1);
        assertEquals(0, uniqMacs);
        
        //create an initial set of macs for the day
        remoteInterface.updateUniqueMacs(timestampMs , customerId_1, locationId_1, equipmentId_1, clientMacSet_1);
        uniqMacs = remoteInterface.getUniqueMacsCount(year, dayOfYear, customerId_1, locationId_1, equipmentId_1);
        assertEquals(3, uniqMacs);

        //update the set of macs for the day with some duplicates
        clientMacSet_1.add(4L);
        remoteInterface.updateUniqueMacs(timestampMs , customerId_1, locationId_1, equipmentId_1, clientMacSet_1);
        uniqMacs = remoteInterface.getUniqueMacsCount(year, dayOfYear, customerId_1, locationId_1, equipmentId_1);
        assertEquals(4, uniqMacs);

        //delete the set of macs for teh day
        remoteInterface.deleteUniqueMacs(timestampMs + 1 , customerId_1, locationId_1, equipmentId_1);
        uniqMacs = remoteInterface.getUniqueMacsCount(year, dayOfYear, customerId_1, locationId_1, equipmentId_1);
        assertEquals(0, uniqMacs);
    }
    
    @Test
    public void testFinalizeUniqueMacs() {
        int customerId_1 = getNextCustomerId();
        long locationId_1 = getNextLocationId();
        long equipmentId_1 = getNextEquipmentId();
    
        Set<Long> clientMacSet_1 = new HashSet<>();
        clientMacSet_1.add(1L);
        clientMacSet_1.add(2L);
        clientMacSet_1.add(3L);
               
        int year = 2010;
        int dayOfYear = 11;
        
        Calendar calendar = Calendar.getInstance(DateTimeUtils.TZ_GMT);
        calendar.set(Calendar.YEAR, year);
        calendar.set(Calendar.DAY_OF_YEAR, dayOfYear);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 1);  
        
        long timestampMs = calendar.getTimeInMillis();

        List<ServiceAdoptionMetrics> samList = new ArrayList<>();
        
        samList.add(new ServiceAdoptionMetrics(2010, 1, 2, 11, customerId_1, locationId_1, equipmentId_1, 10, 20, 30));
        samList.add(new ServiceAdoptionMetrics(2010, 1, 2, 12, customerId_1, locationId_1, equipmentId_1, 100, 200, 300));
        
        //create initial service_metrics_collection_config
        remoteInterface.update(samList);
        
        List<ServiceAdoptionMetrics> ret = remoteInterface.get(2010, Collections.singleton(equipmentId_1));
        assertEquals(2, ret.size());
        ret.forEach((m) -> {
            assertEquals(1, m.getMonth() );
            assertEquals(2, m.getWeekOfYear() );
            assertTrue(m.getDayOfYear() == 11 || m.getDayOfYear() == 12);
            assertEquals(customerId_1, m.getCustomerId() );
            assertEquals(locationId_1, m.getLocationId() );
            assertEquals(equipmentId_1, m.getEquipmentId() );
            
            assertTrue(m.getNumUniqueConnectedMacs() == 10 || m.getNumUniqueConnectedMacs() == 100);
            assertTrue(m.getNumBytesUpstream() == 20 || m.getNumBytesUpstream() == 200);
            assertTrue(m.getNumBytesDownstream() == 30 || m.getNumBytesDownstream() == 300);
        } );


        //check that initial count of unique macs for the day is 0
        long uniqMacs = remoteInterface.getUniqueMacsCount(year, dayOfYear, customerId_1, locationId_1, equipmentId_1);
        assertEquals(0, uniqMacs);
        
        //create an initial set of macs for the day
        remoteInterface.updateUniqueMacs(timestampMs , customerId_1, locationId_1, equipmentId_1, clientMacSet_1);
        uniqMacs = remoteInterface.getUniqueMacsCount(year, dayOfYear, customerId_1, locationId_1, equipmentId_1);
        assertEquals(3, uniqMacs);

        //update the set of macs for the day with some duplicates
        clientMacSet_1.add(4L);
        remoteInterface.updateUniqueMacs(timestampMs , customerId_1, locationId_1, equipmentId_1, clientMacSet_1);
        uniqMacs = remoteInterface.getUniqueMacsCount(year, dayOfYear, customerId_1, locationId_1, equipmentId_1);
        assertEquals(4, uniqMacs);

        //finalize mac counts for the day
        remoteInterface.finalizeUniqueMacsCount(year, dayOfYear);

        //after finalizing the unique mac counts are cleared
        uniqMacs = remoteInterface.getUniqueMacsCount(year, dayOfYear, customerId_1, locationId_1, equipmentId_1);
        assertEquals(0, uniqMacs);

        //test counts after finalizing unique macs
        ret = remoteInterface.get(2010, Collections.singleton(equipmentId_1));
        assertEquals(2, ret.size());
        ret.forEach((m) -> {
            assertEquals(1, m.getMonth() );
            assertEquals(2, m.getWeekOfYear() );
            assertTrue(m.getDayOfYear() == 11 || m.getDayOfYear() == 12);
            assertEquals(customerId_1, m.getCustomerId() );
            assertEquals(locationId_1, m.getLocationId() );
            assertEquals(equipmentId_1, m.getEquipmentId() );
            
            assertTrue( ( m.getNumUniqueConnectedMacs() == 4 && m.getDayOfYear() == 11 ) || m.getNumUniqueConnectedMacs() == 100 /*this number did not change because there were no unique macs present for that day*/);
            assertTrue(m.getNumBytesUpstream() == 20 || m.getNumBytesUpstream() == 200);
            assertTrue(m.getNumBytesDownstream() == 30 || m.getNumBytesDownstream() == 300);
        } );
        
        //finalize again mac counts for the day - should be the no-op since themacs were cleared by the first finalize attempt
        remoteInterface.finalizeUniqueMacsCount(year, dayOfYear);

        //re-test counts after finalizing unique macs - the counts should remain the same
        ret = remoteInterface.get(2010, Collections.singleton(equipmentId_1));
        assertEquals(2, ret.size());
        ret.forEach((m) -> {
            assertEquals(1, m.getMonth() );
            assertEquals(2, m.getWeekOfYear() );
            assertTrue(m.getDayOfYear() == 11 || m.getDayOfYear() == 12);
            assertEquals(customerId_1, m.getCustomerId() );
            assertEquals(locationId_1, m.getLocationId() );
            assertEquals(equipmentId_1, m.getEquipmentId() );
            
            assertTrue( ( m.getNumUniqueConnectedMacs() == 4 && m.getDayOfYear() == 11 ) || m.getNumUniqueConnectedMacs() == 100  /*this number did not change because there were no unique macs present for that day*/);
            assertTrue(m.getNumBytesUpstream() == 20 || m.getNumBytesUpstream() == 200);
            assertTrue(m.getNumBytesDownstream() == 30 || m.getNumBytesDownstream() == 300);
        } );
        

    }
}
