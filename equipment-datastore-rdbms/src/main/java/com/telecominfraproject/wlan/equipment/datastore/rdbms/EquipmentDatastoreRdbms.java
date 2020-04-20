package com.telecominfraproject.wlan.equipment.datastore.rdbms;

import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

import com.telecominfraproject.wlan.core.model.pagination.ColumnAndSort;
import com.telecominfraproject.wlan.core.model.pagination.PaginationContext;
import com.telecominfraproject.wlan.core.model.pagination.PaginationResponse;

import com.telecominfraproject.wlan.equipment.datastore.EquipmentDatastore;
import com.telecominfraproject.wlan.equipment.models.Equipment;

/**
 * @author dtoptygin
 *
 */
@Configuration
public class EquipmentDatastoreRdbms implements EquipmentDatastore {

    @Autowired EquipmentDAO equipmentDAO;

    @Override
    public Equipment create(Equipment equipment) {
        return equipmentDAO.create(equipment);
    }

    @Override
    public Equipment get(long equipmentId) {
        return equipmentDAO.get(equipmentId);
    }

    @Override
    public Equipment getOrNull(long equipmentId) {
        return equipmentDAO.getOrNull(equipmentId);
    }
    
    @Override
    public Equipment update(Equipment equipment) {
        return equipmentDAO.update(equipment);
    }

    @Override
    public Equipment delete(long equipmentId) {
        return equipmentDAO.delete(equipmentId);
    }
    
    @Override
    public List<Equipment> get(Set<Long> equipmentIdSet) {
    	return equipmentDAO.get(equipmentIdSet);
    }
    
    @Override
    public PaginationResponse<Equipment> getForCustomer(int customerId, List<ColumnAndSort> sortBy,
    		PaginationContext<Equipment> context) {
    	return equipmentDAO.getForCustomer( customerId, sortBy, context);
    }
}
