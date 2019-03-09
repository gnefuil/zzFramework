package com.javapex.zz.web.action.res;

import com.javapex.zz.web.constant.ZzConstant;
import com.javapex.zz.web.exception.ZzException;
import com.javapex.zz.web.action.req.Cookie ;
import io.netty.handler.codec.http.cookie.DefaultCookie;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ZzHttpResponse implements ZzResponse{
    private Map<String, String> headers = new HashMap<>(8);

    private String contentType;

    private String httpContent;

    private List<io.netty.handler.codec.http.cookie.Cookie> cookies = new ArrayList<>(6);

    private ZzHttpResponse() {
    }

    public static ZzHttpResponse init() {
        ZzHttpResponse response = new ZzHttpResponse();
        response.contentType = ZzConstant.ContentType.TEXT;
        return response;
    }

    @Override
    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    @Override
    public String getContentType() {
        return contentType;
    }

    @Override
    public void setHttpContent(String content) {
        httpContent = content;
    }

    @Override
    public String getHttpContent() {
        return this.httpContent == null ? "" : this.httpContent;
    }

    public void setHeaders(String key, String value) {
        this.headers.put(key, value);
    }

    @Override
    public Map<String, String> getHeaders() {
        return this.headers;
    }


    @Override
    public void setCookie(Cookie zzCookie) {
        if (null == zzCookie){
            throw new ZzException("cookie is null!") ;
        }

        if (null == zzCookie.getName()){
            throw new ZzException("cookie.getName() is null!") ;
        }
        if (null == zzCookie.getValue()){
            throw new ZzException("cookie.getValue() is null!") ;
        }

        DefaultCookie cookie = new DefaultCookie(zzCookie.getName(), zzCookie.getValue());

        cookie.setPath("/");
        cookie.setMaxAge(zzCookie.getMaxAge());
        cookies.add(cookie) ;
    }

    @Override
    public List<io.netty.handler.codec.http.cookie.Cookie> cookies() {
        return cookies;
    }
}
