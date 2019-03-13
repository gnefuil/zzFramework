package com.javapex.test.v4;

import com.javapex.beans.factory.annotation.AutowiredAnnotationProcessor;
import com.javapex.beans.factory.annotation.AutowiredFieldElement;
import com.javapex.beans.factory.annotation.InjectionElement;
import com.javapex.beans.factory.annotation.InjectionMetadata;
import com.javapex.beans.factory.config.DependencyDescriptor;
import com.javapex.beans.factory.support.DefaultBeanFactory;
import com.javapex.dao.v4.AccountDao;
import com.javapex.dao.v4.ItemDao;
import com.javapex.service.v4.PetStoreService;
import org.junit.Assert;
import org.junit.Test;

import java.lang.reflect.Field;
import java.util.List;

public class AutowiredAnnotationProcessorTest {

    AccountDao accountDao = new AccountDao();
    ItemDao itemDao = new ItemDao();

    DefaultBeanFactory beanFactory = new DefaultBeanFactory(){
        //匿名类，不用读取xml了，采用mock方式
        public Object resolveDependency(DependencyDescriptor descriptor){
            if(descriptor.getDependencyType().equals(AccountDao.class)){
                return accountDao;
            }
            if(descriptor.getDependencyType().equals(ItemDao.class)){
                return itemDao;
            }
            throw new RuntimeException("can't support types except AccountDao and ItemDao");
        }
    };



    @Test
    public void testGetInjectionMetadata(){

        AutowiredAnnotationProcessor processor = new AutowiredAnnotationProcessor();
        processor.setBeanFactory(beanFactory);
        InjectionMetadata injectionMetadata = processor.buildAutowiringMetadata(PetStoreService.class);
        List<InjectionElement> elements = injectionMetadata.getInjectionElements();
        Assert.assertEquals(2, elements.size());

        assertFieldExists(elements,"accountDao");
        assertFieldExists(elements,"itemDao");

        PetStoreService petStore = new PetStoreService();

        injectionMetadata.inject(petStore);

        Assert.assertTrue(petStore.getAccountDao() instanceof AccountDao);

        Assert.assertTrue(petStore.getItemDao() instanceof ItemDao);
    }

    private void assertFieldExists(List<InjectionElement> elements ,String fieldName){
        for(InjectionElement ele : elements){
            AutowiredFieldElement fieldEle = (AutowiredFieldElement)ele;
            Field f = fieldEle.getField();
            if(f.getName().equals(fieldName)){
                return;
            }
        }
        Assert.fail(fieldName + "does not exist!");
    }

}
