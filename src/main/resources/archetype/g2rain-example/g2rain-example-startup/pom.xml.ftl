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
    <packaging>jar</packaging>

    <name>${projectName}-startup</name>
    <description>
        Startup module for ${projectName}
    </description>

    <dependencies>
        <dependency>
            <groupId>${groupId}</groupId>
            <artifactId>${projectName}-biz</artifactId>
            <version>${"$"}{project.version}</version>
        </dependency>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-web</artifactId>
        </dependency>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-actuator</artifactId>
        </dependency>
        <dependency>
            <groupId>com.g2rain</groupId>
            <artifactId>g2rain-starter-spring-doc</artifactId>
        </dependency>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-devtools</artifactId>
            <scope>runtime</scope>
            <optional>true</optional>
        </dependency>
    </dependencies>

    <build>
        <plugins>
            <plugin>
                <groupId>org.springframework.boot</groupId>
                <artifactId>spring-boot-maven-plugin</artifactId>
                <configuration>
                    <excludes>
                        <exclude>
                            <groupId>org.projectlombok</groupId>
                            <artifactId>lombok</artifactId>
                        </exclude>
                    </excludes>
                </configuration>
            </plugin>
            <plugin>
                <groupId>com.google.cloud.tools</groupId>
                <artifactId>jib-maven-plugin</artifactId>
                <version>${"$"}{jib.maven.plugin.version}</version>
                <configuration>
                    <from>
                        <image>docker://eclipse-temurin:25-jre</image>
                    </from>
                    <to>
                        <image>g2rain/${projectName}:${"$"}{project.version}</image>
                    </to>
                    <container>
                        <mainClass>${package}.Application</mainClass>
                        <ports>
                            <port>8080</port>
                        </ports>
                    </container>
                </configuration>
            </plugin>
        </plugins>
    </build>
</project>
