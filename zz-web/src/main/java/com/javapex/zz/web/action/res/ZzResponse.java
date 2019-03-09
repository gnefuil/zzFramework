package com.javapex.zz.web.action.res;

import com.javapex.zz.web.action.req.Cookie;

import java.util.List;
import java.util.Map;

public interface ZzResponse {

    /**
     * get all customer headers
     * @return
     */
    Map<String, String> getHeaders();


    /**
     * set content type
     * @param contentType
     */
    void setContentType(String contentType);

    /**
     * get content type
     * @return
     */
    String getContentType();

    /**
     * set http body
     * @param content
     */
    void setHttpContent(String content);

    /**
     * get http body
     * @return
     */
    String getHttpContent();


    /**
     * set cookie
     * @param cookie cookie
     */
    void setCookie(Cookie cookie) ;


    /**
     * get all cookies
     * @return all cookies
     */
    List<io.netty.handler.codec.http.cookie.Cookie> cookies() ;

}
