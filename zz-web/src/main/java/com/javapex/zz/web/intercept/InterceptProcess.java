package com.javapex.zz.web.intercept;

import com.javapex.zz.web.action.param.Param;
import com.javapex.zz.web.config.AppConfig;
import com.javapex.zz.web.context.ZzContext;
import com.javapex.zz.web.reflect.ClassScanner;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public class InterceptProcess {


    private InterceptProcess(){}

    private volatile static InterceptProcess process ;

    private static List<ZzInterceptor> interceptors ;

    private AppConfig appConfig = AppConfig.getInstance();

    /**
     * get single Instance
     * @return
     */
    public static InterceptProcess getInstance(){
        if (process == null){
            synchronized (InterceptProcess.class){
                if (process == null){
                    process = new InterceptProcess() ;
                }
            }
        }
        return process ;
    }


    public void loadInterceptors() throws Exception {

        if (interceptors != null){
            return;
        }else {
            interceptors = new ArrayList<>(10) ;
            Map<Class<?>, Integer> cicadaInterceptor = ClassScanner.getZzInterceptor(appConfig.getRootPackageName());
            for (Map.Entry<Class<?>, Integer> classEntry : cicadaInterceptor.entrySet()) {
                Class<?> interceptorClass = classEntry.getKey();
                ZzInterceptor interceptor = (ZzInterceptor) interceptorClass.newInstance();
                interceptor.setOrder(classEntry.getValue());
                interceptors.add(interceptor);
            }
            Collections.sort(interceptors,new OrderComparator());
        }
    }


    /**
     * execute before
     * @param param
     * @throws Exception
     */
    public boolean processBefore(Param param) throws Exception {
        for (ZzInterceptor interceptor : interceptors) {
            boolean access = interceptor.before(ZzContext.getContext(), param);
            if (!access){
                return access ;
            }
        }
        return true;
    }

    /**
     * execute after
     * @param param
     * @throws Exception
     */
    public void processAfter(Param param) throws Exception{
        for (ZzInterceptor interceptor : interceptors) {
            interceptor.after(ZzContext.getContext(),param) ;
        }
    }
    
}
