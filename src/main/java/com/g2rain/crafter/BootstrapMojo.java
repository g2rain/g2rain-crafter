package com.g2rain.crafter;


import com.g2rain.crafter.config.SkeletonConfig;
import com.g2rain.crafter.generator.SkeletonGenerator;
import com.g2rain.crafter.utils.Constants;
import com.g2rain.generator.config.FoundryConfig;
import com.g2rain.generator.generator.FoundryGenerator;
import com.g2rain.generator.utils.Strings;
import lombok.Getter;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.project.MavenProject;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Objects;
import java.util.Properties;
import java.util.Scanner;
import java.util.stream.Stream;

/**
 * Maven 插件入口类：G2Rain 项目构建器（Crafter）。
 * <p>
 * 该插件负责项目的自动化初始化与代码生成，支持两种主要阶段：
 * <ul>
 *     <li><b>skeleton</b> —— 项目骨架生成，包含基础工程结构和依赖管理；</li>
 *     <li><b>foundry</b> —— 业务代码生成，基于数据库表结构生成实体、Mapper、Service 等代码。</li>
 * </ul>
 *
 * <p>执行阶段由参数 {@code -Dphase} 控制：
 * <ul>
 *     <li>不指定：执行完整流程（skeleton + foundry）；</li>
 *     <li>{@code -Dphase=skeleton}：仅生成项目骨架；</li>
 *     <li>{@code -Dphase=foundry}：仅生成业务代码。</li>
 * </ul>
 *
 * <p><b>示例：</b></p>
 * <pre>{@code
 * mvn com.g2rain:code-crafter:bootstrap \
 *     -Darchetype.groupId=com.g2rain \
 *     -Darchetype.artifactId=g2rain-demo \
 *     -Darchetype.version=1.0.0 \
 *     -Darchetype.package=com.g2rain.demo \
 *     -Ddatabase.url=jdbc:mysql://localhost:3306/test \
 *     -Ddatabase.driver=com.mysql.cj.jdbc.Driver \
 *     -Ddatabase.username=root \
 *     -Ddatabase.password=123456 \
 *     -Ddatabase.tables=route_definition
 *     -Dtables.overwrite=false
 * }</pre>
 * <p>
 * 插件执行期间会在控制台打印执行计划与详细配置。
 *
 * @author alpha
 * @since 2025/10/28
 */
@Getter
@Mojo(name = "bootstrap", requiresProject = false)
public class BootstrapMojo extends AbstractMojo {

    /**
     * Maven 对象
     */
    @Parameter(defaultValue = "${project}", readonly = true)
    private MavenProject project;

    /**
     * 执行阶段：
     * <ul>
     *     <li>未指定：执行完整流程（skeleton + foundry）</li>
     *     <li>skeleton：仅生成项目骨架</li>
     *     <li>foundry：仅生成业务代码</li>
     * </ul>
     */
    @Parameter(property = "phase")
    private String phase;

    /**
     * Maven 项目 Group ID（必填）
     */
    @Parameter(property = "archetype.groupId")
    protected String groupId;

    /**
     * Maven 项目 Artifact ID（项目名称，必填）
     */
    @Parameter(property = "archetype.artifactId")
    protected String projectName;

    /**
     * 项目版本号，默认 1.0.0
     */
    @Parameter(property = "archetype.version")
    protected String version;

    /**
     * Java 基础包名（必填）
     */
    @Parameter(property = "archetype.package")
    protected String basePackage;

    /**
     * 项目描述（可选）
     */
    @Parameter(property = "archetype.description")
    protected String description;

    /**
     * 数据库连接 URL
     */
    @Parameter(property = "database.url")
    private String url;

    /**
     * 数据库驱动类
     */
    @Parameter(property = "database.driver")
    private String driver;

    /**
     * 数据库用户名
     */
    @Parameter(property = "database.username")
    private String username;

    /**
     * 数据库密码
     */
    @Parameter(property = "database.password")
    private String password;

    /**
     * 待生成的数据库表名（可多表，以逗号分隔）
     */
    @Parameter(property = "database.tables")
    private String tables;

    /**
     * 待生成的数据库表名是否允许覆盖, 默认是不覆盖
     */
    @Parameter(property = "tables.overwrite")
    private Boolean overwrite;

    /**
     * foundry 配置文件路径
     */
    @Parameter(property = "config.file")
    private File configFile;

