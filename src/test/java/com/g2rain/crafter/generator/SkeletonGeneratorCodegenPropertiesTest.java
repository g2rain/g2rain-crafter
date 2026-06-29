package com.g2rain.crafter.generator;

import com.g2rain.crafter.config.SkeletonConfig;
import org.apache.maven.plugin.logging.Log;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class SkeletonGeneratorCodegenPropertiesTest {

    private static final String PROJECT_NAME = "codegen-skeleton-test";

    @AfterEach
    void tearDown() throws Exception {
        Path projectDir = Paths.get(PROJECT_NAME);
        if (Files.exists(projectDir)) {
            deleteDirectory(projectDir);
        }
    }

    @Test
    void generatedCodegenPropertiesHasNoUnresolvedPlaceholders() throws Exception {
        Log log = Mockito.mock(Log.class);
        SkeletonConfig config = new SkeletonConfig(
            "com.test",
            PROJECT_NAME,
            "1.0.0",
            "com.test.demo",
            "codegen test project"
        );

        new SkeletonGenerator(log, config).generate();

        Path codegen = Paths.get(PROJECT_NAME, "codegen.properties");
        assertTrue(Files.exists(codegen), "codegen.properties should be generated");

        String content = Files.readString(codegen);
        assertFalse(content.contains("${package}"), "package placeholder should be rendered");
        assertFalse(content.contains("${projectName}"), "projectName placeholder should be rendered");
        assertTrue(content.contains("archetype.package=com.test.demo"));
        assertFalse(content.contains("project.basePackage="));
        assertTrue(content.contains("jdbc:mysql://localhost:3306/" + PROJECT_NAME));
    }

    private void deleteDirectory(Path directory) throws Exception {
        if (Files.isDirectory(directory)) {
            try (var children = Files.list(directory)) {
                for (Path child : children.toList()) {
                    deleteDirectory(child);
                }
            }
        }
        Files.deleteIfExists(directory);
    }
}
