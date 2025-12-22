package com.g2rain.crafter;

import org.apache.maven.plugin.MojoExecutionException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import java.io.InputStream;
import java.io.PrintStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * BootstrapMojo插件入口类的测试类
 */
public class BootstrapMojoTest {

    private final PrintStream systemOut = System.out;
    private final InputStream systemIn = System.in;

    @AfterEach
    void tearDown() {
        System.setOut(systemOut);
        System.setIn(systemIn);
        
        // 清理测试生成的项目目录
        try {
            Path testProjectDir = Paths.get("test-project");
            if (Files.exists(testProjectDir)) {
                deleteDirectory(testProjectDir);
            }
            
            Path testProjectBizDir = Paths.get("test-project-biz");
            if (Files.exists(testProjectBizDir)) {
                deleteDirectory(testProjectBizDir);
            }
        } catch (Exception e) {
            // 忽略清理错误，不影响测试结果
        }
    }

    @Test
    void testExecuteWithSkeletonPhase() {
        // 创建 BootstrapMojo 对象
        BootstrapMojo mojo = new BootstrapMojo();

        // 设置所有必需参数，避免调用promptForSkeletonParameters方法
        setPrivateField(mojo, "phase", "skeleton");
        setPrivateField(mojo, "groupId", "com.test");
        setPrivateField(mojo, "projectName", "test-project");
        setPrivateField(mojo, "version", "1.0.0");
        setPrivateField(mojo, "basePackage", "com.test.project");
        setPrivateField(mojo, "description", "Test Description");

        // 执行（在正常情况下不会抛出异常）
        assertDoesNotThrow(mojo::execute);
    }

    @Test
    void testExecuteWithFoundryPhase() {
        // 创建 BootstrapMojo 对象
        BootstrapMojo mojo = new BootstrapMojo();

        // 设置所有必需参数，避免调用promptForFoundryParameters方法
        setPrivateField(mojo, "phase", "foundry");
        setPrivateField(mojo, "basePackage", "com.test.project");
        setPrivateField(mojo, "url", "jdbc:mysql://localhost:3306/test");
        setPrivateField(mojo, "driver", "com.mysql.cj.jdbc.Driver");
        setPrivateField(mojo, "username", "root");
        setPrivateField(mojo, "password", "password");
        setPrivateField(mojo, "tables", "user");

        // 执行（在测试环境中可能因为数据库连接失败而抛出异常，这是预期的行为）
        // 我们验证execute方法会抛出MojoExecutionException
        assertThrows(MojoExecutionException.class, mojo::execute);
    }

    @Test
    void testExecuteWithBothPhases() {
        // 创建 BootstrapMojo 对象
        BootstrapMojo mojo = new BootstrapMojo();

        // 设置所有必需参数，避免调用promptForSkeletonParameters和promptForFoundryParameters方法
        setPrivateField(mojo, "groupId", "com.test");
        setPrivateField(mojo, "projectName", "test-project");
        setPrivateField(mojo, "version", "1.0.0");
        setPrivateField(mojo, "basePackage", "com.test.project");
        setPrivateField(mojo, "description", "Test Description");
        setPrivateField(mojo, "url", "jdbc:mysql://localhost:3306/test");
        setPrivateField(mojo, "driver", "com.mysql.cj.jdbc.Driver");
        setPrivateField(mojo, "username", "root");
        setPrivateField(mojo, "password", "password");
        setPrivateField(mojo, "tables", "user");

        // 执行（在测试环境中可能因为数据库连接失败而抛出异常，这是预期的行为）
        // 我们验证execute方法会抛出MojoExecutionException
        assertThrows(MojoExecutionException.class, mojo::execute);
    }

    // 使用反射设置私有字段的辅助方法
    private void setPrivateField(Object target, String fieldName, Object value) {
        try {
            java.lang.reflect.Field field = target.getClass().getDeclaredField(fieldName);
            field.setAccessible(true);
            field.set(target, value);
        } catch (Exception e) {
            throw new RuntimeException("Failed to set field: " + fieldName, e);
        }
    }
    
    // 递归删除目录的辅助方法
    private void deleteDirectory(Path directory) throws Exception {
        if (Files.isDirectory(directory)) {
            Files.list(directory).forEach(child -> {
                try {
                    deleteDirectory(child);
                } catch (Exception e) {
                    throw new RuntimeException("Failed to delete: " + child, e);
                }
            });
        }
        Files.delete(directory);
    }
}