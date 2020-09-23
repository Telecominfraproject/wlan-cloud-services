package com.telecominfraproject.wlan.streams.equipmentalarms;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit4.SpringRunner;

import com.telecominfraproject.wlan.alarm.AlarmServiceInterface;
import com.telecominfraproject.wlan.alarm.AlarmServiceLocal;
import com.telecominfraproject.wlan.alarm.controller.AlarmController;
import com.telecominfraproject.wlan.alarm.datastore.inmemory.AlarmDatastoreInMemory;
import com.telecominfraproject.wlan.alarm.models.Alarm;
import com.telecominfraproject.wlan.alarm.models.AlarmCode;
import com.telecominfraproject.wlan.cloudeventdispatcher.CloudEventDispatcherEmpty;
import com.telecominfraproject.wlan.servicemetric.apnode.models.ApNodeMetrics;
import com.telecominfraproject.wlan.servicemetric.apnode.models.ApPerformance;
import com.telecominfraproject.wlan.servicemetric.models.ServiceMetric;
import com.telecominfraproject.wlan.status.StatusServiceLocal;
import com.telecominfraproject.wlan.status.controller.StatusController;
import com.telecominfraproject.wlan.status.datastore.inmemory.StatusDatastoreInMemory;
import com.telecominfraproject.wlan.stream.StreamInterface;
import com.telecominfraproject.wlan.stream.StreamMessageDispatcher;
import com.telecominfraproject.wlan.streams.simple.SimpleStreamsConfig;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.NONE, classes = EquipmentAlarmsProcessor.class)
@Import(value = { EquipmentAlarmsProcessor.class, SimpleStreamsConfig.class, StreamMessageDispatcher.class,
        AlarmServiceLocal.class, AlarmController.class, AlarmDatastoreInMemory.class,
        StatusServiceLocal.class, StatusController.class, StatusDatastoreInMemory.class,
        CloudEventDispatcherEmpty.class,
        //EquipmentAlarmsProcessorTests.Config.class,
        })
public class EquipmentAlarmsProcessorTests {

    private static final long testTimeBucketMs = 200;
    private static final long testCheckAlarmsIntervalMs = 50;

    static {
        System.setProperty("tip.wlan.equipmentAlarmProcessor.checkAlarmsIntervalMs", "" + testCheckAlarmsIntervalMs);
        System.setProperty("tip.wlan.equipmentAlarmProcessor.timeBucketMs", "" + testTimeBucketMs);
    }

    @Autowired 
    StreamInterface<ServiceMetric> metricStreamInterface;
    
    @Autowired 
    EquipmentAlarmsProcessor equipmentAlarmsProcessor;

    @Autowired 
    AlarmServiceInterface alarmService;

    protected static final AtomicLong testSequence = new AtomicLong(1);
        
    @Configuration
    public static class Config {

        //Another way of configuring dependencies (instead of mentioning them in the pom.xml with the test scope) :
//        @Bean
//        AlarmServiceInterface getAlarmServiceInterface() {
//            return new AlarmServiceInterface() {
//                ...
//            };
//        }
        
//        @Bean
//        StatusServiceInterface getStatusServiceInterface() {
//            return new StatusServiceInterface() {
//                ...
//            };
//        }

    }

