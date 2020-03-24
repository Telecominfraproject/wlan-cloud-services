/**
 * 
 */
package com.telecominfraproject.wlan.servicemetrics.models;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.telecominfraproject.wlan.core.model.json.BaseJsonModel;

/**
 * @author yongli
 *
 */
@JsonInclude(JsonInclude.Include.NON_NULL) 
public abstract class MetricData extends BaseJsonModel {
    private static final long serialVersionUID = 1360838336969645855L;

    public abstract String getType();
}
