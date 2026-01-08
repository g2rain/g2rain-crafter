# g2rain-crafter 使用手册

---

## 📋 介绍

本手册指导如何使用 g2rain-crafter Maven 插件生成项目骨架与业务代码。手册以实际操作步骤为主，包含配置示例、执行命令、IDE操作方法以及生成结果示例。

---

## ⚙️ 配置文件

插件依赖 `codegen.properties` 配置文件，需放置在项目根目录。示例内容：

```properties
# 项目配置
archetype.package=com.g2rain.demo
# 数据库配置
database.url=jdbc:mysql://localhost:3306/g2rain-demo?useSSL=false&serverTimezone=UTC&allowPublicKeyRetrieval=true
database.driver=com.mysql.cj.jdbc.Driver
database.username=root
database.password=root123456
# 待生成表
database.tables=user,product,trade
# 文件覆盖控制
tables.overwrite=false
```

> ⚠️ 必须放置在项目根目录，并确保数据库信息正确。

---

## 💻 安装插件

在项目根 POM 中添加插件：

```xml

<pluginManagement>
    <plugins>
        <plugin>
            <groupId>com.g2rain</groupId>
            <artifactId>g2rain-crafter</artifactId>
            <version>1.0.3</version>
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

---

## 🚀 使用方式

### 1. 交互式输入（缺少参数时命令行提示）

#### 生成业务代码

```bash
# 需要在项目根目录执行, 但是这种方式其实是项目没有安装插件, 直接运行即可令; 如果安装了插件默认会使用插件的配置文件
mvn com.g2rain:g2rain-crafter:1.0.3:bootstrap -Dphase=foundry
```

> ⚠️ 命令必须在项目根目录执行。

### 2. IDE Maven 控制面板执行

* 打开 IDE 的 Maven 项目面板
* 定位到项目根目录
* 找到 g2rain 插件对应目标
* 执行 `bootstrap` 目标，使用 `codegen.properties`

---

## 📂 生成结果示例

```
demo-project/
├── demo-project-api/
├── demo-project-biz/
├── demo-project-startup/
│   └── src/main/java/com/example/demo/config/VirtualThreadConfigurer.java
│   └── src/main/java/com/example/demo/Application.java
├── codegen.properties
├── pom.xml
```

---

## 🧪 测试与验证

* 使用 MySQL 测试表结构生成对应代码
* 确认 DTO / VO / DAO / Service / Controller / API 生成正常
* 验证启动类能正常运行 Spring Boot 应用
