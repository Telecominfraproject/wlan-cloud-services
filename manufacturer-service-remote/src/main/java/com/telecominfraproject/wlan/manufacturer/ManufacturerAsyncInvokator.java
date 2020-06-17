package com.telecominfraproject.wlan.manufacturer;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.Future;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.stereotype.Component;

import com.telecominfraproject.wlan.manufacturer.models.ManufacturerOuiDetails;

@Component
public class ManufacturerAsyncInvokator {
    private static final Logger LOG = LoggerFactory.getLogger(ManufacturerAsyncInvokator.class);

    @Async //Async methods MUST be defined on separate Beans or Components, otherwise they will not be treated asynchronously
    public Future<Map<String, ManufacturerOuiDetails>> getManufacturerDetailsForOuiSet(ManufacturerServiceRemote manufacturerServiceRemote, Set<String> ouiSet){
        LOG.debug("getManufacturerDetailsForOuiSet(size={}) thread {}",ouiSet.size(), Thread.currentThread().getName());
        try{
            return new AsyncResult<>(manufacturerServiceRemote.getManufacturueDetailsForOuiSetImpl(ouiSet));
        }catch (Exception e) {
            return new AsyncResult<>(new HashMap<>());
        }finally{
        }

        
    }

}
