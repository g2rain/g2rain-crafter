package com.g2rain.crafter.config;

import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * SkeletonConfig配置类的测试类
 */
public class SkeletonConfigTest {

    @Test
    void testSkeletonConfigConstructor() {
        String groupId = "com.g2rain";
        String projectName = "g2rain-demo";
        String version = "1.0.0";
        String basePackage = "com.g2rain.demo";
        String description = "示例项目";

        SkeletonConfig config = new SkeletonConfig(groupId, projectName, version, basePackage, description);

        assertEquals(groupId, config.getGroupId());
        assertEquals(projectName, config.getProjectName());
        assertEquals(version, config.getVersion());
        assertEquals(basePackage, config.getBasePackage());
        assertEquals(description, config.getDescription());
    }

    @Test
    void testSkeletonConfigSettersAndGetters() {
        SkeletonConfig config = new SkeletonConfig("com.test", "test-project", "0.1.0", "com.test.project", "测试项目");

        String newGroupId = "com.newtest";
        String newProjectName = "new-test-project";
        String newVersion = "2.0.0";
        String newBasePackage = "com.newtest.project";
        String newDescription = "新测试项目";

        config.setGroupId(newGroupId);
        config.setProjectName(newProjectName);
        config.setVersion(newVersion);
        config.setBasePackage(newBasePackage);
        config.setDescription(newDescription);

        assertEquals(newGroupId, config.getGroupId());
        assertEquals(newProjectName, config.getProjectName());
        assertEquals(newVersion, config.getVersion());
        assertEquals(newBasePackage, config.getBasePackage());
        assertEquals(newDescription, config.getDescription());
    }

    @Test
    void testToDataMethod() {
        String groupId = "com.g2rain";
        String projectName = "g2rain-demo";
        String version = "1.0.0";
        String basePackage = "com.g2rain.demo";
        String description = "示例项目";

        SkeletonConfig config = new SkeletonConfig(groupId, projectName, version, basePackage, description);
        Map<String, Object> data = config.toData();

        assertEquals(groupId, data.get("groupId"));
        assertEquals(projectName, data.get("projectName"));
        assertEquals(version, data.get("version"));
        assertEquals(description, data.get("description"));
        assertEquals(basePackage, data.get("package"));
        assertEquals(5, data.size());
    }

    @Test
    void testSkeletonConfigWithNullValues() {
        SkeletonConfig config = new SkeletonConfig(null, null, null, null, null);

        assertNull(config.getGroupId());
        assertNull(config.getProjectName());
        assertNull(config.getVersion());
        assertNull(config.getBasePackage());
        assertNull(config.getDescription());

        // 测试toData方法会抛出空指针异常（这是预期行为，因为Map.of不接受null值）
        assertThrows(NullPointerException.class, config::toData);
    }
}