package com.javapex.test.v4;

import com.javapex.core.io.Resource;
import com.javapex.core.io.support.PackageResourceLoader;
import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;

public class PackageResourceLoaderTest {

    @Test
    public void testGetResources() throws IOException {
        PackageResourceLoader loader = new PackageResourceLoader();
        Resource[] resources = loader.getResources("com.javapex.dao.v4");
        Assert.assertEquals(2, resources.length);

    }
}
