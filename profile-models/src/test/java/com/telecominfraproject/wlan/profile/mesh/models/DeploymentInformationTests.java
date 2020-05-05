package com.telecominfraproject.wlan.profile.mesh.models;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.telecominfraproject.wlan.core.model.json.BaseJsonModel;
import com.telecominfraproject.wlan.datastore.exceptions.DsDataValidationException;
import com.telecominfraproject.wlan.equipment.models.NetworkForwardMode;

public class DeploymentInformationTests {

    private static final Logger LOG = LoggerFactory.getLogger(DeploymentInformationTests.class);

    @Test
    public void testMeshNAS() {
        DeploymentInformation deployInfo = new MeshDeploymentInformation(1);
        populateCommonElement(deployInfo);
        deployInfo.addEquipmentInfo(new DeploymentEquipmentInfo(1L, null, "MeshPortal-QR1", ApMeshMode.MESH_PORTAL,
                NetworkForwardMode.NAT));
        deployInfo.addEquipmentInfo(new DeploymentEquipmentInfo(2L, null, "MeshPoint-QR2", ApMeshMode.MESH_POINT,
                NetworkForwardMode.BRIDGE));

        String jsonString = deployInfo.toPrettyString();
        LOG.debug("Deployment {}: {}", NetworkDeploymentType.MESH, jsonString);
        DeploymentInformation parsed = BaseJsonModel.fromString(jsonString, DeploymentInformation.class);
        assertNotNull(parsed);
        assertEquals(deployInfo, parsed);
        testValidateConfig(deployInfo);

        DeploymentEquipmentInfo equipmentInfo = deployInfo.findEquipmentInfo(1L);
        // test standalone in mesh
        equipmentInfo.setRole(ApMeshMode.STANDALONE);
        try {
            deployInfo.validateValue();
            fail("invalid role");
        } catch (DsDataValidationException exp) {
        }
        equipmentInfo.setRole(ApMeshMode.MESH_POINT);

        // test missing mesh portal
        try {
            deployInfo.validateValue();
            fail("invalid role");
        } catch (DsDataValidationException exp) {
        }
        equipmentInfo.setRole(ApMeshMode.MESH_PORTAL);

        // test NAT on Mesh Point
        equipmentInfo = deployInfo.findEquipmentInfo(2L);
        equipmentInfo.setForwardMode(NetworkForwardMode.NAT);
        try {
            deployInfo.validateValue();
            fail("invalid forward mode");
        } catch (DsDataValidationException exp) {
        }
        equipmentInfo.setForwardMode(NetworkForwardMode.BRIDGE);
    }

    @Test
    public void testMultipleBridge() {
        DeploymentInformation deployInfo = new MultiApDeploymentInformation(1);
        populateCommonElement(deployInfo);
        deployInfo.addEquipmentInfo(
                new DeploymentEquipmentInfo(1L, "QR1", "AP1", ApMeshMode.STANDALONE, NetworkForwardMode.BRIDGE));
        deployInfo.addEquipmentInfo(
                new DeploymentEquipmentInfo(2L, "QR2", "AP2", ApMeshMode.STANDALONE, NetworkForwardMode.BRIDGE));
        deployInfo.addEquipmentInfo(
                new DeploymentEquipmentInfo(3L, "QR3", "AP3", ApMeshMode.STANDALONE, NetworkForwardMode.BRIDGE));
        deployInfo.addEquipmentInfo(
                new DeploymentEquipmentInfo(4L, "QR4", "AP4", ApMeshMode.STANDALONE, NetworkForwardMode.BRIDGE));

        String jsonString = deployInfo.toPrettyString();
        LOG.debug("Deployment {}: {}", NetworkDeploymentType.MULTIPLE, jsonString);
        DeploymentInformation parsed = BaseJsonModel.fromString(jsonString, DeploymentInformation.class);
        assertNotNull(parsed);
        assertEquals(deployInfo, parsed);

        DeploymentEquipmentInfo equipmentInfo = deployInfo.findEquipmentInfo(1L);
        equipmentInfo.setRole(ApMeshMode.MESH_PORTAL);
        try {
            deployInfo.validateValue();
            fail("invalid role");
        } catch (DsDataValidationException exp) {
        }
        equipmentInfo.setRole(ApMeshMode.STANDALONE);

        testValidateConfig(deployInfo);
    }

    @Test
    public void testSingleNAT() {
        DeploymentInformation deployInfo = new SingleApDeploymentInformation(1);
        populateCommonElement(deployInfo);
        deployInfo.addEquipmentInfo(
                new DeploymentEquipmentInfo(1L, "QR1", "AP1", ApMeshMode.STANDALONE, NetworkForwardMode.NAT));

        String jsonString = deployInfo.toPrettyString();
        LOG.debug("Deployment {}: {}", NetworkDeploymentType.SINGLE, jsonString);
        DeploymentInformation parsed = BaseJsonModel.fromString(jsonString, DeploymentInformation.class);
        assertNotNull(parsed);
        assertEquals(deployInfo, parsed);
        testValidateConfig(deployInfo);

        DeploymentEquipmentInfo equipmentInfo = deployInfo.findEquipmentInfo(1L);
        equipmentInfo.setRole(ApMeshMode.MESH_PORTAL);
        try {
            deployInfo.validateValue();
            fail("invalid role");
        } catch (DsDataValidationException exp) {
        }
        equipmentInfo.setRole(ApMeshMode.STANDALONE);

        // add one more
        deployInfo.addEquipmentInfo(
                new DeploymentEquipmentInfo(2L, "QR2", "AP2", ApMeshMode.STANDALONE, NetworkForwardMode.NAT));
        try {
            deployInfo.validateValue();
            fail("multiple equipment");
        } catch (DsDataValidationException exp) {
        }
    }

    private void populateCommonElement(DeploymentInformation deployInfo) {
        deployInfo.addSsidInfo(new DeploymentSsidInfo(DeploymentSsidType.PSK, "Work", "BestSecret!"));
        deployInfo.addSsidInfo(new DeploymentSsidInfo(DeploymentSsidType.GUEST, "Guest", null));
    }

    private void testValidateConfig(DeploymentInformation deployInfo) {
        deployInfo.validateValue();

        Map<String, DeploymentSsidInfo> ssidMap = deployInfo.getSsidMap();
        deployInfo.setSsidMap(new HashMap<>());
        try {
            deployInfo.validateValue();
            fail("empty ssid");
        } catch (DsDataValidationException exp) {
        }

        Map<Long, DeploymentEquipmentInfo> equipmentMap = deployInfo.getEquipmentMap();
        deployInfo.setEquipmentMap(new HashMap<>());
        try {
            deployInfo.validateValue();
            fail("empty equipment");
        } catch (DsDataValidationException exp) {
        }
        deployInfo.setEquipmentMap(equipmentMap);
    }

}
