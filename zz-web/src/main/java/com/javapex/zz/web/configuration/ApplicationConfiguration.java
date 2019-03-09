package com.javapex.zz.web.configuration;

import com.javapex.zz.web.constant.ZzConstant;

public class ApplicationConfiguration extends AbstractZzConfiguration {

    public ApplicationConfiguration() {
        super.setPropertiesName(ZzConstant.SystemProperties.APPLICATION_PROPERTIES);
    }

}
