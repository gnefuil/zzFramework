package com.javapex.zz.web.util;

import com.javapex.zz.web.config.AppConfig;

public class PathUtil {
    /**
     * Get Root Path
     * /zz-example/demoAction
     * @param path
     * @return zz-example
     */
    public static String getRootPath(String path) {
        return "/" + path.split("/")[1];
    }

    /**
     * Get Action Path
     * /zz-example/demoAction
     * @param path
     * @return demoAction
     */
    public static String getActionPath(String path) {
        return path.split("/")[2];
    }

    /**
     * Get Action Path
     * /zz-example/routeAction/getUser
     * @param path
     * @return getUser
     */
    public static String getRoutePath(String path) {
        AppConfig instance = AppConfig.getInstance();
        return path.replace(instance.getRootPackageName(),"") ;
    }
}
