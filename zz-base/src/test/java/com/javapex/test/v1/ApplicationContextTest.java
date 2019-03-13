package com.javapex.test.v1;

import com.javapex.context.ApplicationContext;
import com.javapex.context.support.ClassPathXmlApplicationContext;
import com.javapex.context.support.FileSystemXmlApplicationContext;
import com.javapex.service.v1.PetStoreService;
import org.junit.Assert;
import org.junit.Test;

public class ApplicationContextTest {

    @Test
    public void testGetBean() {
        ApplicationContext ctx = new ClassPathXmlApplicationContext("petstore-v1.xml");
        PetStoreService petStore = (PetStoreService)ctx.getBean("petStore");
        Assert.assertNotNull(petStore);
    }
    @Test
    public void testGetBeanFromFileSystemContext() {
        //TODO: 处理写死的路径
        ApplicationContext ctx = new FileSystemXmlApplicationContext("D:\\@Code\\zzFramework\\zz-base\\src\\test\\resources\\petstore-v1.xml");
        PetStoreService petStore = (PetStoreService)ctx.getBean("petStore");
        Assert.assertNotNull(petStore);
    }
}
