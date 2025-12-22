<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0"
         xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">

    <modelVersion>4.0.0</modelVersion>

    <parent>
        <groupId>${groupId}</groupId>
        <artifactId>${projectName}</artifactId>
        <version>${"$"}{revision}</version>
    </parent>

    <artifactId>${projectName}-startup</artifactId>
    <version>1.0.0</version>
    <packaging>jar</packaging>

    <name>${projectName}-startup</name>
    <description>
        Startup module for ${projectName}
    </description>

    <dependencies>
        <!-- 依赖业务模块 -->
        <dependency>
            <groupId>${groupId}</groupId>
            <artifactId>${projectName}-biz</artifactId>
            <version>1.0.0</version>
        </dependency>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-web</artifactId>
        </dependency>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-devtools</artifactId>
            <scope>runtime</scope>
            <optional>true</optional>
        </dependency>
    </dependencies>

    <build>
        <!-- 生成最终包名格式为[artifactId]-[version].jar(如my-app-1.0.0.jar) -->
        <finalName>${"$"}{project.artifactId}-${"$"}{project.version}</finalName>

        <!-- 声明资源处理规则的配置项, 仅定义基础规则(路径、包含/排除文件), 必须显式声明以覆盖默认资源目录 -->
        <resources>
            <!-- 定义文件处理规则 -->
            <resource>
                <!-- 指定资源文件目录(默认src/main/resources) -->
                <directory>src/main/resources</directory>
                <!-- 启用资源过滤, 替换文件中的${"$"}{property}占位符为实际值 -->
                <filtering>true</filtering>
                <!-- 仅处理匹配的文件类型(.yml/.properties/.xml), 其他格式忽略 -->
                <includes>
                    <include>**/*.yml</include>
                    <include>**/*.properties</include>
                    <include>**/*.xml</include>
                </includes>
            </resource>
        </resources>

        <plugins>
            <plugin>
                <groupId>org.springframework.boot</groupId>
                <artifactId>spring-boot-maven-plugin</artifactId>
                <configuration>
                    <!-- 排除Lombok等编译期依赖, 避免打入运行时包(Lombok仅需编译阶段) -->
                    <excludes>
                        <exclude>
                            <groupId>org.projectlombok</groupId>
                            <artifactId>lombok</artifactId>
                        </exclude>
                    </excludes>
                    <!-- 启用分层打包优化(true), 提升Docker镜像构建效率 -->
                    <layers>
                        <enabled>true</enabled>
                    </layers>
                    <!-- 定义Docker镜像名称格式([artifactId]:[version]) -->
                    <image>
                        <name>${"$"}{project.artifactId}:${"$"}{project.version}</name>
                    </image>
                </configuration>
                <executions>
                    <execution>
                        <goals>
                            <!-- 核心目标:将普通JAR转换为可执行JAR(含依赖和启动脚本) -->
                            <goal>repackage</goal>
                            <!-- 生成build-info.properties文件, 记录构建元数据(如时间/版本) -->
                            <goal>build-info</goal>
                        </goals>
                    </execution>
                </executions>
            </plugin>
        </plugins>
    </build>
</project>
