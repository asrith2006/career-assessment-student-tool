package com.careers.assessment.config;

import com.careers.assessment.security.JwtAuthenticationFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.config.Customizer;
import org.springframework.security.web.servlet.util.matcher.MvcRequestMatcher;
import org.springframework.web.servlet.handler.HandlerMappingIntrospector;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {

    @Autowired
    private HandlerMappingIntrospector introspector;

    @Autowired
    private JwtAuthenticationFilter jwtAuthenticationFilter;

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig) throws Exception {
        return authConfig.getAuthenticationManager();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        MvcRequestMatcher.Builder mvcMatcherBuilder = new MvcRequestMatcher.Builder(introspector);
        
        http
            .cors(Customizer.withDefaults())
            .csrf(csrf -> csrf.disable())
            .exceptionHandling(exception -> exception
                .authenticationEntryPoint((request, response, authException) -> {
                    response.sendError(401, "Unauthorized");
                }))
            .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .authorizeHttpRequests(authz -> authz
                .requestMatchers(mvcMatcherBuilder.pattern("/auth/**")).permitAll()
                .requestMatchers(mvcMatcherBuilder.pattern("/careers/public/**")).permitAll()
                .requestMatchers(mvcMatcherBuilder.pattern("/questions/public/**")).permitAll()
                .requestMatchers(mvcMatcherBuilder.pattern("/tests/submit")).hasRole("STUDENT")
                .requestMatchers(mvcMatcherBuilder.pattern("/tests/my-results")).hasRole("STUDENT")
                .requestMatchers(mvcMatcherBuilder.pattern("/tests/result/**")).hasAnyRole("STUDENT", "ADMIN")
                .requestMatchers(mvcMatcherBuilder.pattern("/admin/**")).hasRole("ADMIN")
                .requestMatchers(mvcMatcherBuilder.pattern("/students/**")).hasRole("STUDENT")
                .anyRequest().authenticated()
            );

        http.addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

}