    @Test
    public void testCPUTemperatureAlarm() {
        int customerId = getNextCustomerId();        
        long equipmentId = getNextEquipmentId();
        
        ServiceMetric record = new ServiceMetric(customerId , equipmentId );
        //create metric a bit in the future so that it gets picked up by the processor and not simply discarded
        record.setCreatedTimestamp(System.currentTimeMillis() + 2 * testTimeBucketMs);
        
        ApNodeMetrics apNodeMetrics = new ApNodeMetrics();
        record.setDetails(apNodeMetrics);
        
        ApPerformance apPerformance = new ApPerformance();
        apNodeMetrics.setApPerformance(apPerformance);

        //we will force the CPUTemperature alarm to be raised
        apPerformance.setCpuTemperature(85);
        
        apPerformance.setCpuUtilized(new int[] {  70, 70 });

        apPerformance.setFreeMemory(30000000);
        
        //publish metric that should trigger alarm for the CPU Temperature
        metricStreamInterface.publish(record );
        
        //wait for the metric to be processed
        sleep(2 * testTimeBucketMs);
        
        //verify that alarm was raised
        List<Alarm> alarms = alarmService.get(customerId, Collections.singleton(equipmentId), Collections.singleton(AlarmCode.CPUTemperature));
        assertEquals(1, alarms.size());
        
        //Now create a metric that should clear the alarm
        ServiceMetric recordToClearAlarm = record.clone();
        recordToClearAlarm.setCreatedTimestamp(System.currentTimeMillis() + 2* testTimeBucketMs );
        ((ApNodeMetrics)recordToClearAlarm.getDetails()).getApPerformance().setCpuTemperature(70);
        
        //publish metric that should clear the alarm for the CPU Temperature
        metricStreamInterface.publish(recordToClearAlarm);

        //wait for the metric to be processed
        sleep(2 * testTimeBucketMs);
        
        //verify that alarm was cleared
        alarms = alarmService.get(customerId, Collections.singleton(equipmentId), Collections.singleton(AlarmCode.CPUTemperature));
        assertEquals(0, alarms.size());

        
    }
    
    
    @Test
    public void testCPUUtilizationAlarm() {
        int customerId = getNextCustomerId();        
        long equipmentId = getNextEquipmentId();
        
        ServiceMetric record = new ServiceMetric(customerId , equipmentId );
        //create metric a bit in the future so that it gets picked up by the processor and not simply discarded
        record.setCreatedTimestamp(System.currentTimeMillis() + 2 * testTimeBucketMs);
        
        ApNodeMetrics apNodeMetrics = new ApNodeMetrics();
        record.setDetails(apNodeMetrics);
        
        ApPerformance apPerformance = new ApPerformance();
        apNodeMetrics.setApPerformance(apPerformance);

        apPerformance.setCpuTemperature(55);
        
        //we will force the CPUUtilization alarm to be raised
        apPerformance.setCpuUtilized(new int[] {  90, 90 });

        apPerformance.setFreeMemory(30000000);
        
        //publish metric that should trigger alarm for the CPU Utilization
        metricStreamInterface.publish(record );
        
        //wait for the metric to be processed
        sleep(2 * testTimeBucketMs);
        
        //verify that alarm was raised
        List<Alarm> alarms = alarmService.get(customerId, Collections.singleton(equipmentId), Collections.singleton(AlarmCode.CPUUtilization));
        assertEquals(1, alarms.size());
        
        //Now create a metric that should clear the alarm
        ServiceMetric recordToClearAlarm = record.clone();
        recordToClearAlarm.setCreatedTimestamp(System.currentTimeMillis() + 2* testTimeBucketMs );
        ((ApNodeMetrics)recordToClearAlarm.getDetails()).getApPerformance().setCpuUtilized(new int[] {  50, 50 });
        
        //publish metric that should clear the alarm for the CPU Utilization
        metricStreamInterface.publish(recordToClearAlarm);

        //wait for the metric to be processed
        sleep(2 * testTimeBucketMs);
        
        //verify that alarm was cleared
        alarms = alarmService.get(customerId, Collections.singleton(equipmentId), Collections.singleton(AlarmCode.CPUUtilization));
        assertEquals(0, alarms.size());
        
    }
    
    public int getNextCustomerId() {
        return (int) testSequence.incrementAndGet();
    }
    
    public long getNextEquipmentId() {
        return testSequence.incrementAndGet();
    }
    
    public void sleep(long ms) {
        try {
            Thread.sleep( ms );
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
    
}
