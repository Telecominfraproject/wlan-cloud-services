package com.telecominfraproject.wlan.profile.mesh.models;

import com.telecominfraproject.wlan.datastore.exceptions.DsDataValidationException;

/**
 * @author yongli
 *
 */
public class SingleApDeploymentInformation extends DeploymentInformation {
    /**
     * 
     */
    private static final long serialVersionUID = 4960402792317949515L;

    public SingleApDeploymentInformation(int customerId) {
        super(customerId, NetworkDeploymentType.SINGLE);
    }

    protected SingleApDeploymentInformation() {
        this(0);
    }

    @Override
    public void validateValue() {
        super.validateValue();
        if (Boolean.TRUE.equals(getEquipmentDiscovery())) {
            throw new DsDataValidationException("equipment discovery is not supported for this type of deployment");
        }
        if (getEquipmentMap().size() != 1) {
            throw new DsDataValidationException("more than one equipment specified");
        }
        for (DeploymentEquipmentInfo eq : getEquipmentMap().values()) {
            if (!ApMeshMode.STANDALONE.equals(eq.getRole())) {
                throw new DsDataValidationException("only standalone ap allowed");
            }
        }
    }
}
