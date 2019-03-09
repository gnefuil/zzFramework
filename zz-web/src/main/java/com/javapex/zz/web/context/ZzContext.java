package com.javapex.zz.web.context;

import com.alibaba.fastjson.JSON;
import com.javapex.zz.web.action.req.ZzRequest;
import com.javapex.zz.web.action.res.WorkRes;
import com.javapex.zz.web.action.res.ZzResponse;
import com.javapex.zz.web.constant.ZzConstant;
import com.javapex.zz.web.thread.ThreadLocalHolder;

public class ZzContext {


    /**
     * current thread request
     */
    private ZzRequest request ;

    /**
     * current thread response
     */
    private ZzResponse response ;

    public ZzContext(ZzRequest request, ZzResponse response) {
        this.request = request;
        this.response = response;
    }


    /**
     * response json message
     * @param workRes
     */
    public void json(WorkRes workRes){
        ZzContext.getResponse().setContentType(ZzConstant.ContentType.JSON);
        ZzContext.getResponse().setHttpContent(JSON.toJSONString(workRes));
    }

    /**
     * response text message
     * @param text response body
     */
    public void text(String text){
        ZzContext.getResponse().setContentType(ZzConstant.ContentType.TEXT);
        ZzContext.getResponse().setHttpContent(text);
    }

    /**
     * response html
     * @param html response body
     */
    public void html(String html){
        ZzContext.getResponse().setContentType(ZzConstant.ContentType.HTML);
        ZzContext.getResponse().setHttpContent(html);
    }

    public static ZzRequest getRequest(){
        return ZzContext.getContext().request ;
    }

    public ZzRequest request(){
        return ZzContext.getContext().request ;
    }

    public static ZzResponse getResponse(){
        return ZzContext.getContext().response ;
    }

    public static void setContext(ZzContext context){
        ThreadLocalHolder.setZzContext(context) ;
    }


    public static void removeContext(){
        ThreadLocalHolder.removeZzContext();
    }

    public static ZzContext getContext(){
        return ThreadLocalHolder.getZzContext() ;
    }
}
