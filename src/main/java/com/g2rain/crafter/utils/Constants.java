package com.g2rain.crafter.utils;


/**
 * 全局常量定义类，用于统一存放系统中的固定字符串与格式化模板。
 * <p>
 * 本类为工具类，不可实例化。主要提供系统通用常量，如路径格式、日志输出模板、数据库默认配置等。
 * <p>
 * 设计目的：
 * <ul>
 *     <li>集中管理系统中的常量，避免硬编码。</li>
 *     <li>保证代码风格统一与易维护性。</li>
 *     <li>遵循开源项目和企业级项目中常见的常量类规范。</li>
 * </ul>
 *
 * <p><b>示例：</b></p>
 * <pre>{@code
 * String path = MessageFormat.format(Constants.PATH_FORMAT, "com", "g2rain", "demo", "dao");
 * System.out.println(Constants.HORIZONTAL_LINE);
 * System.out.printf(Constants.LOG_FORMAT, "DB Name", Constants.DB_NAME);
 * }</pre>
 *
 * @author alpha
 * @since 2025/10/28
 */
public class Constants {

    /**
     * 私有构造函数，防止实例化。
     * <p>
     * 工具类仅包含静态常量与方法，不应被实例化。
     */
    private Constants() {
        // 禁止实例化
    }

    /**
     * 控制台输出时的分隔线。
     * <p>
     * 用于日志或控制台输出时分隔不同模块内容，提升可读性。
     * <p><b>示例：</b></p>
     * <pre>{@code
     * System.out.println(Constants.HORIZONTAL_LINE);
     * }</pre>
     */
    public static final String HORIZONTAL_LINE = "=========================================";

    /**
     * 日志格式模板。
     * <p>
     * 用于统一日志输出格式，第一个参数左对齐15个字符宽度，第二个参数为内容。
     * <p><b>示例：</b></p>
     * <pre>{@code
     * System.out.printf(Constants.LOG_FORMAT + "%n", "DB_HOST", Constants.DB_HOST);
     * }</pre>
     */
    public static final String LOG_FORMAT = "%-15s: %s";

    /**
     * 项目版本常量。
     * <p>
     * 定义代码生成器工具的当前版本号，遵循语义化版本规范。
     * 此版本号用于：
     * <ul>
     *     <li>生成代码中的版本信息注释</li>
     *     <li>插件自身的版本标识</li>
     *     <li>生成的 POM 文件中的默认版本号</li>
     * </ul>
     *
     * <p><b>版本规范：</b></p>
     * <ul>
     *     <li><b>主版本号</b> - 不兼容的 API 修改</li>
     *     <li><b>次版本号</b> - 向下兼容的功能性新增</li>
     *     <li><b>修订号</b> - 向下兼容的问题修正</li>
     * </ul>
     *
     * <p><b>示例：</b></p>
     * <pre>
     * 1.0.0 - 初始发布版本
     * 1.1.0 - 新增功能，向下兼容
     * 2.0.0 - 重大更新，可能不兼容
     * </pre>
     */
    public static final String PROJECT_VERSION = "1.0.0";

    /**
     * 模板文件的基础目录名称。
     * <p>
     * 用于代码生成或模板文件管理模块的根路径标识。
     */
    public static final String TEMPLATE_BASE = "g2rain-example";
}
