package com.telecominfraproject.wlan.manufacturer.datastore.rdbms;

import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

import com.telecominfraproject.wlan.core.model.pagination.ColumnAndSort;
import com.telecominfraproject.wlan.core.model.pagination.PaginationContext;
import com.telecominfraproject.wlan.core.model.pagination.PaginationResponse;

import com.telecominfraproject.wlan.manufacturer.datastore.ManufacturerDatastore;
import com.telecominfraproject.wlan.manufacturer.models.Manufacturer;

/**
 * @author dtoptygin
 *
 */
@Configuration
public class ManufacturerDatastoreRdbms implements ManufacturerDatastore {

    @Autowired ManufacturerDAO manufacturerDAO;

    @Override
    public Manufacturer create(Manufacturer manufacturer) {
        return manufacturerDAO.create(manufacturer);
    }

    @Override
    public Manufacturer get(long manufacturerId) {
        return manufacturerDAO.get(manufacturerId);
    }

    @Override
    public Manufacturer getOrNull(long manufacturerId) {
        return manufacturerDAO.getOrNull(manufacturerId);
    }
    
    @Override
    public Manufacturer update(Manufacturer manufacturer) {
        return manufacturerDAO.update(manufacturer);
    }

    @Override
    public Manufacturer delete(long manufacturerId) {
        return manufacturerDAO.delete(manufacturerId);
    }
    
    @Override
    public List<Manufacturer> get(Set<Long> manufacturerIdSet) {
    	return manufacturerDAO.get(manufacturerIdSet);
    }
    
    @Override
    public PaginationResponse<Manufacturer> getForCustomer(int customerId, List<ColumnAndSort> sortBy,
    		PaginationContext<Manufacturer> context) {
    	return manufacturerDAO.getForCustomer( customerId, sortBy, context);
    }
}
