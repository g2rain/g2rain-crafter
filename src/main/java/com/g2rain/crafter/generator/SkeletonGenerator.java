package com.g2rain.crafter.generator;


import com.g2rain.crafter.config.SkeletonConfig;
import com.g2rain.crafter.utils.Constants;
import com.g2rain.generator.AbstractGenerator;
import lombok.NonNull;
import org.apache.maven.plugin.logging.Log;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.FileSystem;
import java.nio.file.FileSystems;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.StandardCopyOption;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.Map;
import java.util.Objects;

import static com.g2rain.generator.utils.Constants.JAVA_FILE_DIR;

/**
 * 项目骨架生成器，用于根据 {@link SkeletonConfig} 配置生成完整的 Maven 项目骨架。
 *
 * <p>该类核心功能：</p>
 * <ol>
 *     <li>根据模板根目录 {@code /archetype} 遍历所有文件和目录</li>
 *     <li>支持 IDEA 或文件系统直接读取模板资源</li>
 *     <li>支持从 Jar 包中读取模板资源（例如在打包后的 g2rain-generator.jar 中）</li>
 *     <li>处理 Freemarker 模板文件 (*.ftl)，渲染成最终 Java 类、配置文件等</li>
 *     <li>非模板文件直接复制到目标项目目录</li>
 *     <li>自动替换模板路径中的项目名占位符 ({@code g2rain-example}) 为实际项目名</li>
 *     <li>特殊处理 Application.java.ftl，按 basePackage 生成对应的目录结构</li>
 * </ol>
 *
 * <p>模板目录结构示例：</p>
 * <pre>{@code
 * archetype/
 * ├─ src/main/java/g2rain-example/config/VirtualThreadConfigurer.java.ftl
 * ├─ src/main/java/g2rain-example/Application.java.ftl
 * ├─ src/main/resources/application.yml.ftl
 * └─ pom.xml.ftl
 * }</pre>
 *
 * <p>生成后目录示例（假设项目名 g2rain-demo, basePackage com.g2rain.demo）：</p>
 * <pre>{@code
 * g2rain-demo/
 * ├─ src/main/java/com/g2rain/demo/config/VirtualThreadConfigurer.java
 * ├─ src/main/java/com/g2rain/demo/Application.java
 * ├─ src/main/resources/application.yml
 * └─ pom.xml
 * }</pre>
 *
 * <p><b>使用示例：</b></p>
 * <pre>{@code
 * SkeletonConfig config = new SkeletonConfig(
 *     "com.g2rain",          // groupId
 *     "g2rain-demo",         // projectName
 *     "1.0.0",               // version
 *     "com.g2rain.demo",     // basePackage
 *     "示例项目"              // description
 * );
 * SkeletonGenerator generator = new SkeletonGenerator(log, config);
 * generator.generate(); // 执行骨架生成
 * }</pre>
 *
 * <p>核心流程：</p>
 * <ol>
 *     <li>调用 {@link #generate()} 获取模板资源 URL</li>
 *     <li>根据 URL 协议判断：
 *         <ul>
 *             <li>file：IDEA 或文件系统直接读取，调用 {@link #copyFromFileSystem(Path)}</li>
 *             <li>jar：从 Jar 文件读取，使用 {@link java.nio.file.FileSystem} 挂载路径</li>
 *         </ul>
 *     </li>
 *     <li>遍历目录：
 *         <ul>
 *             <li>创建目标目录（保持模板相对路径）</li>
 *             <li>遍历文件：
 *                 <ul>
 *                     <li>以 .ftl 结尾的文件渲染模板</li>
 *                     <li>其他文件直接复制</li>
 *                 </ul>
 *             </li>
 *         </ul>
 *     </li>
 * </ol>
 *
 * <p>注意事项：</p>
 * <ul>
 *     <li>.keep 文件会被跳过，用于保持空目录结构</li>
 *     <li>模板渲染使用 {@link SkeletonConfig#toData()} 提供的数据模型</li>
 *     <li>生成 Application.java.ftl 时会自动将 basePackage 转换为目录结构</li>
 * </ul>
 *
 * @author alpha
 * @since 2025/10/28
 */
public class SkeletonGenerator extends AbstractGenerator {

    /**
     * 模板资源根目录
     */
    private static final String ARCHETYPE_BASE = "/archetype";

    /**
     * 骨架生成配置
     */
    private final SkeletonConfig skeletonConfig;

    /**
     * 构造函数
     *
     * @param log            日志对象，用于输出生成信息
     * @param skeletonConfig 骨架配置
     */
    public SkeletonGenerator(Log log, SkeletonConfig skeletonConfig) {
        super(log, ARCHETYPE_BASE); // 初始化 AbstractGenerator
        this.skeletonConfig = skeletonConfig;
    }

