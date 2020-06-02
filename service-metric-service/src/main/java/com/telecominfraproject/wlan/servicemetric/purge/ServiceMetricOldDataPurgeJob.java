package com.telecominfraproject.wlan.servicemetric.purge;

import java.util.concurrent.atomic.AtomicBoolean;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.telecominfraproject.wlan.servicemetric.datastore.ServiceMetricDatastore;

/**
 * Purges service metric records older than configured time.
 * 
 * @author dtop
 *
 */
@Component
@EnableScheduling
@Profile("purgeOldServiceMetrics")
public class ServiceMetricOldDataPurgeJob {

    private static final Logger LOG = LoggerFactory.getLogger(ServiceMetricOldDataPurgeJob.class);

    @Autowired private ServiceMetricDatastore serviceMetricDatastore;

    private AtomicBoolean jobInProgress = new AtomicBoolean();
    
    @Value("${tip.wlan.purgeServiceMetricsOlderThanSec:14400}") //default is 4 hours
    private long purgeServiceMetricsOlderThanSec;


    @Scheduled(initialDelay=1200000, fixedDelay=3600000)
    public void scheduleScanRuleAgentQueueAssignements(){
       
       LOG.info("ServiceMetricOldDataPurgeJob periodic task started");
       
           
       if(!jobInProgress.compareAndSet(false, true)){
           LOG.info("another ServiceMetricOldDataPurgeJob was started before this one, will not start a new one ontil the old one is done");
           return;
       }

       try{
           performPurge();
       } catch(Exception e) {
           LOG.warn("exception when performing ServiceMetricOldDataPurgeJob", e);
       } finally {
    	   jobInProgress.set(false);
           LOG.info("ServiceMetricOldDataPurgeJob completed");
       }
    }

	private void performPurge() {
		if(purgeServiceMetricsOlderThanSec>60) {
	        LOG.info("Deleting ServiceMetrics older than {} sec", purgeServiceMetricsOlderThanSec);
	        
			serviceMetricDatastore.delete(System.currentTimeMillis() - (purgeServiceMetricsOlderThanSec * 1000) );
		}
	}  
}
