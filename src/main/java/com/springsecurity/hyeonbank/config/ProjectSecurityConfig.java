package com.springsecurity.hyeonbank.config;

import jakarta.servlet.http.HttpServletRequest;
import org.apache.tomcat.util.file.ConfigurationSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;

import java.util.Collections;

@Configuration
public class ProjectSecurityConfig {

    @Bean
    SecurityFilterChain defaultSecurityFilterChain(HttpSecurity http) throws Exception {
        http.cors(
                cors -> cors.configurationSource(
                                new CorsConfigurationSource() {
                                    @Override
                                    public CorsConfiguration getCorsConfiguration(HttpServletRequest request) {
                                         CorsConfiguration config = new CorsConfiguration();
                                         config.setAllowedOrigins(Collections.singletonList("http://localhost:4200"));
                                         config.setAllowedMethods(Collections.singletonList("*"));
                                         config.setAllowCredentials(true);
                                         config.setAllowedHeaders(Collections.singletonList("*"));
                                         config.setMaxAge(3600L);
                                         return config;
                                    }
                                }
                        )
                ).csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(request -> request
                        .requestMatchers("/myAccount", "/myBalance", "/myLoans", "myCards", "/user")
                        .authenticated() // 위 주소들을 로그인 해야 볼 수 있도록 함
                        .requestMatchers("/notices", "/contact", "/register")
                        .permitAll() // 위 두 주소만 로그인 없이 볼 수 있도록 함
                )
                .httpBasic(Customizer.withDefaults())
                .formLogin(Customizer.withDefaults());
        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

}
