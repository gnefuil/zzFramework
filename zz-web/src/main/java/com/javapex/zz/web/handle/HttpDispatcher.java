package com.javapex.zz.web.handle;

import com.alibaba.fastjson.JSON;
import com.javapex.zz.web.action.param.Param;
import com.javapex.zz.web.action.param.ParamMap;
import com.javapex.zz.web.action.req.ZzHttpRequest;
import com.javapex.zz.web.action.req.ZzRequest;
import com.javapex.zz.web.action.res.WorkRes;
import com.javapex.zz.web.action.res.ZzHttpResponse;
import com.javapex.zz.web.action.res.ZzResponse;
import com.javapex.zz.web.config.AppConfig;
import com.javapex.zz.web.constant.ZzConstant;
import com.javapex.zz.web.context.ZzContext;
import com.javapex.zz.web.exception.ZzException;
import com.javapex.zz.web.intercept.InterceptProcess;
import com.javapex.zz.web.log.LoggerBuilder;
import com.javapex.zz.web.route.RouteProcess;
import com.javapex.zz.web.route.RouterScanner;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.*;
import io.netty.handler.codec.http.cookie.Cookie;
import io.netty.util.CharsetUtil;
import org.slf4j.Logger;

import java.lang.reflect.Method;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;

@ChannelHandler.Sharable
public final class HttpDispatcher extends SimpleChannelInboundHandler<DefaultHttpRequest> {

    private static final Logger LOGGER = LoggerBuilder.getLogger(HttpDispatcher.class);

    private final AppConfig appConfig = AppConfig.getInstance();
    private final InterceptProcess interceptProcess = InterceptProcess.getInstance();
    private final RouterScanner routerScanner = RouterScanner.getInstance();
    private final RouteProcess routeProcess = RouteProcess.getInstance() ;

    @Override
    public void channelRead0(ChannelHandlerContext ctx, DefaultHttpRequest httpRequest) {

        ZzRequest zzRequest = ZzHttpRequest.init(httpRequest);
        ZzResponse zzResponse = ZzHttpResponse.init();

        // set current thread request and response
        ZzContext.setContext(new ZzContext(zzRequest, zzResponse));

        try {
            // request uri
            String uri = zzRequest.getUrl();
            QueryStringDecoder queryStringDecoder = new QueryStringDecoder(URLDecoder.decode(httpRequest.uri(), "utf-8"));

            // check Root Path
            appConfig.checkRootPath(uri, queryStringDecoder);

            // route Action
            //Class<?> actionClazz = routeAction(queryStringDecoder, appConfig);

            //build paramMap
            Param paramMap = buildParamMap(queryStringDecoder);

            //load interceptors
            interceptProcess.loadInterceptors();

            //interceptor before
            boolean access = interceptProcess.processBefore(paramMap);
            if (!access) {
                return;
            }

            // execute Method
            Method method = routerScanner.routeMethod(queryStringDecoder);
            routeProcess.invoke(method,queryStringDecoder) ;


            //WorkAction action = (WorkAction) actionClazz.newInstance();
            //action.execute(ZzContext.getContext(), paramMap);


            // interceptor after
            interceptProcess.processAfter(paramMap);

        } catch (Exception e) {
            exceptionCaught(ctx, e);
        } finally {
            // Response
            responseContent(ctx);

            // remove zz thread context
            ZzContext.removeContext();
        }


    }


    /**
     * Response
     *
     * @param ctx
     */
    private void responseContent(ChannelHandlerContext ctx) {

        ZzResponse zzResponse = ZzContext.getResponse();
        String context = zzResponse.getHttpContent() ;

        ByteBuf buf = Unpooled.wrappedBuffer(context.getBytes(StandardCharsets.UTF_8));
        DefaultFullHttpResponse response = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.OK, buf);
        buildHeader(response);
        ctx.writeAndFlush(response);
    }

    /**
     * build paramMap
     *
     * @param queryStringDecoder
     * @return
     */
    private Param buildParamMap(QueryStringDecoder queryStringDecoder) {
        Map<String, List<String>> parameters = queryStringDecoder.parameters();
        Param paramMap = new ParamMap();
        for (Map.Entry<String, List<String>> stringListEntry : parameters.entrySet()) {
            String key = stringListEntry.getKey();
            List<String> value = stringListEntry.getValue();
            paramMap.put(key, value.get(0));
        }
        return paramMap;
    }




    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {

        if (ZzException.isResetByPeer(cause.getMessage())){
            return;
        }

        LOGGER.error(cause.getMessage(), cause);

        WorkRes workRes = new WorkRes();
        workRes.setCode(String.valueOf(HttpResponseStatus.NOT_FOUND.code()));
        workRes.setMessage(cause.getMessage());

        DefaultFullHttpResponse response = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.NOT_FOUND, Unpooled.copiedBuffer(JSON.toJSONString(workRes), CharsetUtil.UTF_8));
        buildHeader(response);
        ctx.writeAndFlush(response);
    }

    /**
     * build Header
     *
     * @param response
     */
    private void buildHeader(DefaultFullHttpResponse response) {
        ZzResponse zzResponse = ZzContext.getResponse();

        HttpHeaders headers = response.headers();
        headers.setInt(HttpHeaderNames.CONTENT_LENGTH, response.content().readableBytes());
        headers.set(HttpHeaderNames.CONTENT_TYPE, zzResponse.getContentType());

        List<Cookie> cookies = zzResponse.cookies();
        for (Cookie cookie : cookies) {
            headers.add(ZzConstant.ContentType.SET_COOKIE, io.netty.handler.codec.http.cookie.ServerCookieEncoder.LAX.encode(cookie));
        }

    }
}
