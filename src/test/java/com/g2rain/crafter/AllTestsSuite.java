package com.g2rain.crafter;

import com.g2rain.crafter.config.SkeletonConfigTest;
import com.g2rain.crafter.generator.SkeletonGeneratorTest;
import com.g2rain.crafter.utils.ConstantsTest;
import org.junit.platform.suite.api.SelectClasses;
import org.junit.platform.suite.api.Suite;

/**
 * g2rain-crafter项目的所有测试套件
 */
@Suite
@SelectClasses({
        // utils包测试类
        ConstantsTest.class,

        SkeletonConfigTest.class,

        SkeletonGeneratorTest.class,

        BootstrapMojoTest.class,
})
public class AllTestsSuite {
}