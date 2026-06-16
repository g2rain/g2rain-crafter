package com.g2rain.crafter;

import com.g2rain.generator.config.FoundryConfig;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.lang.reflect.Method;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class BootstrapMojoConfigTest {

    @TempDir
    Path tempDir;

    @Test
    void loadFoundryConfigFileReadsIsolationSettings() throws Exception {
        BootstrapMojo mojo = createMojoWithConfig("""
            archetype.package=com.demo.app
            database.url=jdbc:mysql://localhost:3306/demo
            database.driver=com.mysql.cj.jdbc.Driver
            database.username=root
            database.password=pwd
            database.tables=user
            data.isolation.withIsolation=false
            data.isolation.tenantColumns=tenant_id,organ_id
            data.isolation.excludeTables=article
            """);

        assertEquals("com.demo.app", mojo.getBasePackage());
        assertFalse(mojo.resolveWithIsolation());
        assertEquals("tenant_id,organ_id", mojo.resolveTenantColumns());
        assertEquals("article", mojo.resolveExcludeTables());

        FoundryConfig config = new FoundryConfig("demo", mojo.getBasePackage(),
            "jdbc:mysql://localhost:3306/demo", "com.mysql.cj.jdbc.Driver", "root", "pwd");
        config.setWithIsolation(mojo.resolveWithIsolation());
        config.setTenantColumns(mojo.resolveTenantColumns());
        config.setExcludeTables(mojo.resolveExcludeTables());

        assertFalse(config.isWithIsolation());
        assertEquals("tenant_id,organ_id", config.getTenantColumns());
        assertEquals("article", config.getExcludeTables());
    }

    @Test
    void cliParametersTakePriorityOverCodegenProperties() throws Exception {
        BootstrapMojo mojo = createMojoWithConfig("""
            archetype.package=com.from.file
            database.url=jdbc:mysql://localhost:3306/demo
            database.driver=com.mysql.cj.jdbc.Driver
            database.username=root
            database.password=pwd
            database.tables=user
            data.isolation.withIsolation=false
            data.isolation.tenantColumns=tenant_id,organ_id
            data.isolation.excludeTables=article
            """);

        setField(mojo, "basePackage", "com.from.cli");
        setField(mojo, "withIsolation", Boolean.TRUE);
        setField(mojo, "tenantColumns", "organ_id");
        setField(mojo, "excludeTables", "dict_type");

        assertEquals("com.from.cli", mojo.getBasePackage());
        assertTrue(mojo.resolveWithIsolation());
        assertEquals("organ_id", mojo.resolveTenantColumns());
        assertEquals("dict_type", mojo.resolveExcludeTables());
    }

    private BootstrapMojo createMojoWithConfig(String content) throws Exception {
        Path configPath = tempDir.resolve("codegen.properties");
        Files.writeString(configPath, content);

        BootstrapMojo mojo = new BootstrapMojo();
        setField(mojo, "configFile", configPath.toFile());

        Method loadConfig = BootstrapMojo.class.getDeclaredMethod("loadFoundryConfigFile");
        loadConfig.setAccessible(true);
        loadConfig.invoke(mojo);
        return mojo;
    }

    private void setField(Object target, String fieldName, Object value) throws Exception {
        var field = target.getClass().getDeclaredField(fieldName);
        field.setAccessible(true);
        field.set(target, value);
    }
}
