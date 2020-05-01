package com.telecominfraproject.wlan.equipment.models;

import java.util.Set;

import org.junit.Test;
import org.reflections.Reflections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.telecominfraproject.wlan.core.model.json.BaseJsonModel;

public class DeserializeTests {

    private static final Logger LOG = LoggerFactory.getLogger(DeserializeTests.class);

    @Test
    public void testUnknownEnum() {
        Reflections reflections = new Reflections(DeserializeTests.class.getPackage().getName());

        // SubTypesScanner
        Set<Class<? extends BaseJsonModel>> modelClasses = reflections.getSubTypesOf(BaseJsonModel.class);

        for (Class<? extends BaseJsonModel> c : modelClasses) {
            BaseJsonModel.checkUnknownEnumForClass(c, true, LOG);
        }
    }
}
