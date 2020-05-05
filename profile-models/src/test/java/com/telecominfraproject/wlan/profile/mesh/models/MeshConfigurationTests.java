package com.telecominfraproject.wlan.profile.mesh.models;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.telecominfraproject.wlan.core.model.json.BaseJsonModel;

public class MeshConfigurationTests {

    private static final Logger LOG = LoggerFactory.getLogger(MeshConfigurationTests.class);

    @Test
    public void testMeshPointConfiguration() {
        ApMeshPointConfiguration testConfig = new ApMeshPointConfiguration();
        String jsonDocument = testConfig.toPrettyString();
        LOG.debug("Testing {}", testConfig);
        ApMeshConfiguration newConfig = BaseJsonModel.fromString(jsonDocument, ApMeshConfiguration.class);

        assertNotNull(newConfig);
        assertEquals(testConfig, newConfig);
    }

    @Test
    public void testMeshPortalConfiguration() {
        List<MeshPointInformation> meshPoints = new ArrayList<>();
        for (long eqId = 1; eqId < 4; ++eqId) {
            meshPoints.add(new MeshPointInformation(eqId + 100, null));
        }
        ApMeshPortalConfiguration testConfig = new ApMeshPortalConfiguration(0L, meshPoints, true);
        testConfig.setEthernetProtection(true);
        String jsonDocument = testConfig.toPrettyString();
        LOG.debug("Testing {}", testConfig);
        ApMeshConfiguration newConfig = BaseJsonModel.fromString(jsonDocument, ApMeshConfiguration.class);

        assertNotNull(newConfig);
        assertEquals(testConfig, newConfig);
    }

}
