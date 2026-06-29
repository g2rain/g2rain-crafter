# g2rain-crafter

[![Maven Central](https://img.shields.io/maven-central/v/com.g2rain/g2rain-crafter.svg)](https://search.maven.org/artifact/com.g2rain/g2rain-crafter)
[![License](https://img.shields.io/badge/license-Apache%202.0-blue.svg)](LICENSE)
[![Java Version](https://img.shields.io/badge/Java-25+-orange.svg)](https://openjdk.java.net/)
[![Build Status](https://img.shields.io/badge/build-Maven-C71A36?logo=apachemaven&logoColor=white)](https://github.com/g2rain/g2rain-crafter)

## 1. 徽标与状态标识
- 当前版本通过 `Maven Central` 发布
- 当前运行时要求 `Java 25+`
- 当前构建方式以 `Maven` 为准
- 当前开源许可证为 `Apache 2.0`

## 2. 项目简介
`g2rain-crafter` 是 G2rain 平台面向 Java 后端项目的 Maven bootstrap 插件，用于快速生成多模块项目骨架，并在需要时继续委托下游生成器完成数据库表到业务代码骨架的输出。它的定位不是单纯 CRUD 生成器，而是“项目初始化 + 生成编排”的工程化工具。

## 3. 平台定位

`g2rain-crafter` 位于 G2rain 平台工程化能力层，是平台 Java 项目从零初始化的重要入口。  
它主要服务于需要快速搭建 `-api / -biz / -startup` 结构的新项目，以及需要在骨架建好后继续增量生成业务代码的团队。  
它负责项目引导与阶段编排，具体的表到代码生成能力由 `g2rain-generator-maven-plugin` 承担。

## 4. 核心能力

- Bootstrap Goal：提供 `bootstrap` 作为统一入口
- 双阶段执行：支持 `skeleton`、`foundry` 与完整流程
- Archetype 骨架生成：自动生成 Parent POM、模块 POM、启动类、基础配置与目录结构
- Foundry 委托生成：复用下游生成器输出 DAO、PO、DTO、VO、Service、Controller、API、Mapper
- 交互与非交互兼容：支持命令行、配置文件与控制台输入
- 参数兼容迁移：支持 `project.basePackage` 并兼容旧的 `archetype.package`

## 5. 技术栈

- 语言与运行时：`Java 25`
- 构建工具：`Maven`
- 构件类型：`maven-plugin`
- 核心依赖：`Maven Plugin API`、`g2rain-generator-maven-plugin`
- 测试框架：`JUnit Jupiter`、`Mockito`
- 发布工具：`maven-plugin-plugin`、`GPG`、`Central Publishing`

## 6. 快速开始
### 环境要求

- `JDK 25`
- `Maven 3.9+`
- 如果执行 `foundry`，需可访问目标数据库

### 直接执行完整流程

```bash
mvn com.g2rain:g2rain-crafter:1.0.7:bootstrap
```

### 仅生成项目骨架

```bash
mvn com.g2rain:g2rain-crafter:1.0.7:bootstrap -Dphase=skeleton
```

### 仅生成业务代码

```bash
mvn com.g2rain:g2rain-crafter:1.0.7:bootstrap -Dphase=foundry -Dconfig.file=./codegen.properties
```

### 常见非交互参数

```bash
mvn com.g2rain:g2rain-crafter:1.0.7:bootstrap \
  -Darchetype.groupId=com.g2rain \
  -Darchetype.artifactId=g2rain-demo \
  -Darchetype.version=1.0.0 \
  -Dproject.basePackage=com.g2rain.demo \
  -Darchetype.description="示例项目"
```

### Foundry 配置补充

当执行 `foundry` 阶段时，可通过 `codegen.properties` 配置：

```properties
project.basePackage=com.g2rain.demo

database.url=jdbc:mysql://localhost:3306/g2rain-demo?useSSL=false&serverTimezone=UTC&allowPublicKeyRetrieval=true
database.driver=com.mysql.cj.jdbc.Driver
database.username=root
database.password=root123456

database.tables=user,product,trade
tables.overwrite=false

data.isolation.withIsolation=true
data.isolation.tenantColumns=organ_id
data.isolation.excludeTables=
```

### 参数优先级

- 命令行参数
- 配置文件
- 交互输入

### 本地构建

```bash
mvn clean install
```

### 本地测试

```bash
mvn test
```

### 发布说明

- 正式版通过 `release.yml` 发布
- 发布流程包含 source、javadoc、GPG 签名与 Central Publishing

## 7. 项目结构

```text
g2rain-crafter/
├── src/main/java/com/g2rain/crafter/
│   ├── config/
│   ├── generator/
│   └── utils/
├── src/main/resources/archetype/
├── src/test/java/com/g2rain/crafter/
├── .github/workflows/
│   └── release.yml
└── pom.xml
```

### 核心能力结构说明

#### 1. `BootstrapMojo`：双阶段编排入口
- 解决问题：把项目骨架初始化和业务代码生成统一收敛到一个 Maven Goal 中
- 核心逻辑：
  - 根据 `phase` 决定执行 `skeleton`、`foundry` 或完整流程
  - 负责命令行、配置文件、交互输入的参数收集
  - 组装 `SkeletonConfig` 与 `FoundryConfig`
- 典型用法：新项目初始化时直接跑完整流程；已有项目则只跑 `foundry`

#### 2. `SkeletonGenerator`：archetype 骨架模板渲染器
- 解决问题：快速生成符合平台规范的多模块空项目，避免人工搭目录和基础 POM
- 核心逻辑：
  - 遍历 `src/main/resources/archetype`
  - 将模板项目名 `g2rain-example` 替换为真实项目名
  - 将 `basePackage` 映射为 Java 目录结构
  - 渲染 `.ftl` 并复制普通文件
- 典型用法：生成 Parent POM、`-api` / `-biz` / `-startup` 模块、启动类和 `codegen.properties`

#### 3. `SkeletonConfig`：骨架数据模型
- 解决问题：把骨架模板渲染所需的项目信息统一组织起来
- 核心逻辑：
  - 保存 `groupId`、`projectName`、`version`、`basePackage`、`description`
  - 以 `toData()` 形式供模板直接使用
- 典型用法：作为骨架模板的数据上下文，而不是在模板里拼接零散变量

#### 4. `archetype/`：项目模板资产目录
- 解决问题：把项目初始化规则固化成可维护模板，而不是硬编码在 Java 中
- 核心逻辑：
  - 提供根 `pom.xml.ftl`
  - 提供模块 `pom.xml.ftl`
  - 提供 `Application.java.ftl`、配置类模板、应用配置模板和 `codegen.properties.ftl`
- 典型用法：调整项目初始化标准时，优先修改这里的模板资产

#### 5. 与 `g2rain-generator-maven-plugin` 的协作边界
- `g2rain-crafter` 负责项目初始化与阶段编排
- `g2rain-generator-maven-plugin` 负责数据库表到业务代码生成
- 二者关系是“上游引导器 + 下游生成器”，而不是重复实现

### 接入建议与边界
- 从零搭项目时优先使用完整流程
- 已有项目需要按表增量生成时，只执行 `foundry`
- 模板资产变化后，应同步检查生成结果是否仍符合团队规范

## 8. 常用命令

```bash
mvn clean install
mvn test
mvn com.g2rain:g2rain-crafter:1.0.7:help
mvn com.g2rain:g2rain-crafter:1.0.7:bootstrap -Dphase=skeleton
mvn com.g2rain:g2rain-crafter:1.0.7:bootstrap -Dphase=foundry -Dconfig.file=./codegen.properties
```

## 9. 质量与测试
- 当前扫描到主源码文件 `4` 个，测试文件 `7` 个，archetype 文件 `17` 个
- 已覆盖 bootstrap 配置解析、skeleton 生成器与 `codegen.properties` 渲染测试
- `foundry` 具体表到代码逻辑主要由下游 `g2rain-generator-maven-plugin` 提供
- 后续如调整 archetype 模板，建议同步补充模板输出相关测试

## 10. 相关仓库

- `g2rain-generator-maven-plugin`
- `g2rain-common`
- `g2rain-spring-boot-starter`
- `g2rain-app-cli`
- `g2rain-app-template`

## 11. 使用建议

- 适合作为平台 Java 新项目初始化的首选工具
- 适合把项目结构标准化与业务代码生成分阶段执行
- 使用前建议先明确项目名、基础包和数据库表范围
- 如仅想生成业务代码，不必重新执行骨架阶段

## 12. 贡献指南

欢迎通过文档改进、Issue 反馈、测试补充、模板优化、功能增强等形式参与贡献。  
建议流程：
1. Fork 本仓库
2. 创建特性分支
3. 提交修改
4. 推送分支
5. 提交 Pull Request

提交前请尽量确保：
- 遵循现有技术栈与代码规范
- 更新相关文档
- 补充必要测试

## 13. 许可证

本项目基于 [Apache 2.0许可证](LICENSE) 开源。

## 14. 联系我们

- **站点**: https://www.g2rain.com/
- **Issues**: [GitHub Issues](https://github.com/g2rain/g2rain/issues)
- **讨论**: [GitHub Discussions](https://github.com/g2rain/g2rain/discussions)
- **邮箱**: g2rain_developer@163.com

## 15. 致谢

感谢所有为这个项目做出贡献的开发者们。  
如果这个项目对您有帮助，欢迎 Star 支持。
