package com.javapex.zz.web.log;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LoggerBuilder {
    /**
     * get static Logger
     * @param clazz
     * @return
     */
    public static Logger getLogger(Class clazz){
        return LoggerFactory.getLogger(clazz);
    }
}
