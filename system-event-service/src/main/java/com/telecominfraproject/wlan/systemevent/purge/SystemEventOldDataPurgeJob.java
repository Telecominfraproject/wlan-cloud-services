package com.telecominfraproject.wlan.systemevent.purge;

import java.util.concurrent.atomic.AtomicBoolean;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.telecominfraproject.wlan.systemevent.datastore.SystemEventDatastore;

/**
 * Purges system event records older than configured time.
 * 
 * @author dtop
 *
 */
@Component
@EnableScheduling
@Profile("purgeOldSystemEvents")
public class SystemEventOldDataPurgeJob {

    private static final Logger LOG = LoggerFactory.getLogger(SystemEventOldDataPurgeJob.class);

    @Autowired private SystemEventDatastore systemEventDatastore;

    private AtomicBoolean jobInProgress = new AtomicBoolean();
    
    @Value("${tip.wlan.purgeSystemEventsOlderThanSec:14400}") //default is 4 hours
    private long purgeSystemEventsOlderThanSec;


    @Scheduled(initialDelay=1400000, fixedDelay=3600000)
    public void scheduleScanRuleAgentQueueAssignements(){
       
       LOG.info("SystemEventOldDataPurgeJob periodic task started");
       
           
       if(!jobInProgress.compareAndSet(false, true)){
           LOG.info("another SystemEventOldDataPurgeJob was started before this one, will not start a new one ontil the old one is done");
           return;
       }

       try{
           performPurge();
       } catch(Exception e) {
           LOG.warn("exception when performing SystemEventOldDataPurgeJob", e);
       } finally {
    	   jobInProgress.set(false);
           LOG.info("SystemEventOldDataPurgeJob completed");
       }
    }

	private void performPurge() {
		if(purgeSystemEventsOlderThanSec>60) {
	        LOG.info("Deleting SystemEvents older than {} sec", purgeSystemEventsOlderThanSec);
	        
			systemEventDatastore.delete(System.currentTimeMillis() - (purgeSystemEventsOlderThanSec * 1000) );
		}
	}  
}
