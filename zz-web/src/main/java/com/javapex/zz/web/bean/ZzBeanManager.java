package com.javapex.zz.web.bean;

import com.javapex.zz.web.reflect.ClassScanner;
import com.javapex.zz.web.route.RouteProcess;

import java.util.Map;

public class ZzBeanManager {
    
    private ZzBeanManager(){
    }
    private static volatile ZzBeanManager cicadaBeanManager;

    private static ZzBeanFactory cicadaBeanFactory ;

    public static ZzBeanManager getInstance() {
        if (cicadaBeanManager == null) {
            synchronized (RouteProcess.class) {//todo synchronized
                if (cicadaBeanManager == null) {
                    cicadaBeanManager = new ZzBeanManager();
                }
            }
        }
        return cicadaBeanManager;
    }

    /**
     * init route bean factory
     * @param packageName
     * @throws Exception
     */
    public void init(String packageName) throws Exception {
        Map<String, Class<?>> cicadaAction = ClassScanner.getZzAction(packageName);

        Class<?> bean = ClassScanner.getCustomRouteBean();
        cicadaBeanFactory = (ZzBeanFactory) bean.newInstance() ;

        for (Map.Entry<String, Class<?>> classEntry : cicadaAction.entrySet()) {
            Object instance = classEntry.getValue().newInstance();
            cicadaBeanFactory.register(instance) ;
        }

    }


    /**
     * get route bean
     * @param name
     * @return
     * @throws Exception
     */
    public Object getBean(String name) throws Exception {
        return cicadaBeanFactory.getBean(name) ;
    }


    /**
     * release all beans
     */
    public void releaseBean(){
        cicadaBeanFactory.releaseBean();
    }
}
