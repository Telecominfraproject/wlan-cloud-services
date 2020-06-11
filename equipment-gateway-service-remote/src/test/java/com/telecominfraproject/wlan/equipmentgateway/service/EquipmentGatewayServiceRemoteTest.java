package com.telecominfraproject.wlan.equipmentgateway.service;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;

import com.telecominfraproject.wlan.core.model.service.GatewayType;
import com.telecominfraproject.wlan.equipmentgateway.models.CEGWBaseCommand;
import com.telecominfraproject.wlan.equipmentgateway.models.CEGWBlinkRequest;
import com.telecominfraproject.wlan.equipmentgateway.models.CEGWCommandResultCode;
import com.telecominfraproject.wlan.equipmentgateway.models.CEGWConfigChangeNotification;
import com.telecominfraproject.wlan.equipmentgateway.models.CEGWStartDebugEngine;
import com.telecominfraproject.wlan.equipmentgateway.models.EquipmentCommandResponse;
import com.telecominfraproject.wlan.remote.tests.BaseRemoteTest;
import com.telecominfraproject.wlan.routing.RoutingServiceInterface;
import com.telecominfraproject.wlan.routing.models.EquipmentGatewayRecord;
import com.telecominfraproject.wlan.routing.models.EquipmentRoutingRecord;

//NOTE: these profiles will be ADDED to the list of active profiles  
@ActiveProfiles(profiles = {
      "integration_test",
      "no_ssl","http_digest_auth","rest-template-single-user-per-service-digest-auth",
      }) 
public class EquipmentGatewayServiceRemoteTest extends BaseRemoteTest {

    @Autowired EquipmentGatewayServiceInterface testInterface;
    @Autowired RoutingServiceInterface routingInterface;

    @Before public void urlSetup(){
        configureBaseUrl("tip.wlan.routingServiceBaseUrl");
        addProperties("tip.wlan.equipmentGatewayProtocol", "http");
    }
    
    @Test
    public void testEquipmentGateway_SingleCommand() {
		long equipmentId = getNextEquipmentId();
		int customerId = getNextCustomerId();
    	String inventoryId = "ap-"+ equipmentId;    	
		 
		//create gateway record
		EquipmentGatewayRecord equipmentGwRecord = new EquipmentGatewayRecord(GatewayType.CEGW);
		equipmentGwRecord.setHostname("localhost");
		equipmentGwRecord.setIpAddr("127.0.0.1");
		equipmentGwRecord.setPort(server.getWebServer().getPort());
		
		equipmentGwRecord = routingInterface.registerGateway(equipmentGwRecord );
		
		//create equipment routing record
		EquipmentRoutingRecord eqRoutingRec = new EquipmentRoutingRecord();
		eqRoutingRec.setCustomerId(customerId);
		eqRoutingRec.setEquipmentId(equipmentId);
		eqRoutingRec.setGatewayId(equipmentGwRecord.getId());
		
		eqRoutingRec = routingInterface.create(eqRoutingRec);
		
		//now we can send a command that uses the routing info we set up earlier
    	EquipmentCommandResponse ret = testInterface.sendCommand(new CEGWBlinkRequest(inventoryId , equipmentId ));
    	assertEquals(CEGWCommandResultCode.Success, ret.getResultCode());
    	
    	//test sending command to equipment that cannot be found
    	ret = testInterface.sendCommand(new CEGWBlinkRequest(inventoryId+"-non-existent" , -1 ));
    	assertEquals(CEGWCommandResultCode.FailedToSend, ret.getResultCode());
    	
    }


