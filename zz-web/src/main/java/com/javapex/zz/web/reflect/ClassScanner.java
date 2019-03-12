package com.javapex.zz.web.reflect;

import com.javapex.zz.web.annotation.Interceptor;
import com.javapex.zz.web.annotation.ZzAction;
import com.javapex.zz.web.bean.ZzBeanFactory;
import com.javapex.zz.web.bean.ZzDefaultBean;
import com.javapex.zz.web.configuration.AbstractZzConfiguration;
import com.javapex.zz.web.configuration.ApplicationConfiguration;
import com.javapex.zz.web.enums.StatusEnum;
import com.javapex.zz.web.exception.ZzException;
import com.javapex.zz.web.log.LoggerBuilder;

import org.slf4j.Logger;

import java.io.File;
import java.io.IOException;
import java.lang.annotation.Annotation;
import java.net.JarURLConnection;
import java.net.URL;
import java.net.URLDecoder;
import java.util.*;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

public class ClassScanner {


    private final static Logger LOGGER = LoggerBuilder.getLogger(ClassScanner.class);


    private static Map<String, Class<?>> actionMap = null;
    private static Map<Class<?>, Integer> interceptorMap = null;

    private static Set<Class<?>> classes = null;
    private static Set<Class<?>> zz_classes = null;

    private static List<Class<?>> configurationList = null;


    /**
     * get Configuration
     *
     * @param packageName
     * @return
     * @throws Exception
     */
    public static List<Class<?>> getConfiguration(String packageName) throws Exception {

        if (configurationList == null) {
            Set<Class<?>> clsList = getClasses(packageName);

            // Manually add ApplicationConfiguration
            clsList.add(ApplicationConfiguration.class) ;

            if (clsList == null || clsList.isEmpty()) {
                return configurationList;  //这不就是返回 null 吗
            }

            configurationList = new ArrayList<>(16);
            for (Class<?> cls : clsList) {

                if (cls.getSuperclass() != AbstractZzConfiguration.class) {
                    continue;
                }

                configurationList.add(cls) ;
            }
        }
        return configurationList;
    }
    /**
     * get @ZzAction
     *
     * @param packageName
     * @return
     * @throws Exception
     */
    public static Map<String, Class<?>> getZzAction(String packageName) throws Exception {

        if (actionMap == null) {
            Set<Class<?>> clsList = getClasses(packageName);

            if (clsList == null || clsList.isEmpty()) {
                return actionMap;
            }

            actionMap = new HashMap<>(16);
            for (Class<?> cls : clsList) {

                Annotation annotation = cls.getAnnotation(ZzAction.class);
                if (annotation == null) {
                    continue;
                }

                ZzAction zzAction = (ZzAction) annotation;
                actionMap.put(zzAction.value() == null ? cls.getName() : zzAction.value(), cls);

            }
        }
        return actionMap;
    }

    /**
     * get @Interceptor
     *
     * @param packageName
     * @return
     * @throws Exception
     */
    public static Map<Class<?>, Integer> getZzInterceptor(String packageName) throws Exception {

        if (interceptorMap == null) {
            Set<Class<?>> clsList = getClasses(packageName);

            if (clsList == null || clsList.isEmpty()) {
                return interceptorMap;
            }

            interceptorMap = new HashMap<>(16);
            for (Class<?> cls : clsList) {
                Annotation annotation = cls.getAnnotation(Interceptor.class);
                if (annotation == null) {
                    continue;
                }

                Interceptor interceptor = (Interceptor) annotation;
                interceptorMap.put(cls, interceptor.order());

            }
        }

        return interceptorMap;
    }

    /**
     * get All classes
     *
     * @param packageName
     * @return
     * @throws Exception
     */
    public static Set<Class<?>> getClasses(String packageName) throws Exception {

        if (classes == null){
            classes = new HashSet<>(32) ;

            baseScanner(packageName,classes);
        }

        return classes;
    }

