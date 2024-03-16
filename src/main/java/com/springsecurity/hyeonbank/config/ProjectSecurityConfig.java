package com.springsecurity.hyeonbank.config;

import com.springsecurity.hyeonbank.filter.CsrfCookieFilter;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
import org.springframework.security.web.csrf.CsrfTokenRequestAttributeHandler;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;

import java.util.Collections;

@Configuration
public class ProjectSecurityConfig {

    @Bean
    SecurityFilterChain defaultSecurityFilterChain(HttpSecurity http) throws Exception {
        // CsrfTokenRequestAttributeHandler는 CSRF 토큰을 요청 속성(attribute)으로
        // 추가하여 HTML 폼(form)을 보여줄 때 해당 폼에 CSRF 토큰을 포함시킵니다.
        CsrfTokenRequestAttributeHandler requestHandler = new CsrfTokenRequestAttributeHandler();
        requestHandler.setCsrfRequestAttributeName("_csrf");

        http.securityContext(context -> context
                        // SecurityContext를 변경할 때 명시적으로 저장할 필요가 없도록 설정합니다.
                        // 이렇게 하면 Security 프레임 워크에 작업을 위임해서
                        // SecurityContext가 자동으로 저장합니다.
                        .requireExplicitSave(false)
                ).sessionManagement(session -> session
                        // 항상 새로운 세션을 만들도록 설정합니다.
                        .sessionCreationPolicy(SessionCreationPolicy.ALWAYS)
                ).cors(
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
                .authorizeHttpRequests(request -> request
                        .requestMatchers("/myAccount").hasAuthority("VIEWACCOUNT")
                        .requestMatchers("/myBalance").hasAnyAuthority("VIEWACCOUNT", "VIEWBALANCE")
                        .requestMatchers("/myLoans").hasAuthority("VIEWLOANS")
                        .requestMatchers("/myCards").hasAuthority("VIEWCARDS")
                        .requestMatchers("/user")
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
