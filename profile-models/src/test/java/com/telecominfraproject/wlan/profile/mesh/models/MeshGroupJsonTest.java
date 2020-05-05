package com.telecominfraproject.wlan.profile.mesh.models;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.telecominfraproject.wlan.core.model.json.BaseJsonModel;

public class MeshGroupJsonTest {

    private static final Logger LOG = LoggerFactory.getLogger(MeshGroupJsonTest.class);

    @Test
    public void testGroup() {
        MeshGroup group = new MeshGroup();
        MeshGroupProperty property = new MeshGroupProperty();
        property.setName("SouthKey");
        property.setLocationId(100);
        property.setEthernetProtection(true);
        group.setProperty(property);

        List<MeshGroupMember> members = new ArrayList<>();

        members.add(new MeshGroupMember(ApMeshMode.MESH_PORTAL, 1L));
        members.add(new MeshGroupMember(ApMeshMode.MESH_PORTAL, 2L));
        members.add(new MeshGroupMember(ApMeshMode.MESH_POINT, 3L));
        group.setMembers(members);
        String jsonDocument = group.toPrettyString();

        LOG.debug("Encoded {}: {}", group.getClass().getSimpleName(), jsonDocument);
        MeshGroup decoded = BaseJsonModel.fromString(jsonDocument, MeshGroup.class);
        assertEquals(group, decoded);
    }

    @Test
    public void testMembership() {
        MeshGroupMembership membership = new MeshGroupMembership(ApMeshMode.MESH_POINT, 3L, 1L);
        membership.setCreatedTimestamp(System.currentTimeMillis());
        membership.setLastModifiedTimestamp(System.currentTimeMillis());
        String jsonDocument = membership.toPrettyString();
        LOG.debug("Encoded {}: {}", membership.getClass().getSimpleName(), jsonDocument);
        MeshGroupMembership decoded = BaseJsonModel.fromString(jsonDocument, MeshGroupMembership.class);
        assertEquals(membership, decoded);
    }

}
