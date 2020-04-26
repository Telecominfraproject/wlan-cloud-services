package com.telecominfraproject.wlan.manufacturer;

import java.util.List;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.telecominfraproject.wlan.core.model.pagination.ColumnAndSort;
import com.telecominfraproject.wlan.core.model.pagination.PaginationContext;
import com.telecominfraproject.wlan.core.model.pagination.PaginationResponse;

import com.telecominfraproject.wlan.manufacturer.controller.ManufacturerController;
import com.telecominfraproject.wlan.manufacturer.models.Manufacturer;

/**
 * @author dtoptygin
 *
 */
@Component
public class ManufacturerServiceLocal implements ManufacturerServiceInterface {

    @Autowired private ManufacturerController manufacturerController;
    private static final Logger LOG = LoggerFactory.getLogger(ManufacturerServiceLocal.class);

    
    @Override
    public Manufacturer create(Manufacturer manufacturer) {
        LOG.debug("calling manufacturerController.create {} ", manufacturer);
        return manufacturerController.create(manufacturer);
    }

    @Override
    public Manufacturer get(long manufacturerId) {
        LOG.debug("calling manufacturerController.get {} ", manufacturerId);
        return manufacturerController.get(manufacturerId);
    }
    
    @Override
    public Manufacturer getOrNull(long manufacturerId) {
        LOG.debug("calling manufacturerController.getOrNull {} ", manufacturerId);
        return manufacturerController.getOrNull(manufacturerId);
    }
    
    @Override
    public List<Manufacturer> get(Set<Long> manufacturerIdSet) {
        LOG.debug("calling manufacturerController.getAllInSet {} ", manufacturerIdSet);
        return manufacturerController.getAllInSet(manufacturerIdSet);
    }
    
    @Override
    public PaginationResponse<Manufacturer> getForCustomer(int customerId, List<ColumnAndSort> sortBy,
    		PaginationContext<Manufacturer> context) {
        LOG.debug("calling manufacturerController.getForCustomer {} ", customerId);
        return manufacturerController.getForCustomer(customerId, sortBy, context);
    }

    @Override
    public Manufacturer update(Manufacturer manufacturer) {
        LOG.debug("calling manufacturerController.update {} ", manufacturer);
        return manufacturerController.update(manufacturer);
    }

    @Override
    public Manufacturer delete(long manufacturerId) {
        LOG.debug("calling manufacturerController.delete {} ", manufacturerId);
        return manufacturerController.delete(manufacturerId);
    }

}
