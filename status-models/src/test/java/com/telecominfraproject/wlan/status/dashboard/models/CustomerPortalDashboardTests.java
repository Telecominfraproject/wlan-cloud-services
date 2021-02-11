package com.telecominfraproject.wlan.status.dashboard.models;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.telecominfraproject.wlan.core.model.equipment.RadioType;
import com.telecominfraproject.wlan.core.model.json.BaseJsonModel;
import com.telecominfraproject.wlan.status.dashboard.models.events.CustomerPortalDashboardPartialEvent;
import com.telecominfraproject.wlan.status.models.StatusCode;

public class CustomerPortalDashboardTests {

    private static final Logger LOG = LoggerFactory.getLogger(CustomerPortalDashboardTests.class);

    @Test
    public void testCustomerPortalDashboard() {
		CustomerPortalDashboardPartialEvent evtPartial = new CustomerPortalDashboardPartialEvent();
		evtPartial.incrementAssociatedClientsCountPerRadio(RadioType.is2dot4GHz, 10);
		evtPartial.incrementAssociatedClientsCountPerRadio(RadioType.is5GHzL, 5);
		evtPartial.incrementAssociatedClientsCountPerRadio(RadioType.is5GHzU, 3);
		evtPartial.incrementClientCountPerOui("000000", 8);
		evtPartial.incrementClientCountPerOui("0F0F0F", 22);
		evtPartial.incrementAlarmsCountBySeverity(StatusCode.requiresAttention, 10);
		
		CustomerPortalDashboardStatus status = new CustomerPortalDashboardStatus();
		status.applyPartialEvent(evtPartial);
		status.incrementEquipmentCountPerOui("2F2F2F", 14);
		status.incrementAlarmsCountBySeverity(StatusCode.requiresAttention, 10);
		
		String statusStr = status.toString();
		
		CustomerPortalDashboardStatus statusDeserialized = (CustomerPortalDashboardStatus) BaseJsonModel.fromString(statusStr, BaseJsonModel.class);
		String statusDeserializedStr = statusDeserialized.toString();
		
		assertEquals(statusStr, statusDeserializedStr);
    }
    
    @Test
	public void testCustomerPortalDashboardPartial() {
		CustomerPortalDashboardPartialEvent evt = new CustomerPortalDashboardPartialEvent();
		evt.incrementAssociatedClientsCountPerRadio(RadioType.is2dot4GHz, 10);
		evt.incrementAssociatedClientsCountPerRadio(RadioType.is5GHzL, 5);
		evt.incrementAssociatedClientsCountPerRadio(RadioType.is5GHzU, 3);
		evt.incrementClientCountPerOui("000000", 8);
		evt.incrementClientCountPerOui("0F0F0F", 22);
		evt.incrementAlarmsCountBySeverity(StatusCode.requiresAttention, 10);
		
		String evtStr = evt.toString();
		
		CustomerPortalDashboardPartialEvent evtDeserialized = (CustomerPortalDashboardPartialEvent) BaseJsonModel.fromString(evtStr, BaseJsonModel.class);
		String evtDeserializedStr = evtDeserialized.toString();
		
		assertEquals(evtStr, evtDeserializedStr);

	}

}