    /**
     * 执行骨架生成。
     *
     * <p>核心流程：</p>
     * <ol>
     *     <li>获取模板资源 URL</li>
     *     <li>判断 URL 协议：
     *         <ul>
     *             <li>file：调用 {@link #copyFromFileSystem(Path)} 直接从文件系统复制</li>
     *             <li>jar：使用 {@link java.nio.file.FileSystem} 挂载 Jar 并调用 {@link #copyFromFileSystem(Path)}</li>
     *         </ul>
     *     </li>
     *     <li>不支持的协议抛出 {@link IOException}</li>
     * </ol>
     *
     * @throws Exception 模板不存在或文件操作失败时抛出
     */
    @Override
    public void generate() throws Exception {
        URL url = getClass().getResource(ARCHETYPE_BASE);
        if (Objects.isNull(url)) {
            throw new IOException("Template root not found: archetype");
        }

        // IDEA 或文件系统下
        if ("file".equals(url.getProtocol())) {
            copyFromFileSystem(Paths.get(url.toURI()));
            return;
        }

        // 从 Jar 包中读取模板资源 url: jar:file:/.../g2rain-generator-1.0.0.jar!/archetype
        if ("jar".equals(url.getProtocol())) {
            try (FileSystem fs = FileSystems.newFileSystem(url.toURI(), Map.of())) {
                copyFromFileSystem(fs.getPath(ARCHETYPE_BASE));
            }
            return;
        }

        // 不支持的资源协议
        throw new IOException("Unsupported resource protocol: " + url.getProtocol());
    }

    /**
     * 遍历模板目录，将文件复制或渲染到目标项目目录。
     *
     * <p>处理规则：</p>
     * <ul>
     *     <li>目录：直接创建</li>
     *     <li>普通文件：直接复制</li>
     *     <li>Freemarker 模板文件 (*.ftl)：
     *         <ul>
     *             <li>使用 {@link SkeletonConfig#toData()} 渲染</li>
     *             <li>Application.java.ftl 特殊处理：按 basePackage 生成目录结构</li>
     *             <li>其它模板文件按原文件名生成目标文件（去掉 .ftl 后缀）</li>
     *         </ul>
     *     </li>
     *     <li>.keep 文件跳过，用于保持空目录</li>
     * </ul>
     *
     * @param sourceRoot 模板资源根路径
     * @throws IOException IO 异常
     */
    private void copyFromFileSystem(Path sourceRoot) throws IOException {
        String pkgPath = skeletonConfig.getBasePackage().replace('.', '/');
        Files.walkFileTree(sourceRoot, new SimpleFileVisitor<>() {
            @Override
            public @NonNull FileVisitResult preVisitDirectory(@NonNull Path dir, @NonNull BasicFileAttributes attrs) throws IOException {
                // 计算目标路径，替换模板项目名占位符
                Path targetPath = Paths.get(sourceRoot.relativize(dir).toString()
                    .replace(File.separatorChar, '/')
                    .replace(Constants.TEMPLATE_BASE, skeletonConfig.getProjectName())
                    .replaceFirst("(.*" + JAVA_FILE_DIR + "/)(.+)$", "$1" + pkgPath + "/$2")
                );

                // 创建目录
                Files.createDirectories(targetPath);
                return FileVisitResult.CONTINUE;
            }

            @Override
            public @NonNull FileVisitResult visitFile(@NonNull Path file, @NonNull BasicFileAttributes attrs) throws IOException {
                // 跳过 .keep 文件
                if (file.getFileName().toString().equals(".keep")) {
                    return FileVisitResult.CONTINUE;
                }

                // 计算源文件相对路径
                String sourcePath = sourceRoot.relativize(file).toString().replace(File.separatorChar, '/');

                // 替换模板占位符 ${project} 和 ${package}，保证路径正确
                Path targetPath = Paths.get(sourcePath
                    .replace(Constants.TEMPLATE_BASE, skeletonConfig.getProjectName())
                    .replaceFirst("(.*" + JAVA_FILE_DIR + "/)(.+)$", "$1" + pkgPath + "/$2")
                );

                if (file.getFileName().toString().endsWith(".ftl")) {
                    // 渲染模板文件
                    String newFileName = targetPath.getFileName().toString().replaceFirst("\\.ftl$", "");
                    // 渲染模板
                    processTemplate(sourcePath, targetPath.resolveSibling(newFileName), skeletonConfig.toData());
                } else {
                    // 普通文件直接复制
                    Files.copy(file, targetPath, StandardCopyOption.REPLACE_EXISTING);
                }

                return FileVisitResult.CONTINUE;
            }
        });
    }
}
