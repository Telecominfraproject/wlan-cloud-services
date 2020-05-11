package com.telecominfraproject.wlan.equipment;

import java.util.List;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.telecominfraproject.wlan.core.model.equipment.EquipmentType;
import com.telecominfraproject.wlan.core.model.pagination.ColumnAndSort;
import com.telecominfraproject.wlan.core.model.pagination.PaginationContext;
import com.telecominfraproject.wlan.core.model.pagination.PaginationResponse;

import com.telecominfraproject.wlan.equipment.controller.EquipmentController;
import com.telecominfraproject.wlan.equipment.models.Equipment;
import com.telecominfraproject.wlan.equipment.models.EquipmentDetails;

/**
 * @author dtoptygin
 *
 */
@Component
public class EquipmentServiceLocal implements EquipmentServiceInterface {

    @Autowired private EquipmentController equipmentController;
    private static final Logger LOG = LoggerFactory.getLogger(EquipmentServiceLocal.class);

    
    @Override
    public Equipment create(Equipment equipment) {
        LOG.debug("calling equipmentController.create {} ", equipment);
        return equipmentController.create(equipment);
    }

    @Override
    public Equipment get(long equipmentId) {
        LOG.debug("calling equipmentController.get {} ", equipmentId);
        return equipmentController.get(equipmentId);
    }
    
    @Override
    public Equipment getOrNull(long equipmentId) {
        LOG.debug("calling equipmentController.getOrNull {} ", equipmentId);
        return equipmentController.getOrNull(equipmentId);
    }
    
    @Override
    public Equipment getByInventoryIdOrNull(String inventoryId) {
        LOG.debug("calling equipmentController.getByInventoryIdOrNull {} ", inventoryId);
        return equipmentController.getByInventoryIdOrNull(inventoryId);
    }
    
    @Override
    public List<Equipment> get(Set<Long> equipmentIdSet) {
        LOG.debug("calling equipmentController.getAllInSet {} ", equipmentIdSet);
        return equipmentController.getAllInSet(equipmentIdSet);
    }
    
    @Override
    public PaginationResponse<Equipment> getForCustomer(int customerId, List<ColumnAndSort> sortBy,
    		PaginationContext<Equipment> context) {
        LOG.debug("calling equipmentController.getForCustomer {} ", customerId);
        return equipmentController.getForCustomer(customerId, sortBy, context);
    }

    @Override
    public PaginationResponse<Equipment> getForCustomer(int customerId, EquipmentType equipmentType,
    		Set<Long> locationIds, List<ColumnAndSort> sortBy, PaginationContext<Equipment> context) {
        LOG.debug("calling equipmentController.getForCustomer {} {} {} ", customerId, equipmentType, locationIds);
        return equipmentController.getForCustomerWithFilter(customerId, equipmentType, locationIds, sortBy, context);
    }
    
    @Override
    public Equipment update(Equipment equipment) {
        LOG.debug("calling equipmentController.update {} ", equipment);
        return equipmentController.update(equipment);
    }

    @Override
    public Equipment delete(long equipmentId) {
        LOG.debug("calling equipmentController.delete {} ", equipmentId);
        return equipmentController.delete(equipmentId);
    }

    @Override
    public EquipmentDetails getDefaultEquipmentDetails(EquipmentType equipmentType) {
    	return equipmentController.getDefaultEquipmentDetails(equipmentType);
    }
}
