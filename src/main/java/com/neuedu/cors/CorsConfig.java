package com.neuedu.cors;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

@Configuration
public class CorsConfig {
    private CorsConfiguration buildConfig() {
        CorsConfiguration corsConfiguration = new CorsConfiguration();
        //添加前端域名，使之可以实行跨域
        corsConfiguration.addAllowedOrigin("http://localhost:8081"); // 1允许任何域名使用
        corsConfiguration.addAllowedHeader("*"); // 2允许任何头跨域
        corsConfiguration.addAllowedMethod("*"); // 3允许任何方法（post、get等）跨域
        corsConfiguration.setAllowCredentials(true);//使客户端可以携带cookie
        return corsConfiguration;
    }

    @Bean
    public CorsFilter corsFilter() {
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", buildConfig()); // 4
        return new CorsFilter(source);
    }
}