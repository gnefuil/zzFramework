package com.javapex.test.v1;

import com.javapex.core.io.ClassPathResource;
import com.javapex.core.io.FileSystemResource;
import com.javapex.core.io.Resource;
import org.junit.Assert;
import org.junit.Test;

import java.io.InputStream;

public class ResourceTest {
    @Test
    public void testClassPathResource() throws Exception {

        Resource r = new ClassPathResource("petstore-v1.xml");

        InputStream is = null;

        try {
            is = r.getInputStream();
            // 注意：这个测试其实并不充分！！
            Assert.assertNotNull(is);
        } finally {
            if (is != null) {
                is.close();
            }
        }

    }

    @Test
    public void testFileSystemResource() throws Exception {
        //TODO: 处理写死的路径
		Resource r = new FileSystemResource("C:\\git-local\\MySpring\\src\\test\\resources\\petstore-v1.xml");
		InputStream is = null;
		try {
			is = r.getInputStream();
			// 注意：这个测试其实并不充分！！
			Assert.assertNotNull(is);
		} finally {
			if (is != null) {
				is.close();
			}
		}

    }
}
