package com.javapex.zz.web.bean;

import com.javapex.zz.web.reflect.ClassScanner;
import com.javapex.zz.web.route.RouteProcess;

import java.util.Map;

public class ZzBeanManager {
    
    private ZzBeanManager(){
    }
    private static volatile ZzBeanManager zzBeanManager;

    private static ZzBeanFactory zzBeanFactory ;

    public static ZzBeanManager getInstance() {
        if (zzBeanManager == null) {
            synchronized (RouteProcess.class) {//todo synchronized
                if (zzBeanManager == null) {
                    zzBeanManager = new ZzBeanManager();
                }
            }
        }
        return zzBeanManager;
    }

    /**
     * init route bean factory
     * @param packageName
     * @throws Exception
     */
    public void init(String packageName) throws Exception {
        Map<String, Class<?>> zzAction = ClassScanner.getZzAction(packageName);

        Class<?> bean = ClassScanner.getCustomRouteBean();
        zzBeanFactory = (ZzBeanFactory) bean.newInstance() ;

        for (Map.Entry<String, Class<?>> classEntry : zzAction.entrySet()) {
            Object instance = classEntry.getValue().newInstance();
            zzBeanFactory.register(instance) ;
        }

    }


    /**
     * get route bean
     * @param name
     * @return
     * @throws Exception
     */
    public Object getBean(String name) throws Exception {
        return zzBeanFactory.getBean(name) ;
    }


    /**
     * release all beans
     */
    public void releaseBean(){
        zzBeanFactory.releaseBean();
    }
}