    /**
     * 控制台输入扫描器，用于交互式参数输入
     */
    private Scanner scanner;

    /**
     * 获取或初始化输入扫描器。
     *
     * @return {@link Scanner} 实例
     */
    private Scanner getScanner() {
        if (Objects.isNull(scanner)) {
            scanner = new Scanner(System.in);
        }
        return scanner;
    }

    /**
     * 插件执行主逻辑。
     * <p>
     * 负责根据 {@code phase} 参数决定执行阶段，依次进行：
     * <ul>
     *     <li>参数收集与验证；</li>
     *     <li>配置展示；</li>
     *     <li>项目骨架生成；</li>
     *     <li>业务代码生成。</li>
     * </ul>
     *
     * @throws MojoExecutionException 当任意生成过程失败时抛出
     */
    @Override
    @SuppressWarnings("java:S2142")
    public void execute() throws MojoExecutionException {
        if ("foundry".equals(phase) && (Objects.isNull(project) || Objects.isNull(project.getFile()) || !project.getFile().exists())) {
            throw new MojoExecutionException("[ERROR] No valid POM file found in the current directory. Please ensure you are running Maven from the project’s root directory.");
        }

        boolean generateSkeleton = !"foundry".equals(phase);
        boolean generateFoundry = !"skeleton".equals(phase);

        getLog().info(Constants.HORIZONTAL_LINE);
        getLog().info("  G2Rain Crafter - Starting execution");
        getLog().info(Constants.HORIZONTAL_LINE);

        getLog().info("Execution plan:");
        getLog().info("  - Generate skeleton: " + generateSkeleton);
        getLog().info("  - Generate foundry: " + generateFoundry);

        try {
            // 收集所需参数
            if (generateSkeleton) {
                prepareSkeletonConfig();
            }

            if (generateFoundry) {
                prepareFoundryConfig();
            }

            // 显示配置信息
            if (generateSkeleton) {
                getLog().info("====== Project Skeleton Configuration =====");
                getLog().info(String.format(Constants.LOG_FORMAT, "Group ID", groupId));
                getLog().info(String.format(Constants.LOG_FORMAT, "Artifact ID", projectName));
                getLog().info(String.format(Constants.LOG_FORMAT, "Version", version));
                getLog().info(String.format(Constants.LOG_FORMAT, "Base Package", basePackage));
                getLog().info(String.format(Constants.LOG_FORMAT, "Description", Objects.toString(this.description, "")));
                getLog().info(Constants.HORIZONTAL_LINE);
                getLog().info("");
            }

            if (generateFoundry) {
                getLog().info("====== Code Generation Configuration =====");
                if (!generateSkeleton) {
                    getLog().info(String.format(Constants.LOG_FORMAT, "Artifact ID", project.getArtifactId()));
                    getLog().info(String.format(Constants.LOG_FORMAT, "Base Package", basePackage));
                }
                getLog().info(String.format(Constants.LOG_FORMAT, "Database URL", url));
                getLog().info(String.format(Constants.LOG_FORMAT, "Driver Class", driver));
                getLog().info(String.format(Constants.LOG_FORMAT, "Database User", username));
                getLog().info(String.format(Constants.LOG_FORMAT, "Table Names", tables));
                getLog().info(String.format(Constants.LOG_FORMAT, "Overwrite Files", Boolean.TRUE.equals(this.overwrite)));
                getLog().info(Constants.HORIZONTAL_LINE);
                getLog().info("");
            }

            // 执行骨架生成
            if (generateSkeleton) {
                getLog().info(">>> Starting skeleton generation...");
                new SkeletonGenerator(getLog(), new SkeletonConfig(
                        groupId,
                        projectName,
                        Objects.toString(version, Constants.PROJECT_VERSION),
                        basePackage,
                        Objects.toString(description, "")
                )).generate();
                getLog().info(">>> Skeleton generation completed.");
            }

            // 执行业务代码生成
            if (generateFoundry) {
                getLog().info(">>> Starting foundry generation...");
                FoundryConfig config = new FoundryConfig(
                        generateSkeleton ? projectName : project.getArtifactId(),
                        basePackage,
                        url,
                        driver,
                        username,
                        password
                );

                config.setStepIn(!generateSkeleton);
                config.setTables(this.tables);
                config.setOverwrite(Boolean.TRUE.equals(this.getOverwrite()));
                new FoundryGenerator(getLog(), config).generate();
                getLog().info(">>> Foundry generation completed.");
            }

            getLog().info(Constants.HORIZONTAL_LINE);
            getLog().info("  G2Rain Crafter - Execution completed!");
            getLog().info(Constants.HORIZONTAL_LINE);
        } catch (Exception e) {
            getLog().info("  G2Rain Crafter - Execution failed: " + e.getMessage(), e);
            throw new MojoExecutionException("Generation failed", e);
        }
    }

