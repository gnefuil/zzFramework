package com.javapex.zz.web.intercept;

import java.util.Comparator;

public class OrderComparator implements Comparator<ZzInterceptor> {

    @Override
    public int compare(ZzInterceptor o1, ZzInterceptor o2) {
        if (o1.getOrder() <= o2.getOrder()){
            return 1 ;
        }
        return 0;
    }
}
