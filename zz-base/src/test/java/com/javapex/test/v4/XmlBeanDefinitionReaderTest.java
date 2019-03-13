package com.javapex.test.v4;
import com.javapex.beans.BeanDefinition;
import com.javapex.beans.factory.support.DefaultBeanFactory;
import com.javapex.beans.factory.xml.XmlBeanDefinitionReader;
import com.javapex.context.annotation.ScannedGenericBeanDefinition;
import com.javapex.core.annotation.AnnotationAttributes;
import com.javapex.core.io.ClassPathResource;
import com.javapex.core.io.Resource;
import com.javapex.core.type.AnnotationMetadata;
import com.javapex.stereotype.Component;
import org.junit.Assert;
import org.junit.Test;
public class XmlBeanDefinitionReaderTest {

    @Test
    public void testParseScanedBean(){

        DefaultBeanFactory factory = new DefaultBeanFactory();
        XmlBeanDefinitionReader reader = new XmlBeanDefinitionReader(factory);
        Resource resource = new ClassPathResource("petstore-v4.xml");
        reader.loadBeanDefinitions(resource);
        String annotation = Component.class.getName();

        //下面的代码和ClassPathBeanDefinitionScannerTest重复，该怎么处理？
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