    /**
     * 准备项目骨架生成的配置参数。
     * <p>
     * 根据运行环境自动选择参数获取方式：
     * <ul>
     *     <li><b>非交互式环境</b> - 直接验证必填参数，缺失时抛出异常</li>
     *     <li><b>交互式环境</b> - 通过控制台提示用户输入缺失参数</li>
     * </ul>
     *
     * <p><b>执行流程：</b></p>
     * <ol>
     *     <li>检测运行环境（是否在控制台中）</li>
     *     <li>非交互式：验证必填参数完整性</li>
     *     <li>交互式：引导用户输入缺失参数</li>
     * </ol>
     *
     * @throws MojoExecutionException 当非交互式环境下必填参数缺失时抛出
     */
    private void prepareSkeletonConfig() throws MojoExecutionException {
        if (Objects.isNull(System.console())) {
            // 非交互式需要校验参数
            validateSkeletonConfig();
        } else {
            // 交互式获取参数
            promptForSkeletonParameters();
        }
    }

    /**
     * 准备代码生成阶段的配置参数。
     * <p>
     * 支持多种配置来源，按优先级处理：
     * <ol>
     *     <li><b>配置文件</b> - 优先从 codegen.properties 加载配置</li>
     *     <li><b>非交互式环境</b> - 验证命令行参数完整性</li>
     *     <li><b>交互式环境</b> - 通过控制台提示用户输入</li>
     * </ol>
     *
     * <p><b>执行流程：</b></p>
     * <ol>
     *     <li>尝试加载配置文件（如果配置了 configFile）</li>
     *     <li>如果配置文件加载成功或处于非交互式环境：验证参数完整性</li>
     *     <li>否则：进入交互式参数输入流程</li>
     * </ol>
     *
     * @throws MojoExecutionException 当必填参数缺失时抛出
     * @throws IOException            当配置文件读取失败时抛出
     */
    private void prepareFoundryConfig() throws MojoExecutionException, IOException {
        if (loadFoundryConfigFile() || Objects.isNull(System.console())) {
            // 加载配置文件, 如果设置文件路径, 需要校验参数; 非交互式也校验参数
            validateFoundryConfig();
        } else {
            // 交互式获取参数
            promptForFoundryParameters();
        }
    }

    /**
     * 验证项目骨架生成所需的配置参数。
     * <p>
     * 检查以下必填参数是否已配置：
     * <ul>
     *     <li>Group ID - 项目组织标识符</li>
     *     <li>Artifact ID - 项目名称标识符</li>
     *     <li>Base Package - Java 基础包名</li>
     * </ul>
     *
     * @throws MojoExecutionException 当任何必填参数未配置时抛出，包含具体的错误信息
     */
    private void validateSkeletonConfig() throws MojoExecutionException {
        if (Strings.isBlank(this.groupId)) {
            throw new MojoExecutionException("The groupId has not been configured. Please check the configuration file or command-line parameters");
        }

        if (Strings.isBlank(this.projectName)) {
            throw new MojoExecutionException("The artifactId is not configured. Please check the configuration file or command-line parameters");
        }

        if (Strings.isBlank(this.basePackage)) {
            throw new MojoExecutionException("The base package name is not configured. Please check the configuration file or command-line parameters");
        }
    }

