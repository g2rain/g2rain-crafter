<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0"
         xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">

    <modelVersion>4.0.0</modelVersion>

    <parent>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-parent</artifactId>
        <version>4.1.1</version>
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
            Boot 4.1.1 基线仍低于部分安全修复线，临时覆盖；Boot/Cloud 抬升后可删。
            - Jackson 3 / Jackson 2：jackson-bom.version、fasterxml.jackson.version
            - Tomcat / Logback / SLF4J：tomcat.version、logback.version、slf4j.version
            - ASM：asm.version（asm-bom）
            - Bouncy Castle：bouncycastle.version（OpenFeign 传递）
            - Kafka 压缩库：zstd-jni / lz4-java / snappy-java（kafka-clients 传递）
        -->
        <jackson-bom.version>3.2.3</jackson-bom.version>
        <fasterxml.jackson.version>2.22.4</fasterxml.jackson.version>
        <tomcat.version>11.0.27</tomcat.version>
        <logback.version>1.6.5</logback.version>
        <slf4j.version>2.0.20</slf4j.version>
        <asm.version>9.11.1</asm.version>
        <bouncycastle.version>1.86</bouncycastle.version>
        <zstd-jni.version>1.5.7-24</zstd-jni.version>
        <lz4-java.version>1.12.0</lz4-java.version>
        <snappy-java.version>1.1.10.12</snappy-java.version>
        <spring.cloud.version>2025.1.3</spring.cloud.version>
        <spring.alibaba.nacos.version>2025.1.0.0</spring.alibaba.nacos.version>
        <mybatis.version>4.1.0</mybatis.version>
        <mysql.version>26.10.0</mysql.version>
        <mapstruct.version>1.6.3</mapstruct.version>
        <g2rain.common.version>1.0.8</g2rain.common.version>
        <g2rain.aegis.core.version>1.0.5</g2rain.aegis.core.version>
        <g2rain.data.redis.version>1.0.5</g2rain.data.redis.version>
        <g2rain-starter-mybatis-extensions.version>1.0.5</g2rain-starter-mybatis-extensions.version>
        <g2rain.starter.spring.doc.version>1.0.5</g2rain.starter.spring.doc.version>
        <g2rain.crafter.version>1.0.8</g2rain.crafter.version>
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
                <groupId>org.ow2.asm</groupId>
                <artifactId>asm-bom</artifactId>
                <version>${"$"}{asm.version}</version>
                <type>pom</type>
                <scope>import</scope>
            </dependency>
            <dependency>
                <groupId>org.bouncycastle</groupId>
                <artifactId>bcprov-jdk18on</artifactId>
                <version>${"$"}{bouncycastle.version}</version>
            </dependency>
            <dependency>
                <groupId>org.bouncycastle</groupId>
                <artifactId>bcpkix-jdk18on</artifactId>
                <version>${"$"}{bouncycastle.version}</version>
            </dependency>
            <dependency>
                <groupId>com.github.luben</groupId>
                <artifactId>zstd-jni</artifactId>
                <version>${"$"}{zstd-jni.version}</version>
            </dependency>
            <dependency>
                <groupId>at.yawk.lz4</groupId>
                <artifactId>lz4-java</artifactId>
                <version>${"$"}{lz4-java.version}</version>
            </dependency>
            <dependency>
                <groupId>org.xerial.snappy</groupId>
                <artifactId>snappy-java</artifactId>
                <version>${"$"}{snappy-java.version}</version>
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