    @Test
    public void testEquipmentGateway_BulkCommands() {
		
		int customerId = getNextCustomerId();
		 
		//create gateway record
		EquipmentGatewayRecord equipmentGwRecord = new EquipmentGatewayRecord(GatewayType.CEGW);
		equipmentGwRecord.setHostname("localhost");
		equipmentGwRecord.setIpAddr("127.0.0.1");
		equipmentGwRecord.setPort(server.getWebServer().getPort());
		
		equipmentGwRecord = routingInterface.registerGateway(equipmentGwRecord );

		List<Long> equipmentIds = new ArrayList<>();
    	long eqId;
		for(int i=0; i<10; i++) {
			eqId = getNextEquipmentId();
			equipmentIds.add(eqId);
    	
			//create equipment routing records
			EquipmentRoutingRecord eqRoutingRec = new EquipmentRoutingRecord();
			eqRoutingRec.setCustomerId(customerId);
			eqRoutingRec.setEquipmentId(eqId);
			eqRoutingRec.setGatewayId(equipmentGwRecord.getId());
			
			eqRoutingRec = routingInterface.create(eqRoutingRec);
		}
		
		//now we can send a batch of commands that uses the routing info we set up earlier
		//we'll prepare 3 commands per equipment Id
		List<CEGWBaseCommand> cmdBatch = new ArrayList<>();
		equipmentIds.forEach(equipmentId -> {
			cmdBatch.add(new CEGWBlinkRequest("ap-" + equipmentId, equipmentId));
			cmdBatch.add(new CEGWConfigChangeNotification("ap-" + equipmentId, equipmentId));
			cmdBatch.add(new CEGWStartDebugEngine("ap-" + equipmentId, equipmentId, "testDebug", 4242 ));
		});
		
    	List<EquipmentCommandResponse> ret = testInterface.sendCommands(cmdBatch);
    	assertEquals(cmdBatch.size(), ret.size());
    	ret.forEach(r -> assertEquals(CEGWCommandResultCode.Success, r.getResultCode()));

    }

    @Test
    public void testEquipmentGateway_BulkCommands_multipleGateways() {
		
		int customerId = getNextCustomerId();
		 
		// Create two gateway records.
		// In real life only one GW record is valid per host/port, the other ones are considered stale.
		// But in this test we are verifying that all the commands will be delivered to all the equipment across many gateways, 
		// so we are not concerned that several gateway records are created for the same host/port.
		EquipmentGatewayRecord equipmentGwRecord1 = new EquipmentGatewayRecord(GatewayType.CEGW);
		equipmentGwRecord1.setHostname("localhost");
		equipmentGwRecord1.setIpAddr("127.0.0.1");
		equipmentGwRecord1.setPort(server.getWebServer().getPort());
		
		equipmentGwRecord1 = routingInterface.registerGateway(equipmentGwRecord1 );

		EquipmentGatewayRecord equipmentGwRecord2 = new EquipmentGatewayRecord(GatewayType.CEGW);
		equipmentGwRecord2.setHostname("localhost");
		equipmentGwRecord2.setIpAddr("127.0.0.1");
		equipmentGwRecord2.setPort(server.getWebServer().getPort());
		
		equipmentGwRecord2 = routingInterface.registerGateway(equipmentGwRecord2 );

		List<Long> equipmentIds = new ArrayList<>();
    	long eqId;
		for(int i=0; i<10; i++) {
			eqId = getNextEquipmentId();
			equipmentIds.add(eqId);
    	
			//create equipment routing records even ones will use equipmentGwRecord1, odd ones will use equipmentGwRecord2
			EquipmentRoutingRecord eqRoutingRec = new EquipmentRoutingRecord();
			eqRoutingRec.setCustomerId(customerId);
			eqRoutingRec.setEquipmentId(eqId);
			eqRoutingRec.setGatewayId( (i%2==0) ? equipmentGwRecord1.getId() : equipmentGwRecord2.getId());
			
			eqRoutingRec = routingInterface.create(eqRoutingRec);
		}
		
		//now we can send a batch of commands that uses the routing info we set up earlier
		//we'll prepare 3 commands per equipment Id
		List<CEGWBaseCommand> cmdBatch = new ArrayList<>();
		equipmentIds.forEach(equipmentId -> {
			cmdBatch.add(new CEGWBlinkRequest("ap-" + equipmentId, equipmentId));
			cmdBatch.add(new CEGWConfigChangeNotification("ap-" + equipmentId, equipmentId));
			cmdBatch.add(new CEGWStartDebugEngine("ap-" + equipmentId, equipmentId, "testDebug", 4242 ));
		});
		
    	List<EquipmentCommandResponse> ret = testInterface.sendCommands(cmdBatch);
    	assertEquals(cmdBatch.size(), ret.size());
    	ret.forEach(r -> assertEquals(CEGWCommandResultCode.Success, r.getResultCode()));

    }

}
