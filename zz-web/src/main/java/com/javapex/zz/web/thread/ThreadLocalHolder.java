package com.javapex.zz.web.thread;

import com.javapex.zz.web.context.ZzContext;
import io.netty.util.concurrent.FastThreadLocal;

public class ThreadLocalHolder {

    private static final FastThreadLocal<Long> LOCAL_TIME= new FastThreadLocal() ;

    private static final FastThreadLocal<ZzContext> ZZ_CONTEXT= new FastThreadLocal() ;


    /**
     * set Zz context
     * @param context current context
     */
    public static void setZzContext(ZzContext context){
        ZZ_CONTEXT.set(context) ;
    }

    /**
     * remove Zz context
     */
    public static void removeZzContext(){
        ZZ_CONTEXT.remove();
    }

    /**
     * @return get Zz context
     */
    public static ZzContext getZzContext(){
        return ZZ_CONTEXT.get() ;
    }

    /**
     * Set time
     * @param time current time
     */
    public static void setLocalTime(long time){
        LOCAL_TIME.set(time) ;
    }

    /**
     * Get time and remove value
     * @return get local time
     */
    public static Long getLocalTime(){
        Long time = LOCAL_TIME.get();
        LOCAL_TIME.remove();
        return time;
    }

}
