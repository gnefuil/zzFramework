package com.javapex.zz.web.configuration;

import java.util.HashMap;
import java.util.Map;

public class ConfigurationHolder {

    private static Map<String,AbstractZzConfiguration> config = new HashMap<>(8) ;

    /**
     * Add holder cache
     * @param key
     * @param configuration
     */
    public static void addConfiguration(String key,AbstractZzConfiguration configuration){
        config.put(key, configuration);
    }


    /**
     * Get class from cache by class name
     * @param clazz
     * @return
     */
    public static AbstractZzConfiguration getConfiguration(Class<? extends AbstractZzConfiguration> clazz){
        return config.get(clazz.getName()) ;
    }
}
