package com.fokatindia.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.reactive.UrlBasedCorsConfigurationSource;
import org.springframework.web.cors.reactive.CorsWebFilter;

import java.util.Arrays;
import java.util.List;

@Configuration
public class CorsConfig {

    @Bean
    public CorsWebFilter corsWebFilter() {

        CorsConfiguration config = new CorsConfiguration();

        config.setAllowCredentials(true);

        // IMPORTANT FIX: use patterns instead of strict origins
        config.addAllowedOriginPattern("https://admin.fokatindia.com");
        config.addAllowedOriginPattern("https://fokatindia.com");
        config.addAllowedOriginPattern("https://www.fokatindia.com");
        config.addAllowedOriginPattern("https://fokatindia-admin-1d8cf.web.app");
        config.addAllowedOriginPattern("https://fokatindia-user-11e15.web.app");
        config.addAllowedOriginPattern("http://localhost:5173");

        config.setAllowedHeaders(List.of("*"));
        config.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));

        // VERY IMPORTANT for preflight caching
        config.setMaxAge(3600L);

        UrlBasedCorsConfigurationSource source =
                new UrlBasedCorsConfigurationSource();

        source.registerCorsConfiguration("/**", config);

        return new CorsWebFilter(source);
    }
}
//@Configuration
//public class CorsConfig {
//
//    @Bean
//    public CorsWebFilter corsWebFilter() {
//
//        CorsConfiguration config = new CorsConfiguration();
//
//        config.setAllowCredentials(true);
//
//        config.setAllowedOrigins(Arrays.asList(
//                "http://localhost:5173",
//                "https://fokatindia-admin-1d8cf.web.app",
//                "https://fokatindia-user-11e15.web.app",
//                // CUSTOM DOMAINS
//                "https://admin.fokatindia.com",
//                "https://fokatindia.com",
//                "https://www.fokatindia.com"
//        ));
//
//        config.setAllowedHeaders(Arrays.asList("*"));
//
//        config.setAllowedMethods(Arrays.asList(
//                "GET",
//                "POST",
//                "PUT",
//                "DELETE",
//                "OPTIONS"
//        ));
//
//        UrlBasedCorsConfigurationSource source =
//                new UrlBasedCorsConfigurationSource();
//
//        source.registerCorsConfiguration("/**", config);
//
//        return new CorsWebFilter(source);
//    }
//}