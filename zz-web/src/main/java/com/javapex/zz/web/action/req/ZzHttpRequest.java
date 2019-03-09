package com.javapex.zz.web.action.req;

import com.javapex.zz.web.constant.ZzConstant;
import io.netty.handler.codec.http.DefaultHttpRequest;
import io.netty.handler.codec.http.cookie.ServerCookieDecoder;

import java.util.HashMap;
import java.util.Map;

public class ZzHttpRequest implements ZzRequest{


    private String method ;

    private String url ;

    private String clientAddress ;

    private Map<String,Cookie> cookie = new HashMap<>(8) ;
    private Map<String,String> headers = new HashMap<>(8) ;

    private ZzHttpRequest(){}

    public static ZzHttpRequest init(DefaultHttpRequest httpRequest){
        ZzHttpRequest request = new ZzHttpRequest() ;
        request.method = httpRequest.method().name();
        request.url = httpRequest.uri();

        //build headers
        buildHeaders(httpRequest, request);

        //init cookies
        initCookies(request);

        return request ;
    }

    /**
     * build headers
     * @param httpRequest io.netty.httprequest
     * @param request zz request
     */
    private static void buildHeaders(DefaultHttpRequest httpRequest, ZzHttpRequest request) {
        for (Map.Entry<String, String> entry : httpRequest.headers().entries()) {
            request.headers.put(entry.getKey(),entry.getValue());
        }
    }

    /**
     * init cookies
     * @param request request
     */
    private static void initCookies(ZzHttpRequest request) {
        for (Map.Entry<String, String> entry : request.headers.entrySet()) {
            String key = entry.getKey();
            String value = entry.getValue();
            if (!key.equals(ZzConstant.ContentType.COOKIE)){
                continue;
            }

            for (io.netty.handler.codec.http.cookie.Cookie cookie : ServerCookieDecoder.LAX.decode(value)) {
                Cookie zzCookie = new Cookie() ;
                zzCookie.setName(cookie.name());
                zzCookie.setValue(cookie.value());
                zzCookie.setDomain(cookie.domain());
                zzCookie.setMaxAge(cookie.maxAge());
                zzCookie.setPath(cookie.path()) ;
                request.cookie.put(zzCookie.getName(),zzCookie) ;
            }
        }
    }

    @Override
    public String getMethod() {
        return this.method;
    }

    @Override
    public String getUrl() {
        return this.url;
    }

    @Override
    public Cookie getCookie(String key) {
        return cookie.get(key) ;
    }
}
