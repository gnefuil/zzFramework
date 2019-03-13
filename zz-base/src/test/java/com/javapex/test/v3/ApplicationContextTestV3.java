package com.javapex.test.v3;

import com.javapex.context.ApplicationContext;
import com.javapex.context.support.ClassPathXmlApplicationContext;
import com.javapex.service.v3.PetStoreService;
import org.junit.Assert;
import org.junit.Test;

public class ApplicationContextTestV3 {
    @Test
    public void testGetBeanProperty() {
        ApplicationContext ctx = new ClassPathXmlApplicationContext("petstore-v3.xml");
        PetStoreService petStore = (PetStoreService)ctx.getBean("petStore");

        Assert.assertNotNull(petStore.getAccountDao());
        Assert.assertNotNull(petStore.getItemDao());
        Assert.assertEquals(1, petStore.getVersion());

    }
}