    /**
     * 加载 Foundry 阶段的配置文件。
     * <p>
     * 从指定的配置文件中读取参数值，仅当命令行参数未设置时才使用配置文件中的值。
     * 配置文件的优先级低于命令行参数，高于交互式输入。
     *
     * <p><b>配置文件参数映射：</b></p>
     * <ul>
     *     <li>{@code archetype.artifactId} → {@code projectName}</li>
     *     <li>{@code archetype.package} → {@code basePackage}</li>
     *     <li>{@code database.url} → {@code url}</li>
     *     <li>{@code database.driver} → {@code driver}</li>
     *     <li>{@code database.username} → {@code username}</li>
     *     <li>{@code database.password} → {@code password}</li>
     *     <li>{@code database.tables} → {@code tables}</li>
     *     <li>{@code database.overwrite} → {@code overwrite}</li>
     * </ul>
     *
     * @return {@code true} 如果配置文件存在且成功加载，{@code false} 如果未配置配置文件路径，
     * 如果配置文件不存在返回 {@code true} 但跳过加载
     * @throws IOException 当配置文件读取失败时抛出
     */
    private boolean loadFoundryConfigFile() throws IOException {
        if (Objects.isNull(configFile) || !configFile.exists() || !configFile.isFile()) {
            return false;
        }

        getLog().info("Load config: " + configFile.getAbsolutePath());

        Properties props = new Properties();
        try (FileInputStream fis = new FileInputStream(configFile)) {
            props.load(fis);

            // 从配置文件加载数据库配置（如果未通过参数指定）
            if (Strings.isBlank(this.basePackage)) {
                this.basePackage = props.getProperty("archetype.package");
            }

            if (Strings.isBlank(this.url)) {
                this.url = props.getProperty("database.url");
            }

            if (Strings.isBlank(this.driver)) {
                this.driver = props.getProperty("database.driver");
            }

            if (Strings.isBlank(this.username)) {
                this.username = props.getProperty("database.username");
            }

            if (Strings.isBlank(this.password)) {
                this.password = props.getProperty("database.password");
            }

            if (Strings.isBlank(this.tables)) {
                this.tables = props.getProperty("database.tables");
            }

            if (Objects.isNull(this.overwrite)) {
                String ow = props.getProperty("tables.overwrite");
                this.overwrite = "true".equalsIgnoreCase(ow);
            }

            return true;
        }
    }

    /**
     * 验证代码生成所需的配置参数。
     * <p>
     * 检查以下必填参数是否已配置：
     * <ul>
     *     <li>Base Package - Java 基础包名</li>
     *     <li>Database URL - 数据库连接地址</li>
     *     <li>Database Driver - 数据库驱动类</li>
     *     <li>Database Username - 数据库用户名</li>
     *     <li>Database Tables - 待生成的表名列表</li>
     * </ul>
     *
     * <p><b>注意：</b>数据库密码为可选参数，某些数据库可能不需要密码。</p>
     *
     * @throws MojoExecutionException 当任何必填参数未配置时抛出，包含具体的错误信息
     */
    private void validateFoundryConfig() throws MojoExecutionException {
        if (Strings.isBlank(this.basePackage)) {
            throw new MojoExecutionException("The base package name is not configured. Please check the configuration file or command-line parameters");
        }

        if (Strings.isBlank(this.url)) {
            throw new MojoExecutionException("The database host address has not been configured. Please check the configuration file or command-line parameters");
        }

        if (Strings.isBlank(this.driver)) {
            throw new MojoExecutionException("The database driver is not configured. Please check the configuration file or command-line parameters");
        }

        if (Strings.isBlank(this.username)) {
            throw new MojoExecutionException("The database username has not been configured. Please check the configuration file or command-line parameters");
        }

        if (Strings.isBlank(this.tables)) {
            throw new MojoExecutionException("The database tables has not been configured. Please check the configuration file or command-line parameters");
        }
    }

    /**
     * 控制台交互式收集项目骨架相关参数。
     * 若所有参数已通过命令行传入，则直接使用。
     */
    private void promptForSkeletonParameters() {
        if (Stream.of(groupId, projectName, basePackage).allMatch(Strings::isNotBlank)) {
            this.version = Objects.toString(this.version, Constants.PROJECT_VERSION);
            this.description = Objects.toString(this.description, "");
            return;
        }

        this.groupId = getNonBlankInput(
                "Group ID [required]: ",
                this.groupId
        );
        this.projectName = getNonBlankInput(
                "Artifact ID [required]: ",
                this.projectName
        );
        this.version = getOptionalInput(
                "Version [optional, default 1.0.0]: ",
                this.version,
                Constants.PROJECT_VERSION
        );
        this.basePackage = getNonBlankInput(
                "Base Package [required]: ",
                this.basePackage
        );
        this.description = getOptionalInput(
                "Description [optional]: ",
                this.description,
                ""
        );
    }

