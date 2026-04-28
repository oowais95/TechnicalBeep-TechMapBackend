package com.technicalbeep.techeventsmap.config;

import com.technicalbeep.techeventsmap.security.JwtAuthenticationFilterPlaceholder;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;

/**
 * Registers the JWT filter placeholder in the servlet chain so ordering and wiring are ready
 * before real authentication is added.
 */
@Configuration
public class SecurityPrepConfig {

    @Bean
    public FilterRegistrationBean<JwtAuthenticationFilterPlaceholder> jwtPlaceholderRegistration(
            JwtAuthenticationFilterPlaceholder filter
    ) {
        FilterRegistrationBean<JwtAuthenticationFilterPlaceholder> bean = new FilterRegistrationBean<>();
        bean.setFilter(filter);
        bean.addUrlPatterns("/*");
        bean.setOrder(Ordered.HIGHEST_PRECEDENCE + 20);
        return bean;
    }
}
