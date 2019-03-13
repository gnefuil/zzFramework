package com.javapex.test.v4;

import com.javapex.context.ApplicationContext;
import com.javapex.context.support.ClassPathXmlApplicationContext;
import com.javapex.service.v4.PetStoreService;
import org.junit.Assert;
import org.junit.Test;

public class ApplicationContextTestV4 {

    @Test
    public void testGetBeanProperty() {
        ApplicationContext ctx = new ClassPathXmlApplicationContext("petstore-v4.xml");
        PetStoreService petStore = (PetStoreService)ctx.getBean("petStore");

        Assert.assertNotNull(petStore.getAccountDao());
        Assert.assertNotNull(petStore.getItemDao());

    }
}
