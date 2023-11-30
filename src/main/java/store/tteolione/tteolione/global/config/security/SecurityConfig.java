package store.tteolione.tteolione.global.config.security;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import store.tteolione.tteolione.global.jwt.JwtAccessDeniedHandler;
import store.tteolione.tteolione.global.jwt.JwtAuthenticationEntryPoint;
import store.tteolione.tteolione.global.jwt.JwtSecurityConfig;
import store.tteolione.tteolione.global.jwt.TokenProvider;
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
    public WebSecurityCustomizer webSecurityCustomizer() {
        return (web) -> web.ignoring().antMatchers("/resource/**", "/css/**", "/js/**", "/img/**", "/lib/**");
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf().disable()
                .exceptionHandling()
                .authenticationEntryPoint(jwtAuthenticationEntryPoint)

                .accessDeniedHandler(jwtAccessDeniedHandler)

                .and()
                .headers()
                .frameOptions()
                .sameOrigin()

                .and()
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)

                .and()
                .authorizeRequests()
                .antMatchers("/api/users/kakao").permitAll()
                .antMatchers(HttpMethod.POST,  "/api/email/send/signup").permitAll()
                .antMatchers(HttpMethod.POST,  "/api/email/verify/signup").permitAll()
                .antMatchers(HttpMethod.POST,  "/api/users/signup").permitAll()
                .antMatchers(HttpMethod.POST,  "/api/users/login").permitAll()
                .antMatchers(HttpMethod.POST,  "/api/users/reissue").permitAll()
                .antMatchers(HttpMethod.POST,  "/api/users/check/nickname").permitAll()
                .antMatchers(HttpMethod.POST,  "/api/users/check/login-id").permitAll()
                .antMatchers(HttpMethod.POST,  "/api/users/find/login-id").permitAll()
                .antMatchers(HttpMethod.POST,  "/api/users/verify/login-id").permitAll()


                .antMatchers("/api/items/**").access("hasRole('ROLE_USER')")

                .anyRequest().authenticated()
                .and()
                .apply(new JwtSecurityConfig(tokenProvider))
                .and()
                .oauth2Login()
                .userInfoEndpoint().userService(customOAuth2UserService);

        return http.build();
    }
}
