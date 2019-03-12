package com.javapex.zz.web;

import com.javapex.zz.web.bootstrap.NettyBootStrap;
import com.javapex.zz.web.config.ZzSetting;

public class ZzServer {
    public static void start(Class<?> clazz,String path) throws Exception {
        ZzSetting.setting(clazz,path) ;
        NettyBootStrap.startZz();
    }
}
