package com.telecominfraproject.wlan.cloudeventdispatcher;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import com.telecominfraproject.wlan.core.client.BaseRemoteClient;
import com.telecominfraproject.wlan.core.model.json.GenericResponse;
import com.telecominfraproject.wlan.servicemetrics.models.SingleMetricRecord;
import com.telecominfraproject.wlan.systemevent.models.SystemEventRecord;

@Component
public class CloudEventDispatcherRemote extends BaseRemoteClient implements CloudEventDispatcherInterface {

    private static final Logger LOG = LoggerFactory.getLogger(CloudEventDispatcherRemote.class);

    private String baseUrl;
    
	@Override
	public void publishMetric(SingleMetricRecord metricRecord) {
		
        // @RequestMapping(value="/metric", method=RequestMethod.POST)
        LOG.debug("calling publishMetric {} ", metricRecord);
        HttpEntity<String> request = new HttpEntity<>( metricRecord.toString(), headers );

        ResponseEntity<GenericResponse> responseEntity = restTemplate.postForEntity(
                getBaseUrl() +"/metric",  request, GenericResponse.class);
        
        GenericResponse ret = responseEntity.getBody();        
        LOG.debug("completed publishMetric {}", ret);

	}

	@Override
	public void publishMetrics(List<SingleMetricRecord> metricRecordList) {
		
        // @RequestMapping(value="/metrics", method=RequestMethod.POST)
        LOG.debug("calling publishMetrics {} ", metricRecordList);
        HttpEntity<String> request = new HttpEntity<>(metricRecordList.toString(), headers);

        ResponseEntity<GenericResponse> responseEntity = restTemplate
                .postForEntity(getBaseUrl() + "/metrics", request, GenericResponse.class);

        GenericResponse ret = responseEntity.getBody();
        LOG.debug("completed publishMetrics {}", ret);

	}

	@Override
	public void publishEvent(SystemEventRecord systemEventRecord) {
    
		//@RequestMapping(value="/event", method=RequestMethod.POST)
        LOG.debug("calling publishEvent {} ", systemEventRecord);

        HttpEntity<String> request = new HttpEntity<>( systemEventRecord.toString(), headers );

        ResponseEntity<GenericResponse> responseEntity = restTemplate.postForEntity(
                getBaseUrl()+"/event", request, GenericResponse.class);
        
        GenericResponse ret = responseEntity.getBody();
        
        LOG.debug("completed publishEvent {} ", ret);

	}

	@Override
	public void publishEvents(List<SystemEventRecord> systemEventRecordList) {

        //@RequestMapping(value="/events", method=RequestMethod.POST)
		LOG.debug("calling publishEvents {} ", systemEventRecordList.size());

        HttpEntity<String> request = new HttpEntity<>( systemEventRecordList.toString(), headers );

        ResponseEntity<GenericResponse> responseEntity = restTemplate.postForEntity(
                getBaseUrl()+"/events", request, GenericResponse.class);
        
        GenericResponse ret = responseEntity.getBody();
        
        LOG.debug("completed publishEvents {} ", ret);

	}

    public String getBaseUrl() {
        if(baseUrl==null){
            baseUrl = environment.getProperty("tip.wlan.cloudEventDispatcherBaseUrl").trim() + "/cloudEventDispatcher";
        }
        return baseUrl;
    }

}
