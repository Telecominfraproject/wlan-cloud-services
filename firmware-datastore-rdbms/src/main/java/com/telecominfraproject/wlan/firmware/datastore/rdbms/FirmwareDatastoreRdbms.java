package com.telecominfraproject.wlan.firmware.datastore.rdbms;

import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

import com.telecominfraproject.wlan.core.model.pagination.ColumnAndSort;
import com.telecominfraproject.wlan.core.model.pagination.PaginationContext;
import com.telecominfraproject.wlan.core.model.pagination.PaginationResponse;

import com.telecominfraproject.wlan.firmware.datastore.FirmwareDatastore;
import com.telecominfraproject.wlan.firmware.models.Firmware;

/**
 * @author dtoptygin
 *
 */
@Configuration
public class FirmwareDatastoreRdbms implements FirmwareDatastore {

    @Autowired FirmwareDAO firmwareDAO;

    @Override
    public Firmware create(Firmware firmware) {
        return firmwareDAO.create(firmware);
    }

    @Override
    public Firmware get(long firmwareId) {
        return firmwareDAO.get(firmwareId);
    }

    @Override
    public Firmware getOrNull(long firmwareId) {
        return firmwareDAO.getOrNull(firmwareId);
    }
    
    @Override
    public Firmware update(Firmware firmware) {
        return firmwareDAO.update(firmware);
    }

    @Override
    public Firmware delete(long firmwareId) {
        return firmwareDAO.delete(firmwareId);
    }
    
    @Override
    public List<Firmware> get(Set<Long> firmwareIdSet) {
    	return firmwareDAO.get(firmwareIdSet);
    }
    
    @Override
    public PaginationResponse<Firmware> getForCustomer(int customerId, List<ColumnAndSort> sortBy,
    		PaginationContext<Firmware> context) {
    	
    	if(context == null) {
    		context = new PaginationContext<>();
    	}

    	return firmwareDAO.getForCustomer( customerId, sortBy, context);
    }
}
