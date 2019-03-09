package com.javapex.zz.web.route;

import com.javapex.zz.web.annotation.ZzAction;
import com.javapex.zz.web.annotation.ZzRoute;
import com.javapex.zz.web.config.AppConfig;
import com.javapex.zz.web.context.ZzContext;
import com.javapex.zz.web.enums.StatusEnum;
import com.javapex.zz.web.exception.ZzException;
import com.javapex.zz.web.reflect.ClassScanner;
import io.netty.handler.codec.http.QueryStringDecoder;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class RouterScanner {

    private static Map<String, Method> routes = null;

    private volatile static RouterScanner routerScanner;

    private AppConfig appConfig = AppConfig.getInstance();

    /**
     * get single Instance
     *
     * @return
     */
    public static RouterScanner getInstance() {
        if (routerScanner == null) {
            synchronized (RouterScanner.class) {
                if (routerScanner == null) {
                    routerScanner = new RouterScanner();
                }
            }
        }
        return routerScanner;
    }

    private RouterScanner() {
    }

    /**
     * get route method
     *
     * @param queryStringDecoder
     * @return
     * @throws Exception
     */
    public Method routeMethod(QueryStringDecoder queryStringDecoder) throws Exception {
        if (routes == null) {
            routes = new HashMap<>(16);
            loadRouteMethods(appConfig.getRootPackageName());
        }

        //default response
        boolean defaultResponse = defaultResponse(queryStringDecoder.path());

        if (defaultResponse) {
            return null;
        }

        Method method = routes.get(queryStringDecoder.path());

        if (method == null) {
            throw new ZzException(StatusEnum.NOT_FOUND);
        }

        return method;


    }

    private boolean defaultResponse(String path) {
        if (appConfig.getRootPath().equals(path)) {
            ZzContext.getContext().html("<center> Hello Zz <br/><br/>" +
                    "Power by <a href='https://javapex.com'>@Zz</a> </center>");
            return true;
        }
        return false;
    }


    private void loadRouteMethods(String packageName) throws Exception {
        Set<Class<?>> classes = ClassScanner.getClasses(packageName);

        for (Class<?> aClass : classes) {
            Method[] declaredMethods = aClass.getMethods();

            for (Method method : declaredMethods) {
                ZzRoute annotation = method.getAnnotation(ZzRoute.class);
                if (annotation == null) {
                    continue;
                }

                ZzAction cicadaAction = aClass.getAnnotation(ZzAction.class);
                routes.put(appConfig.getRootPath() + "/" + cicadaAction.value() + "/" + annotation.value(), method);
            }
        }
    }
}
