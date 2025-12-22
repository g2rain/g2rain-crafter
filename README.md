# g2rain-crafter

[![Maven Central](https://img.shields.io/maven-central/v/com.g2rain/g2rain-crafter.svg)](https://search.maven.org/artifact/com.g2rain/g2rain-crafter)
[![License](https://img.shields.io/badge/license-Apache%202.0-blue.svg)](LICENSE)
[![Java Version](https://img.shields.io/badge/Java-21+-orange.svg)](https://openjdk.java.net/)
[![Build Status](https://img.shields.io/github/actions/workflow/status/g2rain/g2rain-crafter/maven.yml?branch=main)](https://github.com/g2rain/g2rain-crafter/actions)
[![Quality Gate](https://img.shields.io/sonar/quality_gate/g/g2rain/g2rain-crafter)](https://sonarcloud.io/project/overview?id=g2rain_crafter)

---

## 📋 项目简介

g2rain-crafter 是一个企业级 Maven 插件，用于快速生成项目骨架和业务模块代码。通过配置数据库表结构，可自动生成 PO、DTO、DAO、Service、Controller、Mapper 等完整代码，支持 Spring Boot 项目结构化开发。

核心目标：

* 快速生成项目骨架（Parent POM + 模块 POM + 启动类）
* 自动生成业务模块代码（基于表结构的 CRUD 与 DTO、VO、API）
* 支持交互式输入和非交互式配置
* 提升微服务开发效率，减少重复代码编写

## ✨ 核心功能

* **项目骨架生成（skeleton）**：

    * 自动生成 Parent POM、子模块 POM（API / BIZ / STARTUP）
    * 生成 Application 启动类
    * 支持交互式输入和非交互式参数配置

* **业务代码生成（foundry）**：

    * 基于数据库表生成完整 CRUD 代码：PO / DTO / VO / DAO / Mapper XML / Service / ServiceImpl / Controller / API
    * 支持多表生成，表名用逗号分隔
    * 可控制生成阶段：`-Dphase=skeleton` 仅生成骨架，`-Dphase=foundry` 仅生成业务代码，不指定则执行完整流程
    * 支持基于表结构的 Java 类型映射，自动识别主键、自增字段和基础列（create_time / update_time）

* **模板与自定义**：

    * 使用 Freemarker 模板引擎生成代码
    * 可根据 `basePackage` 自动生成包路径
    * 支持模板文件覆盖，跳过已有文件控制生成行为

* **交互与非交互式输入**：

    * 插件在缺少参数时提示命令行输入
    * 可在 POM `<configuration>` 或命令行直接传参数，实现全自动生成

## 🚀 快速开始

### 环境要求

* Java 21+
* Maven 3.6+
* Spring Boot 3.5+

### 安装插件

在项目根 POM 中添加插件：

```xml
<!-- 都是在 增量生成业务代码场景, 并且需要配合 codegen.properties 文件 -->
<pluginManagement>
    <plugins>
        <plugin>
            <groupId>com.g2rain</groupId>
            <artifactId>g2rain-crafter</artifactId>
            <version>1.0.1</version>
            <configuration>
                <phase>foundry</phase>
                <!-- 可选：全局默认配置 -->
                <configFile>${project.basedir}/codegen.properties</configFile>
            </configuration>
        </plugin>
    </plugins>
</pluginManagement>
<plugin>
    <groupId>com.g2rain</groupId>
    <artifactId>g2rain-crafter</artifactId>
    <!-- 根模块执行, 确保子模块不会自动继承 -->
    <inherited>false</inherited>
    <executions>
        <execution>
            <id>bootstrap-execution</id>
            <!-- 不绑定任何生命周期 -->
            <phase>none</phase>
            <goals>
                <goal>bootstrap</goal>
            </goals>
        </execution>
    </executions>
</plugin>
```

### 配置文件示例（可选）

`codegen.properties` 文件示例：

```properties
# =============================================
# G2Rain Crafter 代码生成配置文件
# =============================================

# 项目配置
archetype.package=com.g2rain.demo

# 数据库连接配置
database.url=jdbc:mysql://localhost:3306/g2rain-demo?useSSL=false&serverTimezone=UTC&allowPublicKeyRetrieval=true
database.driver=com.mysql.cj.jdbc.Driver
database.username=root
database.password=root123456

# 待生成的数据库表名列表，用英文逗号分隔
database.tables=user,product,trade

# 文件覆盖控制：true 覆盖，false 跳过
tables.overwrite=false
```

### Maven 插件使用示例

#### 1️⃣ 交互式生成（缺少参数时会提示输入）

生成骨架 + 业务模块（完整流程）：

```bash
# 项目无需安装插件, 直接运行即可
mvn com.g2rain:g2rain-crafter:1.0.1:bootstrap
```

仅生成项目骨架：

```bash
# 项目无需安装插件, 直接运行即可
mvn com.g2rain:g2rain-crafter:1.0.1:bootstrap -Dphase=skeleton
```

仅生成业务代码：

```bash
# 项目无需安装插件, 直接运行即可
mvn com.g2rain:g2rain-crafter:1.0.1:bootstrap -Dphase=foundry
```

#### 2️⃣ 非交互式生成（直接传参数，无需输入）

完整生成流程示例：

```bash
# 项目无需安装插件, 直接运行即可
mvn com.g2rain:g2rain-crafter:1.0.1:bootstrap \
  -Darchetype.groupId=com.g2rain \
  -Darchetype.artifactId=g2rain-dmeo \
  -Darchetype.version=1.0.0 \
  -Darchetype.package=com.g2rain.demo \
  -Darchetype.description="示例项目" \
  -Ddatabase.url=jdbc:mysql://localhost:3306/g2rain-demo \
  -Ddatabase.driver=com.mysql.cj.jdbc.Driver \
  -Ddatabase.username=root \
  -Ddatabase.password=root123456 \
  -Ddatabase.tables=user \
  -Dtables.overwrite=true
```

> ⚡ 注意：通过命令行传参数时，**所有必需参数必须提供**，否则会降级为交互式模式。

### 生成效果

* Parent POM + 模块 POM（API / BIZ / STARTUP）
* Application 启动类（Startup 模块）
* DTO / VO / DAO / Mapper XML / Service / ServiceImpl / Controller / API

### 目录结构示例

```
demo-project/
├── demo-project-api/
├── demo-project-biz/
├── demo-project-startup/
│   └── src/main/java/com/example/demo/config/VirtualThreadConfigurer.java
│   └── src/main/java/com/example/demo/Application.java
├── pom.xml
```

## 🧪 测试与验证

* 使用 MySQL 测试表结构生成对应代码
* 确认 DTO / VO / DAO / Service / Controller / API 生成正常
* 验证启动类能正常运行 Spring Boot 应用

## 📄 许可证

本项目基于 [Apache 2.0许可证](LICENSE) 开源。

## 📞 联系我们

* **Issues**: [GitHub Issues](https://github.com/g2rain/g2rain/issues)
* **讨论**: [GitHub Discussions](https://github.com/g2rain/g2rain/discussions)
* **邮箱**: [support@g2rain.com](mailto:g2rain_developer@163.com)
