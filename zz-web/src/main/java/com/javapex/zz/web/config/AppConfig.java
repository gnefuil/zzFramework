package com.javapex.zz.web.config;

import com.javapex.zz.web.enums.StatusEnum;
import com.javapex.zz.web.exception.ZzException;
import com.javapex.zz.web.util.PathUtil;
import io.netty.handler.codec.http.QueryStringDecoder;

public final class AppConfig {

    private AppConfig() {
    }

    /**
     * simple singleton Object
     */
    private static AppConfig config;

    public static AppConfig getInstance() {
        if (config == null) {
            config = new AppConfig();
        }
        return config;
    }

    private String rootPackageName;

    private String rootPath;

    private Integer port = 7317;

    public String getRootPackageName() {
        return rootPackageName;
    }

    public void setRootPackageName(Class<?> clazz) {
        if (clazz.getPackage() == null) {
            throw new ZzException(StatusEnum.NULL_PACKAGE, "[" + clazz.getName() + ".java]:" + StatusEnum.NULL_PACKAGE.getMessage());
        }
        this.rootPackageName = clazz.getPackage().getName();
    }

    public String getRootPath() {
        return rootPath;
    }

    public void setRootPath(String rootPath) {
        this.rootPath = rootPath;
    }

    public Integer getPort() {
        return port;
    }

    public void setPort(Integer port) {
        this.port = port;
    }


    /**
     * check Root Path
     *
     * @param uri
     * @param queryStringDecoder
     * @return
     */
    public void checkRootPath(String uri, QueryStringDecoder queryStringDecoder) {
        if (!PathUtil.getRootPath(queryStringDecoder.path()).equals(this.getRootPath())) {
            throw new ZzException(StatusEnum.REQUEST_ERROR, uri);
        }
    }
}
