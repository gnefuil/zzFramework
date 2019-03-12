package com.test;

import com.javapex.zz.web.ZzServer;
import org.junit.Test;

public class ZzTest {

    @Test
    public void main() throws Exception{

        ZzServer.start(ZzTest.class,"/zz");

    }

}
