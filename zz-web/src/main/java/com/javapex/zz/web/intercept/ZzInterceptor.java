package com.javapex.zz.web.intercept;

import com.javapex.zz.web.action.param.Param;
import com.javapex.zz.web.context.ZzContext;

public abstract class ZzInterceptor {



    private int order ;

    public int getOrder() {
        return order;
    }

    public void setOrder(int order) {
        this.order = order;
    }

    /**
     * before
     * @param context
     * @param param
     * @return
     * true if the execution chain should proceed with the next interceptor or the handler itself
     * @throws Exception
     */
    protected boolean before(ZzContext context, Param param) throws Exception{
        return true;
    }


    /**
     * after
     * @param context
     * @param param
     * @throws Exception
     */
    protected void after(ZzContext context,Param param) throws Exception{}

}
