<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0"
         xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">

    <modelVersion>4.0.0</modelVersion>

    <parent>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-parent</artifactId>
        <version>4.0.1</version>
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
        <mybatis.version>4.0.0</mybatis.version>
        <pagehelper.version>2.1.1</pagehelper.version>
        <mysql.version>9.5.0</mysql.version>
        <mapstruct.version>1.6.3</mapstruct.version>
        <g2rain.common.version>1.0.2</g2rain.common.version>
        <g2rain.crafter.version>1.0.3</g2rain.crafter.version>
        <flatten.maven.plugin.version>1.7.3</flatten.maven.plugin.version>
        <maven.source.plugin.version>3.3.1</maven.source.plugin.version>
        <maven.compiler.release>21</maven.compiler.release>
        <project.build.sourceEncoding>UTF-8</project.build.sourceEncoding>
    </properties>

    <dependencyManagement>
        <dependencies>
            <dependency>
                <groupId>org.mybatis.spring.boot</groupId>
                <artifactId>mybatis-spring-boot-starter</artifactId>
                <version>${"$"}{mybatis.version}</version>
            </dependency>
            <dependency>
                <groupId>com.github.pagehelper</groupId>
                <artifactId>pagehelper-spring-boot-starter</artifactId>
                <version>${"$"}{pagehelper.version}</version>
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
