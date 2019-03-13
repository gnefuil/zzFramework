package com.javapex.test;

import com.javapex.test.v1.V1AllTests;
import com.javapex.test.v2.V2AllTests;
import com.javapex.test.v3.V3AllTests;
import com.javapex.test.v4.V4AllTests;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(Suite.class)
@Suite.SuiteClasses({V1AllTests.class,V2AllTests.class,V3AllTests.class,V4AllTests.class})
public class AllTests {

}