    /**
     * 把 packageName 下的东西放到 set 里面
     * @param packageName
     * @param set
     */
    private static void baseScanner(String packageName,Set set) {
        boolean recursive = true; //recursive 递归，回归

        String packageDirName = packageName.replace('.', '/');

        Enumeration<URL> dirs;
        try {
            //Finds all the resources with the given name.
            dirs = Thread.currentThread().getContextClassLoader().getResources(packageDirName);
            while (dirs.hasMoreElements()) {
                URL url = dirs.nextElement();
                String protocol = url.getProtocol();
                if ("file".equals(protocol)) {
                    String filePath = URLDecoder.decode(url.getFile(), "UTF-8");
                    findAndAddClassesInPackageByFile(packageName, filePath, recursive, set);        //？？？
                } else if ("jar".equals(protocol)) {
                    JarFile jar;
                    try {
                        jar = ((JarURLConnection) url.openConnection()).getJarFile();
                        Enumeration<JarEntry> entries = jar.entries();
                        while (entries.hasMoreElements()) {
                            JarEntry entry = entries.nextElement();
                            String name = entry.getName();
                            if (name.charAt(0) == '/') {
                                name = name.substring(1);
                            }
                            if (name.startsWith(packageDirName)) {
                                int idx = name.lastIndexOf('/');
                                if (idx != -1) {
                                    packageName = name.substring(0, idx).replace('/', '.');
                                }
                                if ((idx != -1) || recursive) {
                                    if (name.endsWith(".class") && !entry.isDirectory()) {
                                        String className = name.substring(packageName.length() + 1, name.length() - 6);
                                        try {
                                            set.add(Class.forName(packageName + '.' + className));
                                        } catch (ClassNotFoundException e) {
                                            e.printStackTrace();
                                        }
                                    }
                                }
                            }
                        }
                    } catch (IOException e) {
                        LOGGER.error("IOException", e);
                    }
                }
            }
        } catch (IOException e) {
            LOGGER.error("IOException", e);
        }
    }


    public static void findAndAddClassesInPackageByFile(String packageName,
           String packagePath, final boolean recursive, Set<Class<?>> classes) {
        File dir = new File(packagePath);
        if (!dir.exists() || !dir.isDirectory()) {
            return;
        }
        File[] files = dir.listFiles(file -> (recursive && file.isDirectory())
                || (file.getName().endsWith(".class")));
        for (File file : files) {
            if (file.isDirectory()) {
                findAndAddClassesInPackageByFile(packageName + "."
                                + file.getName(), file.getAbsolutePath(), recursive,
                        classes);
            } else {
                String className = file.getName().substring(0,
                        file.getName().length() - 6);
                try {
                    classes.add(Thread.currentThread().getContextClassLoader().loadClass(packageName + '.' + className));
                } catch (ClassNotFoundException e) {
                    LOGGER.error("ClassNotFoundException", e);
                }
            }
        }
    }


    private static final String BASE_PACKAGE = "com.javapex.zz.web"; // todo 包名

    /**
     * get custom route bean
     * @return
     * @throws Exception
     */
    public static Class<?> getCustomRouteBean() throws Exception {
        List<Class<?>> classList = new ArrayList<>();


        Set<Class<?>> classes = getCustomRouteBeanClasses(BASE_PACKAGE) ;
        for (Class<?> aClass : classes) {

            if (aClass.getInterfaces().length == 0){
                continue;
            }
            if (aClass.getInterfaces()[0] != ZzBeanFactory.class){
                continue;
            }
            classList.add(aClass) ;
        }

        if (classList.size() > 2){
            throw new ZzException(StatusEnum.DUPLICATE_IOC);
        }

        if (classList.size() == 2){
            Iterator<Class<?>> iterator = classList.iterator();
            while (iterator.hasNext()){
                if (iterator.next()== ZzDefaultBean.class){
                    iterator.remove();
                }
            }
        }
        if (classList.size()==0)
            return null;

        return classList.get(0);
    }


    public static Set<Class<?>> getCustomRouteBeanClasses(String packageName) throws Exception {

        if (zz_classes == null){
            zz_classes = new HashSet<>(32) ;

            baseScanner(packageName,zz_classes);
        }

        return zz_classes;
    }
}
