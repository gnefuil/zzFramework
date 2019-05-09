package com.javapex.service.v5;

import com.javapex.beans.factory.annotation.Autowired;
import com.javapex.dao.v5.AccountDao;
import com.javapex.dao.v5.ItemDao;
import com.javapex.stereotype.Component;
import com.javapex.util.MessageTracker;

@Component(value="petStore")
public class PetStoreService {
    @Autowired
    AccountDao accountDao;
    @Autowired
    ItemDao itemDao;

    public PetStoreService() {

    }

    public ItemDao getItemDao() {
        return itemDao;
    }

    public AccountDao getAccountDao() {
        return accountDao;
    }

    public void placeOrder(){
        System.out.println("place order");
        MessageTracker.addMsg("place order");

    }
    public void placeOrderWithException(){
        throw new NullPointerException();
    }
}
