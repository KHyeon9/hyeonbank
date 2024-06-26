package com.springsecurity.hyeonbank.config;

import com.springsecurity.hyeonbank.filter.*;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
import org.springframework.security.web.csrf.CsrfTokenRequestAttributeHandler;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;

import java.util.Collections;
import java.util.List;

@Configuration
public class ProjectSecurityConfig {

    @Bean
    SecurityFilterChain defaultSecurityFilterChain(HttpSecurity http) throws Exception {
        // CsrfTokenRequestAttributeHandler는 CSRF 토큰을 요청 속성(attribute)으로
        // 추가하여 HTML 폼(form)을 보여줄 때 해당 폼에 CSRF 토큰을 포함시킵니다.
        CsrfTokenRequestAttributeHandler requestHandler = new CsrfTokenRequestAttributeHandler();
        requestHandler.setCsrfRequestAttributeName("_csrf");
        // jsessionid를 생성하는 것을 멈추기 위해 제거
        // http.securityContext(context -> context
        // SecurityContext를 변경할 때 명시적으로 저장할 필요가 없도록 설정합니다.
        // 이렇게 하면 Security 프레임 워크에 작업을 위임해서
        // SecurityContext가 자동으로 저장합니다.
        // requireExplicitSave(false)
        // ).sessionManagement(session -> session
        // 항상 새로운 세션을 만들도록 설정합니다.
        //      .sessionCreationPolicy(SessionCreationPolicy.ALWAYS)
        // )

        JwtAuthenticationConverter jwtAuthenticationConverter = new JwtAuthenticationConverter();
        jwtAuthenticationConverter.setJwtGrantedAuthoritiesConverter(new KeycloakRoleConverter());

        // jsessionid를 생성하지 않고 stateless로 설정
        http.sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .cors(
                cors -> cors.configurationSource(
                                new CorsConfigurationSource() {
                                    @Override
                                    public CorsConfiguration getCorsConfiguration(HttpServletRequest request) {
                                         CorsConfiguration config = new CorsConfiguration();
                                         config.setAllowedOrigins(Collections.singletonList("http://localhost:4200"));
                                         config.setAllowedMethods(Collections.singletonList("*"));
                                         config.setAllowCredentials(true);
                                         config.setAllowedHeaders(Collections.singletonList("*"));
                                        // 브라우저가 jwt토큰을 받을수 있도록 설정
                                        // csrf의 경우 프레임워크가 제공한 헤더이어서 필요없었지만 jwt는 직접 작성하기 때문에 필요
                                         config.setExposedHeaders(List.of("Authorization"));
                                         config.setMaxAge(3600L);
                                         return config;
                                    }
                                }
                        )
                ).csrf(csrf -> csrf
                        .csrfTokenRequestHandler(requestHandler)
                        .ignoringRequestMatchers("/contact", "/register")
                        // CSRF 토큰을 저장하고 검색하는 데 사용되는 리포지토리를 구성(쿠키에 저장)
                        .csrfTokenRepository(
                                // JavaScript에서도 쿠키에 접근할 수 있도록 HttpOnly속성을 False로 변경
                                CookieCsrfTokenRepository.withHttpOnlyFalse()
                        )
                )
                .addFilterAfter(new CsrfCookieFilter(), BasicAuthenticationFilter.class)
                // KeyCloak을 서버로 했으므로 우리가 jwt 토큰을 생성할 필요가 없음
                // .addFilterBefore(new RequestValidationBeforeFilter(), BasicAuthenticationFilter.class)
                // .addFilterAt(new AuthoritiesLoggingAtFilter(), BasicAuthenticationFilter.class)
                // .addFilterAfter(new AuthoritiesLoggingAfterFilter(), BasicAuthenticationFilter.class)
                // .addFilterAfter(new JWTTokenGeneratorFilter(), BasicAuthenticationFilter.class)
                // .addFilterBefore(new JWTTokenValidatorFilter(), BasicAuthenticationFilter.class)
                .authorizeHttpRequests(request -> request
                        .requestMatchers("/myAccount").hasRole("USER")
                        .requestMatchers("/myBalance").hasAnyRole("USER", "ADMIN")
                        .requestMatchers("/myLoans").authenticated()
                        .requestMatchers("/myCards").hasRole("USER")
                        .requestMatchers("/user")
                        .authenticated() // 위 주소들을 로그인 해야 볼 수 있도록 함
                        .requestMatchers("/notices","/contact","/register")
                        .permitAll() // 위 두 주소만 로그인 없이 볼 수 있도록 함
                )
                .oauth2ResourceServer(server ->
                        server.jwt(jwtConfigurer ->
                                jwtConfigurer.jwtAuthenticationConverter(jwtAuthenticationConverter)
                        )
                );
        return http.build();
    }

    @Bean
    @Deprecated // KeyCloak이 해결
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

}