    /**
     * 控制台交互式收集数据库及代码生成相关参数。
     * 若参数均已提供，则直接返回。
     */
    private void promptForFoundryParameters() {
        if (Stream.of(basePackage, url, driver, username, password, tables)
                .allMatch(Strings::isNotBlank) && Objects.nonNull(overwrite)) {
            return;
        }

        this.basePackage = getNonBlankInput(
                "Base Package [required]: ",
                this.basePackage
        );
        this.url = getNonBlankInput(
                "Database URL [required]: ",
                this.url
        );
        this.driver = getNonBlankInput(
                "Driver Class [required]: ",
                this.driver
        );
        this.username = getNonBlankInput(
                "Username [required]: ",
                this.username
        );
        this.password = getOptionalInput(
                "Password [optional]: ",
                this.password,
                null
        );
        this.tables = getNonBlankInput(
                "Table Names [required]: ",
                this.tables
        );
        this.overwrite = getBooleanInput(
                "Overwrite existing files? (y/N, default N): ",
                this.overwrite,
                false
        );
    }

    /**
     * 控制台输入：读取非空字符串。
     * <p>若当前值已存在则直接返回；否则提示用户输入直到非空。</p>
     *
     * @param prompt       输入提示
     * @param currentValue 当前值（可为空）
     * @return 用户输入的非空字符串
     */
    @SuppressWarnings("java:S106")
    private String getNonBlankInput(String prompt, String currentValue) {
        if (Strings.isNotBlank(currentValue)) {
            return currentValue;
        }

        String input = null;
        while (Strings.isBlank(input)) {
            System.out.print(prompt);
            System.out.flush();
            input = getScanner().nextLine().trim();
        }

        return input;
    }

    /**
     * 控制台输入：读取可选字符串，若为空则返回默认值。
     *
     * @param prompt       输入提示
     * @param currentValue 当前值（可为空）
     * @param defaultValue 默认值
     * @return 用户输入值或默认值
     */
    @SuppressWarnings("java:S106")
    private String getOptionalInput(String prompt, String currentValue, String defaultValue) {
        if (Strings.isNotBlank(currentValue)) {
            return currentValue;
        }

        System.out.print(prompt);
        System.out.flush();

        String input = getScanner().nextLine().trim();
        return Strings.isBlank(input) ? defaultValue : input;
    }

    /**
     * 控制台输入：读取布尔值，支持多种输入格式。
     * <p>
     * 输入处理规则：
     * <ul>
     *     <li>命令行已设置有效值：直接使用命令行值</li>
     *     <li>空输入（直接回车）：返回默认值</li>
     *     <li>有效输入：返回对应布尔值</li>
     *     <li>无效输入：自动重新提示输入</li>
     * </ul>
     *
     * <p><b>支持的输入格式：</b></p>
     * <ul>
     *     <li>true 值：y, yes, true, 1</li>
     *     <li>false 值：n, no, false, 0</li>
     * </ul>
     *
     * <p><b>示例：</b></p>
     * <pre>{@code
     * // 命令行已设置，直接使用
     * boolean result1 = getBooleanInput("Overwrite? [y/N]: ", true, false); // returns true
     *
     * // 交互式输入
     * // 用户输入 "y" -> returns true
     * // 用户输入 "no" -> returns false
     * // 用户直接回车 -> returns false (默认值)
     * // 用户输入 "abc" -> 重新提示输入
     * }</pre>
     *
     * @param prompt       输入提示信息，如 "Overwrite files? [y/N]: "
     * @param currentValue 命令行传入的当前值，可为 null
     * @param defaultValue 默认值，当输入为空时使用
     * @return 解析后的布尔值
     */
    @SuppressWarnings({"java:S106", "SameParameterValue"})
    private boolean getBooleanInput(String prompt, Boolean currentValue, boolean defaultValue) {
        // 如果命令行设置了有效值，直接使用
        if (Objects.nonNull(currentValue)) {
            return currentValue;
        }

        while (true) {
            System.out.print(prompt);
            System.out.flush();

            String input = getScanner().nextLine().trim().toLowerCase();
            if (input.isEmpty()) {
                return defaultValue;
            }

            switch (input) {
                case "y", "yes", "true", "1" -> {
                    return true;
                }
                case "n", "no", "false", "0" -> {
                    return false;
                }
                default -> {
                    // default 什么都不做，自动继续循环
                }
            }
        }
    }
}
