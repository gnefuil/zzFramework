package com.javapex.zz.web.config;

import com.javapex.zz.web.ZzServer;
import com.javapex.zz.web.bean.ZzBeanManager;
import com.javapex.zz.web.configuration.AbstractZzConfiguration;
import com.javapex.zz.web.configuration.ApplicationConfiguration;
import com.javapex.zz.web.configuration.ConfigurationHolder;
import com.javapex.zz.web.constant.ZzConstant;
import com.javapex.zz.web.exception.ZzException;
import com.javapex.zz.web.reflect.ClassScanner;
import com.javapex.zz.web.thread.ThreadLocalHolder;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.List;
import java.util.Properties;

import static com.javapex.zz.web.configuration.ConfigurationHolder.getConfiguration;
import static com.javapex.zz.web.constant.ZzConstant.SystemProperties.APPLICATION_THREAD_MAIN_NAME;

public class ZzSetting {
    public static void setting(Class<?> clazz, String rootPath) throws Exception {

        //  logo
        logo();

        //Initialize the application configuration
        initConfiguration(clazz);

        //Set application configuration
        setAppConfig(rootPath);

        //init route bean factory
        ZzBeanManager.getInstance().init(rootPath);
    }

    private static void logo() {
//        System.out.println(LOGO);
        Thread.currentThread().setName(APPLICATION_THREAD_MAIN_NAME) ;
    }

    private static void setAppConfig(String rootPath) {
        ApplicationConfiguration applicationConfiguration = (ApplicationConfiguration) getConfiguration(ApplicationConfiguration.class);

        if (rootPath == null) {
            rootPath = applicationConfiguration.get(ZzConstant.ROOT_PATH);
        }
        String port = applicationConfiguration.get(ZzConstant.ZZ_PORT);

        if (rootPath == null) {
            throw new ZzException("No [zz.root.path] exists ");
        }
        if (port == null) {
            throw new ZzException("No [zz.port] exists ");
        }
        AppConfig.getInstance().setRootPath(rootPath);
        AppConfig.getInstance().setPort(Integer.parseInt(port));
    }

    private static void initConfiguration(Class<?> clazz) throws Exception {
        ThreadLocalHolder.setLocalTime(System.currentTimeMillis());
        AppConfig.getInstance().setRootPackageName(clazz);

        List<Class<?>> configuration = ClassScanner.getConfiguration(AppConfig.getInstance().getRootPackageName());
        for (Class<?> aClass : configuration) {
            AbstractZzConfiguration conf = (AbstractZzConfiguration) aClass.newInstance();

            // First read
            InputStream stream ;
            String systemProperty = System.getProperty(conf.getPropertiesName());
            if (systemProperty != null) {
                stream = new FileInputStream(new File(systemProperty));
            } else {
                stream = ZzServer.class.getClassLoader().getResourceAsStream(conf.getPropertiesName());
            }

            Properties properties = new Properties();
            properties.load(stream);
            conf.setProperties(properties);

            // add configuration cache
            ConfigurationHolder.addConfiguration(aClass.getName(), conf);
        }
    }
}
