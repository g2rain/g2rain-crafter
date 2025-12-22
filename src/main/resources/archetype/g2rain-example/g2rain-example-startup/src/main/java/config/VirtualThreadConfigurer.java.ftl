package ${package}.config;


<#noparse>
import org.apache.coyote.AbstractProtocol;
import lombok.NonNull;
import org.springframework.boot.tomcat.servlet.TomcatServletWebServerFactory;
import org.springframework.boot.web.server.WebServerFactoryCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.Executors;

/**
 * 配置 Tomcat 使用 Java 21 虚拟线程（Virtual Thread）执行 HTTP 请求。
 *
 * <p>虚拟线程是 Java 21 引入的轻量级线程，可以显著提升高并发场景下的吞吐量。
 * 本配置会为每个 Tomcat Connector 设置独立的虚拟线程执行器，替代传统线程池。</p>
 *
 * <p>注意：
 * <ul>
 *     <li>仅适用于 Java 21 及以上版本。</li>
 *     <li>与 Spring Boot 4.x 和 Tomcat 11.x 兼容。</li>
 * </ul>
 */
@Configuration
public class VirtualThreadConfigurer {

    /**
     * 为 Tomcat Connector 配置虚拟线程执行器。
     *
     * <p>使用 Java 21 虚拟线程处理 HTTP 请求，提高高并发吞吐量。</p>
     *
     * @return 自定义 WebServerFactoryCustomizer，用于设置 Connector 执行器
     */
    @Bean
    public WebServerFactoryCustomizer<@NonNull TomcatServletWebServerFactory> virtualThreadCustomizer() {
        return factory -> factory.addConnectorCustomizers(connector -> {
            if (!(connector.getProtocolHandler() instanceof AbstractProtocol<?> protocol)) {
                return;
            }

            protocol.setExecutor(Executors.newThreadPerTaskExecutor(Thread.ofVirtual().factory()));
        });
    }
}
</#noparse>
