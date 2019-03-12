package com.javapex.zz.web.configuration;

import java.util.Properties;

public abstract class AbstractZzConfiguration {

    /**
     * file name
     */
    private String propertiesName;

    private Properties properties;


    public void setPropertiesName(String propertiesName) {
        this.propertiesName = propertiesName;
    }

    public String getPropertiesName() {
        return propertiesName;
    }

    public void setProperties(Properties properties) {
        this.properties = properties;
    }

    public String get(String key) {
        return properties.get(key) == null ? null : properties.get(key).toString();
    }

    @Override
    public String toString() {
        return "AbstractZzConfiguration{" +
                "propertiesName='" + propertiesName + '\'' +
                ", properties=" + properties +
                '}';
    }
}
