package com.telecominfraproject.wlan.profile.mesh.models;

import com.telecominfraproject.wlan.datastore.exceptions.DsDataValidationException;

/**
 * @author yongli
 *
 */
public interface DeploymentValue {
    /**
     * @throws DsDataValidationException
     */
    public void validateValue();
}
