package com.jwt.jwtsecuritydemo.security;

 
import com.jwt.jwtsecuritydemo.model.type.PermissionType;
import com.jwt.jwtsecuritydemo.model.type.RoleType;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import lombok.RequiredArgsConstructor;
import org.springframework.web.servlet.HandlerExceptionResolver;

import java.io.IOException;

import static com.jwt.jwtsecuritydemo.model.type.RoleType.*;

@Slf4j
@Configuration
@RequiredArgsConstructor
public class WebSecurityConfig {

    private final JwtAuthFilter jwtAuthFilter;
    private final OAuth2SuccessHandler oAuth2SuccessHandler;
    private final HandlerExceptionResolver handlerExceptionResolver;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        
        http
        .csrf(csrf->csrf.disable())
        .formLogin(form -> form.disable())
        .httpBasic(basic -> basic.disable())
        .headers(headers -> headers.frameOptions(frame -> frame.disable()))
        .sessionManagement(sessionconf -> sessionconf.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
        .authorizeHttpRequests(auth -> auth
            .requestMatchers("/auth/**").permitAll()
//                .requestMatchers(HttpMethod.DELETE, "/api/v1/user").hasAuthority(PermissionType.USER_WRITE.name()) // authority level of access of data
            .requestMatchers("/api/v1/**").hasRole(ADMIN.name()) //fetching data from the RoleType
            .requestMatchers("/h2-console/**").permitAll()
            .anyRequest().authenticated()
        )
        .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class)
        .oauth2Login(oAuth2->oAuth2
                .failureHandler(
                (request, response, exception) -> {
                    log.error("OAuth2 error : " + exception.getMessage());
                    handlerExceptionResolver.resolveException(request, response, null, exception);
                })
                .successHandler(oAuth2SuccessHandler)

        )
                .exceptionHandling(exceptionHandlingConfigurer-> exceptionHandlingConfigurer.accessDeniedHandler(new AccessDeniedHandler() {
                    @Override
                    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException)
                            throws IOException, ServletException {
                        handlerExceptionResolver.resolveException(request, response, null, accessDeniedException);

                    }
                })) ;
        return http.build();
    }
    
}
