package com.g2rain.crafter.generator;

import com.g2rain.crafter.config.SkeletonConfig;
import org.apache.maven.plugin.logging.Log;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.junit.jupiter.api.Assertions.assertNotNull;

/**
 * SkeletonGenerator生成器类的测试类
 */
public class SkeletonGeneratorTest {

    @Test
    void testSkeletonGeneratorConstructor() {
        Log log = Mockito.mock(Log.class);
        SkeletonConfig skeletonConfig = Mockito.mock(SkeletonConfig.class);

        SkeletonGenerator generator = new SkeletonGenerator(log, skeletonConfig);

        assertNotNull(generator);
        // 由于字段是私有的，我们只能通过构造函数的成功执行来验证
    }
}