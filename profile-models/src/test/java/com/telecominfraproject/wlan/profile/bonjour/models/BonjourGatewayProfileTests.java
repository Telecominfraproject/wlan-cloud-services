package com.telecominfraproject.wlan.profile.bonjour.models;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.telecominfraproject.wlan.core.model.json.BaseJsonModel;
import com.telecominfraproject.wlan.datastore.exceptions.DsDataValidationException;

public class BonjourGatewayProfileTests {

    private static final Logger LOG = LoggerFactory.getLogger(BonjourGatewayProfileTests.class);

    @Test
    public void testBonjourGatewayProfile() {
        BonjourGatewayProfile src = new BonjourGatewayProfile();

        try {
            src.validateValue(Collections.emptySet());
            fail("did not detect empty profile");
        } catch (DsDataValidationException exp) {
            LOG.debug("successfully caught DsDataValidationException: {}", exp.getLocalizedMessage());
        }

        BonjourServiceSet serviceSet = new BonjourServiceSet();
        try {
            src.addBonjourServiceSet(serviceSet);
            fail("added emptry BonjourServiceSet");
        } catch (DsDataValidationException exp) {
            LOG.debug("successfully caught DsDataValidationException: {}", exp.getLocalizedMessage());
        }

        try {
            serviceSet.setVlanId((short) -1);
            src.addBonjourServiceSet(serviceSet);
            fail("added BonjourServiceSet with invalid VLAN id");
        } catch (DsDataValidationException exp) {
            LOG.debug("successfully caught DsDataValidationException: {}", exp.getLocalizedMessage());
        }

        try {
            serviceSet.setVlanId((short) 2);
            src.addBonjourServiceSet(serviceSet);
            fail("added BonjourServiceSet with empty service name");
        } catch (DsDataValidationException exp) {
            LOG.debug("successfully caught DsDataValidationException: {}", exp.getLocalizedMessage());
        }

        serviceSet.setServiceNames(new HashSet<>(Arrays.asList(new String[] { "AirPlay" })));
        src.addBonjourServiceSet(serviceSet);

        serviceSet = serviceSet.clone();
        serviceSet.setServiceNames(new HashSet<>(Arrays.asList(new String[] { "Airport" })));
        src.addBonjourServiceSet(serviceSet);

        // test the merge result.
        serviceSet = src.findBonjourServiceSet(serviceSet.getVlanId());
        assertEquals(2, serviceSet.getServiceNames().size());

        try {
            src.validateValue(Collections.emptySet());
            fail("profile contains uspported service name");
        } catch (DsDataValidationException exp) {
            LOG.debug("successfully caught DsDataValidationException: {}", exp.getLocalizedMessage());
        }

        // add bonjour service on default vlan
        serviceSet = new BonjourServiceSet();
        serviceSet.setSupportAllServices(true);
        src.addBonjourServiceSet(serviceSet);

        String jsonDoc = src.toString();

        LOG.debug("Encoded BonjourGatewayProfile {}", src);
        BonjourGatewayProfile decoded = BaseJsonModel.fromString(jsonDoc, BonjourGatewayProfile.class);
        LOG.debug("Decoded BonjourGatewayProfile {}", decoded);

        assertEquals(src, decoded);
    }


}
