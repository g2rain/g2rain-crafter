<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0"
         xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">

    <modelVersion>4.0.0</modelVersion>

    <parent>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-parent</artifactId>
        <version>4.1.2</version>
    </parent>

    <groupId>${groupId}</groupId>
    <artifactId>${projectName}</artifactId>
    <version>${"$"}{revision}</version>
    <packaging>pom</packaging>

    <name>${projectName}</name>
    <description>
        ${description}
    </description>

    <modules>
        <module>${projectName}-api</module>
        <module>${projectName}-biz</module>
        <module>${projectName}-startup</module>
    </modules>

    <properties>
        <revision>${version}</revision>
        <!--
            Jackson 3 统一升级至 3.2.4，覆盖 Spring Boot 依赖管理中的旧版本，
            修复传递依赖引入的旧版本问题，并满足安全扫描要求。
            后续 Spring Boot 基线升级后，可根据依赖管理情况评估是否移除。
        -->
        <jackson-bom.version>3.2.4</jackson-bom.version>
        <!--
            SpringDoc 3.2.0 传递依赖引入的 Jackson 2 组件版本高于当前依赖管理版本，
            临时通过 Jackson BOM 统一覆盖至 2.22.4，满足依赖上界检查及安全扫描要求。
            后续升级 SpringDoc 基线后，可根据依赖树评估是否移除。
            - Jackson 2: 2.21 → 2.22.4（fasterxml.jackson.version）
        -->
        <fasterxml.jackson.version>2.22.4</fasterxml.jackson.version>
        <spring.cloud.version>2025.1.4</spring.cloud.version>
        <spring.alibaba.nacos.version>2025.1.0.0</spring.alibaba.nacos.version>
        <mybatis.version>4.1.0</mybatis.version>
        <mysql.version>26.10.0</mysql.version>
        <mapstruct.version>1.6.3</mapstruct.version>
        <g2rain.common.version>1.0.9</g2rain.common.version>
        <g2rain.aegis.core.version>1.0.6</g2rain.aegis.core.version>
        <g2rain.data.redis.version>1.0.6</g2rain.data.redis.version>
        <g2rain-starter-mybatis-extensions.version>1.0.6</g2rain-starter-mybatis-extensions.version>
        <g2rain.starter.spring.doc.version>1.0.6</g2rain.starter.spring.doc.version>
        <g2rain.crafter.version>1.0.9</g2rain.crafter.version>
        <flatten.maven.plugin.version>1.8.0</flatten.maven.plugin.version>
        <jib.maven.plugin.version>3.5.2</jib.maven.plugin.version>
        <maven.source.plugin.version>3.4.0</maven.source.plugin.version>
        <maven.compiler.release>25</maven.compiler.release>
        <project.build.sourceEncoding>UTF-8</project.build.sourceEncoding>
    </properties>

    <dependencyManagement>
        <dependencies>
            <dependency>
                <groupId>org.springframework.cloud</groupId>
                <artifactId>spring-cloud-dependencies</artifactId>
                <version>${"$"}{spring.cloud.version}</version>
                <type>pom</type>
                <scope>import</scope>
            </dependency>
            <dependency>
                <groupId>com.alibaba.cloud</groupId>
                <artifactId>spring-cloud-alibaba-dependencies</artifactId>
                <version>${"$"}{spring.alibaba.nacos.version}</version>
                <type>pom</type>
                <scope>import</scope>
            </dependency>
            <dependency>
                <groupId>com.fasterxml.jackson</groupId>
                <artifactId>jackson-bom</artifactId>
                <version>${"$"}{fasterxml.jackson.version}</version>
                <type>pom</type>
                <scope>import</scope>
            </dependency>
            <dependency>
                <groupId>org.mybatis.spring.boot</groupId>
                <artifactId>mybatis-spring-boot-starter</artifactId>
                <version>${"$"}{mybatis.version}</version>
            </dependency>
            <dependency>
                <groupId>com.mysql</groupId>
                <artifactId>mysql-connector-j</artifactId>
                <version>${"$"}{mysql.version}</version>
            </dependency>
            <dependency>
                <groupId>org.mapstruct</groupId>
                <artifactId>mapstruct</artifactId>
                <version>${"$"}{mapstruct.version}</version>
            </dependency>
            <dependency>
                <groupId>org.mapstruct</groupId>
                <artifactId>mapstruct-processor</artifactId>
                <version>${"$"}{mapstruct.version}</version>
            </dependency>
            <dependency>
                <groupId>com.g2rain</groupId>
                <artifactId>g2rain-common</artifactId>
                <version>${"$"}{g2rain.common.version}</version>
            </dependency>
            <dependency>
                <groupId>com.g2rain</groupId>
                <artifactId>g2rain-starter-aegis-core</artifactId>
                <version>${"$"}{g2rain.aegis.core.version}</version>
                <type>pom</type>
            </dependency>
            <dependency>
                <groupId>com.g2rain</groupId>
                <artifactId>g2rain-starter-data-redis</artifactId>
                <version>${"$"}{g2rain.data.redis.version}</version>
            </dependency>
            <dependency>
                <groupId>com.g2rain</groupId>
                <artifactId>g2rain-starter-mybatis-extensions</artifactId>
                <version>${"$"}{g2rain-starter-mybatis-extensions.version}</version>
            </dependency>
            <dependency>
                <groupId>com.g2rain</groupId>
                <artifactId>g2rain-starter-spring-doc</artifactId>
                <version>${"$"}{g2rain.starter.spring.doc.version}</version>
            </dependency>
        </dependencies>
    </dependencyManagement>

    <dependencies>
        <dependency>
            <groupId>com.g2rain</groupId>
            <artifactId>g2rain-common</artifactId>
        </dependency>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-test</artifactId>
            <scope>test</scope>
        </dependency>
        <dependency>
            <groupId>org.projectlombok</groupId>
            <artifactId>lombok</artifactId>
            <scope>provided</scope>
        </dependency>
    </dependencies>

    <build>
        <pluginManagement>
            <plugins>
                <plugin>
                    <groupId>com.g2rain</groupId>
                    <artifactId>g2rain-crafter</artifactId>
                    <version>${"$"}{g2rain.crafter.version}</version>
                    <configuration>
                        <phase>foundry</phase>
                        <!-- 可选：全局默认配置 -->
                        <configFile>${"$"}{project.basedir}/codegen.properties</configFile>
                    </configuration>
                </plugin>
            </plugins>
        </pluginManagement>

        <plugins>
            <!-- 编译插件，用于编译Java源代码 -->
            <!-- 使用方式: mvn compiler:compile -->
            <plugin>
                <groupId>org.apache.maven.plugins</groupId>
                <artifactId>maven-compiler-plugin</artifactId>
                <configuration>
                    <!-- 指定目标Java版本(如21), 替代传统的<source>和<target>, 确保字节码与指定版本兼容 -->
                    <release>${"$"}{maven.compiler.release}</release>
                    <!-- 指定编码格式 -->
                    <encoding>${"$"}{project.build.sourceEncoding}</encoding>
                    <compilerArgs>
                        <!-- 启用方法参数元数据保留, 支持反射获取参数名(如WebFlux参数绑定) -->
                        <arg>-parameters</arg>
                    </compilerArgs>
                    <!-- 配置注解处理器依赖路径 -->
                    <annotationProcessorPaths>
                        <path>
                            <groupId>org.projectlombok</groupId>
                            <artifactId>lombok</artifactId>
                        </path>
                        <path>
                            <groupId>org.mapstruct</groupId>
                            <artifactId>mapstruct-processor</artifactId>
                            <version>${"$"}{mapstruct.version}</version>
                        </path>
                    </annotationProcessorPaths>
                </configuration>
            </plugin>

            <!-- 生成一个“扁平化”的 POM 文件 -->
            <plugin>
                <groupId>org.codehaus.mojo</groupId>
                <artifactId>flatten-maven-plugin</artifactId>
                <version>${"$"}{flatten.maven.plugin.version}</version>
                <configuration>
                    <updatePomFile>true</updatePomFile>
                    <flattenMode>resolveCiFriendliesOnly</flattenMode>
                </configuration>
                <executions>
                    <execution>
                        <id>flatten</id>
                        <phase>process-resources</phase>
                        <goals>
                            <goal>flatten</goal>
                        </goals>
                    </execution>
                    <execution>
                        <id>flatten.clean</id>
                        <phase>clean</phase>
                        <goals>
                            <goal>clean</goal>
                        </goals>
                    </execution>
                </executions>
            </plugin>

            <!-- G2Rain Crafter 项目构建器插件 - 仅在根目录执行 -->
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
        </plugins>
    </build>
</project>
