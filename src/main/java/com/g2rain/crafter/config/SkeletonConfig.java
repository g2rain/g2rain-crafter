package com.g2rain.crafter.config;


import com.g2rain.generator.config.GeneratorConfig;
import lombok.Getter;
import lombok.Setter;

import java.util.Map;

/**
 * 项目骨架配置类，用于保存 Maven 项目骨架生成器（Skeleton Generator）所需的参数。
 *
 * <p>该类继承自 {@link GeneratorConfig}，除了基础的项目名和包名，还包含 Group ID、
 * 版本号和项目描述，用于生成 Maven 项目骨架的 POM 文件及基础目录结构。</p>
 *
 * <p><b>示例：</b></p>
 * <pre>{@code
 * SkeletonConfig config = new SkeletonConfig(
 *     "com.g2rain",             // Group ID
 *     "g2rain-demo",            // 项目名
 *     "1.0.0",                  // 版本号
 *     "com.g2rain.demo",        // 基础包名
 *     "示例项目"                 // 项目描述
 * );
 *
 * Map<String, Object> data = config.toData();
 * // data 包含：
 * // "groupId" -> "com.g2rain"
 * // "projectName" -> "g2rain-demo"
 * // "version" -> "1.0.0"
 * // "description" -> "示例项目"
 * // "package" -> "com.g2rain.demo"
 * }</pre>
 *
 * @author alpha
 * @since 2025/10/28
 */
@Setter
@Getter
public class SkeletonConfig extends GeneratorConfig {

    /**
     * Maven 项目的 Group ID，例如 "com.g2rain"
     */
    private String groupId;

    /**
     * 项目的版本号，例如 "1.0.0"
     */
    private String version;

    /**
     * 项目描述，用于生成 POM 文件中的描述信息
     */
    private String description;

    /**
     * 构造函数，初始化骨架生成所需的所有基本信息。
     *
     * @param groupId     Maven Group ID
     * @param projectName 项目名称
     * @param version     项目版本
     * @param basePackage 基础包名
     * @param description 项目描述
     */
    public SkeletonConfig(String groupId, String projectName, String version, String basePackage, String description) {
        super(projectName, basePackage); // 初始化 GeneratorConfig 中的项目名和基础包名
        this.groupId = groupId;
        this.version = version;
        this.description = description;
    }

    /**
     * 将当前骨架配置转换为数据模型 Map，用于模板渲染。
     *
     * <p>返回的 Map 可直接用于 Freemarker 模板生成 POM 文件、README 或其他配置文件。</p>
     *
     * <p>Map 的 key 包含：</p>
     * <ul>
     *     <li>"groupId" - Maven Group ID</li>
     *     <li>"projectName" - 项目名称</li>
     *     <li>"version" - 项目版本</li>
     *     <li>"description" - 项目描述</li>
     *     <li>"package" - 基础包名</li>
     * </ul>
     *
     * @return 包含骨架配置数据的 Map
     */
    public Map<String, Object> toData() {
        return Map.of(
                "groupId", this.getGroupId(),
                "projectName", this.getProjectName(),
                "version", this.getVersion(),
                "description", this.getDescription(),
                "package", this.getBasePackage()
        );
    }
}
