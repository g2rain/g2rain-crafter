package ${package};


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * <p>{@code Application} 是 Spring Boot 启动类，用于启动整个应用程序。</p>
 *
 * <p><b>主要功能：</b></p>
 * <ul>
 *     <li>自动启动 Spring Boot 应用上下文</li>
 *     <li>加载并初始化所有配置和组件</li>
 * </ul>
 *
 * <p><b>注解说明：</b></p>
 * <ul>
 *     <li>{@link SpringBootApplication}：组合注解，包含：
 *         <ul>
 *             <li>{@link org.springframework.boot.autoconfigure.SpringBootApplication}：开启自动配置</li>
 *             <li>{@link org.springframework.context.annotation.ComponentScan}：开启组件扫描</li>
 *             <li>{@link org.springframework.context.annotation.Configuration}：声明配置类</li>
 *         </ul>
 *     </li>
 * </ul>
 *
 * <p><b>使用示例：</b></p>
 * <pre>{@code
 * // 启动应用
 * Application.main(new String[]{});
 * }</pre>
 */
@SpringBootApplication
public class Application {

    /**
     * 应用入口方法，启动 Spring Boot 应用。
     *
     * @param args 启动参数
     */
    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
}