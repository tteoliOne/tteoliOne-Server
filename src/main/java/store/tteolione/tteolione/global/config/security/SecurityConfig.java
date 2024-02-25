package store.tteolione.tteolione.global.config.security;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import store.tteolione.tteolione.global.jwt.*;
import store.tteolione.tteolione.global.service.CustomOAuth2UserService;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final TokenProvider tokenProvider;
    private final JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;
    private final JwtAccessDeniedHandler jwtAccessDeniedHandler;
    private final CustomOAuth2UserService customOAuth2UserService;


    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }


    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf((csrf) -> csrf.disable())
                .exceptionHandling((exceptionHandling) ->
                        exceptionHandling.
                                authenticationEntryPoint(jwtAuthenticationEntryPoint)
                                .accessDeniedHandler(jwtAccessDeniedHandler))
                .headers(
                        headers ->
                                headers.frameOptions(
                                        frameOptions ->
                                                frameOptions.sameOrigin()
                                )
                )
                .sessionManagement(
                        sessionManagement ->
                                sessionManagement.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )

                .authorizeRequests(
                        auth ->
                                auth
                                        .requestMatchers("/chat/**").permitAll()
                                        .requestMatchers("/ws-stomp/**").permitAll()
                                        .requestMatchers("/api/users/kakao").permitAll()
                                        .requestMatchers("/api/users/kakao/profile").permitAll()
                                        .requestMatchers(HttpMethod.POST, "/api/email/send/signup").permitAll()
                                        .requestMatchers(HttpMethod.POST, "/api/email/verify/signup").permitAll()
                                        .requestMatchers(HttpMethod.POST, "/api/users/signup").permitAll()
                                        .requestMatchers(HttpMethod.POST, "/api/users/login").permitAll()
                                        .requestMatchers(HttpMethod.POST, "/api/users/reissue").permitAll()
                                        .requestMatchers(HttpMethod.POST, "/api/users/check/nickname").permitAll()
                                        .requestMatchers(HttpMethod.POST, "/api/users/check/login-id").permitAll()
                                        .requestMatchers(HttpMethod.POST, "/api/users/find/login-id").permitAll()
                                        .requestMatchers(HttpMethod.POST, "/api/users/verify/login-id").permitAll()
                                        .requestMatchers(HttpMethod.POST, "/api/users/find/password").permitAll()
                                        .requestMatchers(HttpMethod.POST, "/api/users/verify/password").permitAll()
                                        .requestMatchers(HttpMethod.PATCH, "/api/users/reset/password").permitAll()
                                        .requestMatchers("/api/items/**").access("hasRole('ROLE_USER')")
                                        .anyRequest().authenticated()

                )
                .addFilterBefore(new JwtFilter(tokenProvider), UsernamePasswordAuthenticationFilter.class)
                .oauth2Login(
                        oauth ->
                                oauth.userInfoEndpoint(
                                        userInfo ->
                                                userInfo.userService(customOAuth2UserService)
                                )
                );

        return http.build();
    }

    @Bean
        // CORS 설정
    CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.addAllowedOriginPattern("*");
        configuration.addAllowedMethod("*"); // 모든 HTTP 메서드 허용
        configuration.addAllowedHeader("*"); // 모든 헤더 허용
        configuration.setAllowCredentials(true); // 자격 증명 허용 설정

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration); // 모든 경로에 대해 CORS 구성 적용
        return source;
    }
}
