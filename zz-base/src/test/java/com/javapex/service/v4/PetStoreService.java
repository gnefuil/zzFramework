package com.javapex.service.v4;

import com.javapex.beans.factory.annotation.Autowired;
import com.javapex.dao.v4.AccountDao;
import com.javapex.dao.v4.ItemDao;
import com.javapex.stereotype.Component;

@Component(value="petStore")
public class PetStoreService {

    @Autowired
    private AccountDao accountDao;

    @Autowired
    private ItemDao itemDao;

    public AccountDao getAccountDao() {
        return accountDao;
    }

    public ItemDao getItemDao() {
        return itemDao;
    }


}
