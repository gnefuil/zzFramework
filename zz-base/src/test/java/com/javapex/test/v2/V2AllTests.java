package com.javapex.test.v2;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(Suite.class)
@Suite.SuiteClasses({   ApplicationContextTestV2.class,
                        BeanDefinitionTestV2.class,
                        BeanDefinitionValueResolverTest.class,
                        CustomBooleanEditorTest.class,
                        CustomNumberEditorTest.class,
                        TypeConverterTest.class })
public class V2AllTests {
}
