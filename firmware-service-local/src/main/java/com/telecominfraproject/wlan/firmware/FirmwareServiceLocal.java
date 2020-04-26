package com.telecominfraproject.wlan.firmware;

import java.util.List;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.telecominfraproject.wlan.core.model.pagination.ColumnAndSort;
import com.telecominfraproject.wlan.core.model.pagination.PaginationContext;
import com.telecominfraproject.wlan.core.model.pagination.PaginationResponse;

import com.telecominfraproject.wlan.firmware.controller.FirmwareController;
import com.telecominfraproject.wlan.firmware.models.Firmware;

/**
 * @author dtoptygin
 *
 */
@Component
public class FirmwareServiceLocal implements FirmwareServiceInterface {

    @Autowired private FirmwareController firmwareController;
    private static final Logger LOG = LoggerFactory.getLogger(FirmwareServiceLocal.class);

    
    @Override
    public Firmware create(Firmware firmware) {
        LOG.debug("calling firmwareController.create {} ", firmware);
        return firmwareController.create(firmware);
    }

    @Override
    public Firmware get(long firmwareId) {
        LOG.debug("calling firmwareController.get {} ", firmwareId);
        return firmwareController.get(firmwareId);
    }
    
    @Override
    public Firmware getOrNull(long firmwareId) {
        LOG.debug("calling firmwareController.getOrNull {} ", firmwareId);
        return firmwareController.getOrNull(firmwareId);
    }
    
    @Override
    public List<Firmware> get(Set<Long> firmwareIdSet) {
        LOG.debug("calling firmwareController.getAllInSet {} ", firmwareIdSet);
        return firmwareController.getAllInSet(firmwareIdSet);
    }
    
    @Override
    public PaginationResponse<Firmware> getForCustomer(int customerId, List<ColumnAndSort> sortBy,
    		PaginationContext<Firmware> context) {
        LOG.debug("calling firmwareController.getForCustomer {} ", customerId);
        return firmwareController.getForCustomer(customerId, sortBy, context);
    }

    @Override
    public Firmware update(Firmware firmware) {
        LOG.debug("calling firmwareController.update {} ", firmware);
        return firmwareController.update(firmware);
    }

    @Override
    public Firmware delete(long firmwareId) {
        LOG.debug("calling firmwareController.delete {} ", firmwareId);
        return firmwareController.delete(firmwareId);
    }

}
