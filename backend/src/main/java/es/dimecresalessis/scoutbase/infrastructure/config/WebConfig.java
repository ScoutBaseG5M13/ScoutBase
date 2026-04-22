package es.dimecresalessis.scoutbase.infrastructure.config;

import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * Global Web MVC configuration for the application.
 */
@Configuration
@AllArgsConstructor
public class WebConfig implements WebMvcConfigurer {

    /**
     * The custom interceptor used to manage diagnostic context (MDC).
     */
    private final RequestInterceptor requestInterceptor;

    /**
     * Registers application interceptors into the Spring MVC lifecycle.
     * <p>
     * Adds the {@link RequestInterceptor} to the registry to ensure that
     * tracing identifiers (like Session IDs) are processed for every
     * incoming HTTP request.
     * </p>
     *
     * @param registry The {@link InterceptorRegistry} used to register interceptors.
     */
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(requestInterceptor);
    }

    /**
     * Configures Cross-Origin Resource Sharing (CORS) policies.
     *
     * @param registry The {@link CorsRegistry} used to define mapping rules.
     */
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOrigins("*")
                .allowedMethods("GET", "POST", "PUT", "DELETE");
    }
}