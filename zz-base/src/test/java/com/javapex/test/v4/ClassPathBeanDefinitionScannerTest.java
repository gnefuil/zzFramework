package com.javapex.test.v4;

import com.javapex.beans.BeanDefinition;
import com.javapex.beans.factory.support.DefaultBeanFactory;
import com.javapex.context.annotation.ClassPathBeanDefinitionScanner;
import com.javapex.context.annotation.ScannedGenericBeanDefinition;
import com.javapex.core.annotation.AnnotationAttributes;
import com.javapex.core.type.AnnotationMetadata;
import com.javapex.stereotype.Component;
import org.junit.Assert;
import org.junit.Test;
public class ClassPathBeanDefinitionScannerTest {

    @Test
    public void testParseScanedBean(){
        /**
         * 对指定的package 进行扫描(scan)，找到那些标记为@Component 的类，
         * 创建ScannedGenericBeanDefinition，并且注册到BeanFactory中。
         */
        DefaultBeanFactory factory = new DefaultBeanFactory();
        String basePackages = "com.javapex.service.v4,com.javapex.dao.v4";

        ClassPathBeanDefinitionScanner scanner = new ClassPathBeanDefinitionScanner(factory);
        scanner.doScan(basePackages);

        String annotation = Component.class.getName();
        {
            BeanDefinition bd = factory.getBeanDefinition("petStore");
            Assert.assertTrue(bd instanceof ScannedGenericBeanDefinition);
            ScannedGenericBeanDefinition sbd = (ScannedGenericBeanDefinition)bd;
            AnnotationMetadata amd = sbd.getMetadata();

            Assert.assertTrue(amd.hasAnnotation(annotation));
            AnnotationAttributes attributes = amd.getAnnotationAttributes(annotation);
            Assert.assertEquals("petStore", attributes.get("value"));
        }
        {
            BeanDefinition bd = factory.getBeanDefinition("accountDao");
            Assert.assertTrue(bd instanceof ScannedGenericBeanDefinition);
            ScannedGenericBeanDefinition sbd = (ScannedGenericBeanDefinition)bd;
            AnnotationMetadata amd = sbd.getMetadata();
            Assert.assertTrue(amd.hasAnnotation(annotation));
        }
        {
            BeanDefinition bd = factory.getBeanDefinition("itemDao");
            Assert.assertTrue(bd instanceof ScannedGenericBeanDefinition);
            ScannedGenericBeanDefinition sbd = (ScannedGenericBeanDefinition)bd;
            AnnotationMetadata amd = sbd.getMetadata();
            Assert.assertTrue(amd.hasAnnotation(annotation));
        }
    }
}
