package com.javapex.test.v2;

import com.javapex.context.ApplicationContext;
import com.javapex.context.support.ClassPathXmlApplicationContext;
import com.javapex.dao.v2.AccountDao;
import com.javapex.dao.v2.ItemDao;
import com.javapex.service.v2.PetStoreService;
import org.junit.Test;

import static org.junit.Assert.*;

public class ApplicationContextTestV2 {

    @Test
    public void testGetBeanProperty() {

        ApplicationContext ctx = new ClassPathXmlApplicationContext("petstore-v2.xml");
        PetStoreService petStore = (PetStoreService)ctx.getBean("petStore");

        assertNotNull(petStore.getAccountDao());
        assertNotNull(petStore.getItemDao());

        assertTrue(petStore.getAccountDao() instanceof AccountDao);
        assertTrue(petStore.getItemDao() instanceof ItemDao);

        assertEquals("lf",petStore.getOwner());

        assertEquals(2, petStore.getVersion());
    }
}
