package com.telecominfraproject.wlan.profile.mesh.models;

import com.telecominfraproject.wlan.datastore.exceptions.DsDataValidationException;

/**
 * @author yongli
 *
 */
public class MultiApDeploymentInformation extends DeploymentInformation {
    /**
     * 
     */
    private static final long serialVersionUID = 4179890677614709042L;

    public MultiApDeploymentInformation(int customerId) {
        super(customerId, NetworkDeploymentType.MULTIPLE);
    }

    protected MultiApDeploymentInformation() {
        this(0);
    }

    @Override
    public void validateValue() {
        super.validateValue();
        for (DeploymentEquipmentInfo eq : getEquipmentMap().values()) {
            if (!ApMeshMode.STANDALONE.equals(eq.getRole())) {
                throw new DsDataValidationException("only standalone ap allowed");
            }
        }
    }
}
