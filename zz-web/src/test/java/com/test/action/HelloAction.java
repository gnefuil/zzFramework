package com.test.action;

import com.javapex.zz.web.annotation.ZzAction;
import com.javapex.zz.web.annotation.ZzRoute;
import com.javapex.zz.web.context.ZzContext;

@ZzAction("hehe")
public class HelloAction {

    @ZzRoute("hello")
    public void hello() throws Exception {
        ZzContext context = ZzContext.getContext();

        String url = context.request().getUrl();
        String method = context.request().getMethod();
        context.text("hello world url=" + url + " method=" + method);
    }
}
