package com.telecominfraproject.wlan.profile.mesh.models;

import com.telecominfraproject.wlan.datastore.exceptions.DsDataValidationException;
import com.telecominfraproject.wlan.equipment.models.NetworkForwardMode;

/**
 * @author yongli
 *
 */
public class MeshDeploymentInformation extends DeploymentInformation {
    private static final long serialVersionUID = -2230016824032757029L;
    private static final int MAX_MESH_POINT_COUNT = 4;

    public MeshDeploymentInformation(int customerId) {
        super(customerId, NetworkDeploymentType.MESH);
    }

    protected MeshDeploymentInformation() {
        this(0);
    }

    @Override
    public void validateValue() {
        super.validateValue();
        if (Boolean.TRUE.equals(getEquipmentDiscovery())) {
            throw new DsDataValidationException("equipment discovery is not supported for this type of deployment");
        }
        
        int portalFound = 0;
        int pointFound = 0;

        for (DeploymentEquipmentInfo eq : getEquipmentMap().values()) {
            switch (eq.getRole()) {
            case MESH_POINT:
                ++pointFound;
                if (!NetworkForwardMode.BRIDGE.equals(eq.getForwardMode())) {
                    throw new DsDataValidationException("NAT is not supported for mesh point");
                }
                break;
            case MESH_PORTAL:
                ++portalFound;
                break;
            default:
                throw new DsDataValidationException(
                        "supported equipment role " + eq.getRole() + " for " + eq.getEquipmentId());
            }
        }
        if (1 != portalFound) {
            throw new DsDataValidationException("invalid number of mesh portal (" + portalFound + ") equipment");
        }
        if ((0 > pointFound) || (pointFound > MAX_MESH_POINT_COUNT)) {
            throw new DsDataValidationException("invalid number of mesh point (" + pointFound + ") equipment");
        }
    }
}
