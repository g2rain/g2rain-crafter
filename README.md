# g2rain-crafter

[![Maven Central](https://img.shields.io/maven-central/v/com.g2rain/g2rain-crafter.svg)](https://search.maven.org/artifact/com.g2rain/g2rain-crafter)
[![License](https://img.shields.io/badge/license-Apache%202.0-blue.svg)](LICENSE)
[![Java Version](https://img.shields.io/badge/Java-25+-orange.svg)](https://openjdk.java.net/)
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

* Java 25+
* Maven 3.6+
* Spring Boot 4.0+

### 安装插件

在项目根 POM 中添加插件：

```xml
<!-- 都是在 增量生成业务代码场景, 并且需要配合 codegen.properties 文件 -->
<pluginManagement>
    <plugins>
        <plugin>
            <groupId>com.g2rain</groupId>
            <artifactId>g2rain-crafter</artifactId>
            <version>1.0.7</version>
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

`codegen.properties` 需放在项目根目录，foundry 阶段默认读取。配置项说明：

| 配置键 | 说明 | 示例 |
|--------|------|------|
| `archetype.package` | Java 基础包名（必填） | `com.g2rain.demo` |
| `database.url` | JDBC 连接 URL（必填） | `jdbc:mysql://localhost:3306/g2rain-demo?...` |
| `database.driver` | JDBC 驱动类（必填） | `com.mysql.cj.jdbc.Driver` |
| `database.username` | 数据库用户名（必填） | `root` |
| `database.password` | 数据库密码（可选） | `root123456` |
| `database.tables` | 待生成表名，逗号分隔（必填） | `user,product,trade` |
| `tables.overwrite` | 是否覆盖已有文件，默认 `false` | `false` |
| `data.isolation.withIsolation` | 是否生成数据隔离代码，默认 `true` | `true` |
| `data.isolation.tenantColumns` | 租户列识别，默认 `organ_id` | `organ_id` |
| `data.isolation.excludeTables` | 排除表（即使含租户列） | `dict_type,config` |

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

# 数据隔离 codegen（默认开启；运行时开关见 application.yml）
data.isolation.withIsolation=true
data.isolation.tenantColumns=organ_id
data.isolation.excludeTables=
```

请使用 `archetype.package` 配置 Java 基础包名，与 skeleton 阶段的 `-Darchetype.package` 保持一致。

> 配置文件需放在**项目根目录**（或与 `-Dconfig.file` 指定的路径一致）。foundry 阶段会优先读取该文件；命令行参数优先级高于配置文件，配置文件高于交互式输入。

---

## 📖 执行阶段说明

插件通过 `-Dphase` 控制生成范围：

| `phase` 值 | 说明 | 典型场景 |
|------------|------|----------|
| 不指定 | **完整流程**：skeleton + foundry | 从零创建新项目并生成业务代码 |
| `skeleton` | 仅生成项目骨架 | 初始化 Parent POM、子模块、启动类、`codegen.properties` |
| `foundry` | 仅生成业务代码 | 已有骨架，按表结构增量生成 CRUD 代码 |

> foundry 阶段需要在**已有 POM 的项目根目录**执行，且需配置数据库连接与待生成表名。

---

## 🔧 Maven 插件使用示例

### 运行方式说明

插件支持两种调用方式：

| 方式 | 命令示例 | 适用场景 |
|------|----------|----------|
| **直接运行**（无需安装插件） | `mvn com.g2rain:g2rain-crafter:1.0.7:bootstrap` | 快速体验、CI 一次性脚手架 |
| **项目内已配置插件** | `mvn g2rain-crafter:bootstrap` | 日常增量生成，配合 `codegen.properties` |

以下示例均使用**直接运行**写法；若已在 POM 中安装插件，可将 `com.g2rain:g2rain-crafter:1.0.7:bootstrap` 替换为 `g2rain-crafter:bootstrap`。

---

### 1️⃣ 交互式生成（缺少参数时会提示输入）

未通过命令行或配置文件提供必填项时，插件会在控制台逐步提示输入（Group ID、Artifact ID、包名、数据库 URL、表名等）。

#### 生成骨架 + 业务模块（完整流程）

适用于**空目录**或**尚无 POM** 的场景，一次性完成脚手架与代码生成：

```bash
# 项目无需安装插件，直接运行即可
mvn com.g2rain:g2rain-crafter:1.0.7:bootstrap
```

#### 仅生成项目骨架

适用于先搭好多模块结构，稍后再连库生成业务代码：

```bash
# 项目无需安装插件，直接运行即可
mvn com.g2rain:g2rain-crafter:1.0.7:bootstrap -Dphase=skeleton
```

生成内容包括：Parent POM、API/BIZ/STARTUP 子模块 POM、启动类、基础配置类，以及根目录 `codegen.properties` 模板（已预填 `archetype.package` 与默认数据库 URL）。

#### 仅生成业务代码

适用于**已有骨架项目**，在根目录放置并编辑好 `codegen.properties` 后执行：

```bash
# 需在项目根目录执行（存在 pom.xml）
mvn com.g2rain:g2rain-crafter:1.0.7:bootstrap -Dphase=foundry
```

若 POM 中已配置 `<configFile>${project.basedir}/codegen.properties</configFile>`，也可在项目内执行：

```bash
mvn g2rain-crafter:bootstrap -Dphase=foundry
```

---

### 2️⃣ 非交互式生成（命令行传参，无需输入）

适用于脚本、CI 或需要完全可复现的生成流程。**所有 skeleton 阶段必填参数须一次性提供**，否则会降级为交互式模式。

#### 完整生成流程（skeleton + foundry）

```bash
# 项目无需安装插件，直接运行即可
mvn com.g2rain:g2rain-crafter:1.0.7:bootstrap \
  -Darchetype.groupId=com.g2rain \
  -Darchetype.artifactId=g2rain-demo \
  -Darchetype.version=1.0.0 \
  -Darchetype.package=com.g2rain.demo \
  -Darchetype.description="示例项目" \
  -Ddatabase.url=jdbc:mysql://localhost:3306/g2rain-demo?useSSL=false&serverTimezone=UTC \
  -Ddatabase.driver=com.mysql.cj.jdbc.Driver \
  -Ddatabase.username=root \
  -Ddatabase.password=root123456 \
  -Ddatabase.tables=user,product \
  -Dtables.overwrite=false \
  -Ddata.isolation.withIsolation=true \
  -Ddata.isolation.tenantColumns=organ_id
```

#### 仅 skeleton（非交互式）

```bash
mvn com.g2rain:g2rain-crafter:1.0.7:bootstrap -Dphase=skeleton \
  -Darchetype.groupId=com.g2rain \
  -Darchetype.artifactId=g2rain-demo \
  -Darchetype.version=1.0.0 \
  -Darchetype.package=com.g2rain.demo \
  -Darchetype.description="示例项目"
```

#### 仅 foundry（配合 codegen.properties）

命令行参数可省略 skeleton 相关项，仅需数据库与表配置（或由 `codegen.properties` 提供）：

```bash
mvn com.g2rain:g2rain-crafter:1.0.7:bootstrap -Dphase=foundry \
  -Dconfig.file=./codegen.properties
```

> ⚡ **注意**：非交互式模式下，skeleton 必填项为 `archetype.groupId`、`archetype.artifactId`、`archetype.package`；foundry 必填项为 `database.url`、`database.driver`、`database.username`、`database.tables`。任一缺失将触发交互式补全。

---

### 3️⃣ IDE Maven 面板执行

1. 在 IDE 中打开已安装插件的项目（根模块）
2. 打开 Maven 工具窗口，展开 **Plugins → g2rain-crafter → bootstrap**
3. 双击 `bootstrap`，或在 Run Configuration 中追加 `-Dphase=foundry` 等参数
4. 确保 `codegen.properties` 位于项目根目录，或与 `<configFile>` 配置路径一致

---

### Maven 命令行参数一览

| 参数名 | 说明 | 映射配置键 | 必填阶段 |
|--------|------|------------|----------|
| `-Dphase` | 执行阶段：`skeleton` / `foundry` / 不指定 | — | 可选 |
| `-Darchetype.groupId` | Maven Group ID | — | skeleton |
| `-Darchetype.artifactId` | 项目 Artifact ID（目录名） | — | skeleton |
| `-Darchetype.version` | 项目版本，默认 `1.0.0` | — | skeleton |
| `-Darchetype.package` | Java 基础包名 | `archetype.package` | skeleton / foundry |
| `-Darchetype.description` | 项目描述 | — | 可选 |
| `-Ddatabase.url` | JDBC URL | `database.url` | foundry |
| `-Ddatabase.driver` | JDBC 驱动类 | `database.driver` | foundry |
| `-Ddatabase.username` | 数据库用户名 | `database.username` | foundry |
| `-Ddatabase.password` | 数据库密码 | `database.password` | 可选 |
| `-Ddatabase.tables` | 待生成表名，逗号分隔 | `database.tables` | foundry |
| `-Dtables.overwrite` | 是否覆盖已有文件 | `tables.overwrite` | 可选 |
| `-Dconfig.file` | 配置文件路径 | — | 可选 |
| `-Ddata.isolation.withIsolation` | 是否生成数据隔离代码 | `data.isolation.withIsolation` | 可选 |
| `-Ddata.isolation.tenantColumns` | 租户列识别 | `data.isolation.tenantColumns` | 可选 |
| `-Ddata.isolation.excludeTables` | 排除表（即使含租户列） | `data.isolation.excludeTables` | 可选 |

---

## 📦 生成效果

### skeleton 阶段

| 产出 | 说明 |
|------|------|
| Parent POM | 多模块父工程，统一依赖与插件管理（含 g2rain starter 版本） |
| 子模块 POM | `*-api`、`*-biz`、`*-startup` 三个模块 |
| 启动类 | `Application.java`（Startup 模块） |
| 配置类 | `VirtualThreadConfigurer`、`ArgumentResolverConfig` 等 |
| `codegen.properties` | 根目录代码生成配置（包名、库连接、表名等） |

### foundry 阶段（每张表）

| 层级 | 生成物 |
|------|--------|
| API 模块 | `${Entity}Api`、`${Entity}Dto`、`${Entity}SelectDto`、`${Entity}Vo` |
| BIZ 模块 | `${Entity}Controller`、`${Entity}Service` / `Impl`、`${Entity}Dao`、`${Entity}Po`、`${Entity}Converter` |
| 资源文件 | `mybatis/mapper/${Entity}Mapper.xml` |
| 配置文件 | `application.yml`、`application-dev.yml` 等（首次生成且不存在时） |

命中租户列时，DAO 还会生成 `@DataIsolation` 及 `*WithoutIsolation` 方法（可通过 `data.isolation.*` 控制）。

---

## 📂 目录结构示例

以项目名 `g2rain-demo`、基础包 `com.g2rain.demo` 为例：

```text
g2rain-demo/
├── codegen.properties                          # 代码生成配置
├── pom.xml                                     # Parent POM
├── g2rain-demo-api/
│   └── src/main/java/com/g2rain/demo/
│       ├── api/                                # REST API 接口
│       ├── dto/                                # 查询 DTO
│       └── vo/                                 # 视图对象
├── g2rain-demo-biz/
│   ├── src/main/java/com/g2rain/demo/
│   │   ├── controller/
│   │   ├── service/impl/
│   │   ├── dao/po/
│   │   └── converter/
│   └── src/main/resources/mybatis/mapper/      # Mapper XML
└── g2rain-demo-startup/
    └── src/main/java/com/g2rain/demo/
        ├── Application.java
        └── config/
            ├── VirtualThreadConfigurer.java
            └── ArgumentResolverConfig.java
```

---

## 🧪 测试与验证

建议按以下步骤验证生成结果：

1. **准备数据库**
   - 使用 MySQL 8.0+，创建与 `database.url` 对应的数据库
   - 准备测试表（含主键；若测数据隔离，表需含 `organ_id` 等租户列）

2. **执行生成**
   - skeleton：确认根目录与子模块 POM、启动类、`codegen.properties` 已生成
   - foundry：确认目标表对应的 DTO / VO / DAO / Service / Controller / API / Mapper XML 均已产出

3. **编译与启动**
   ```bash
   mvn clean compile -pl g2rain-demo-startup -am
   mvn spring-boot:run -pl g2rain-demo-startup
   ```
   确认 Spring Boot 应用能正常启动，无 Bean / Mapper 扫描错误

4. **接口抽检**
   - 访问生成的查询类 API（默认以读为主）
   - 检查分页、条件查询是否与表结构一致

5. **覆盖策略**
   - `tables.overwrite=false`（推荐）：已存在文件跳过，适合生产环境增量生成
   - `tables.overwrite=true`：强制覆盖，适合模板升级后全量刷新

---

## 📄 许可证

本项目基于 [Apache 2.0许可证](LICENSE) 开源。

## 📞 联系我们

* **Issues**: [GitHub Issues](https://github.com/g2rain/g2rain/issues)
* **讨论**: [GitHub Discussions](https://github.com/g2rain/g2rain/discussions)
* **邮箱**: [support@g2rain.com](mailto:g2rain_developer@163.com)
