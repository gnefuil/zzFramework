package com.javapex.context.support;

import com.javapex.beans.factory.support.DefaultBeanFactory;
import com.javapex.beans.factory.xml.XmlBeanDefinitionReader;
import com.javapex.context.ApplicationContext;
import com.javapex.core.io.FileSystemResource;
import com.javapex.core.io.Resource;

public class FileSystemXmlApplicationContext extends AbstractApplicationContext {

    public FileSystemXmlApplicationContext(String configFile) {
        super(configFile);
    }

    protected Resource getResourceByPath(String path) {
        return new FileSystemResource(path);
    }
}
