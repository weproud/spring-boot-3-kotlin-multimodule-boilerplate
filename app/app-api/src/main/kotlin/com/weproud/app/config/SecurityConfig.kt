package com.weproud.app.config

import com.weproud.app.config.auth.AuthRole
import com.weproud.app.config.auth.CustomUserDetailsService
import com.weproud.app.config.filter.JwtAuthenticationFilter
import org.springframework.boot.autoconfigure.security.servlet.PathRequest
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpMethod
import org.springframework.http.HttpStatus
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.web.SecurityFilterChain
import org.springframework.security.web.authentication.HttpStatusEntryPoint
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter
import org.springframework.security.web.util.matcher.AntPathRequestMatcher
import org.springframework.web.cors.CorsUtils

@Configuration
@EnableWebSecurity
class SecurityConfig(
    private val customUserDetailsService: CustomUserDetailsService,
) {
    @Bean
    @Throws(Exception::class)
    fun securityFilterChain(http: HttpSecurity): SecurityFilterChain {
        http
            .csrf { it.disable() }
            .cors { it.disable() }
            .headers { headers -> headers.frameOptions { it.disable() } }
            .httpBasic { it.disable() }
            .formLogin { it.disable() }
            .sessionManagement { it.sessionCreationPolicy(SessionCreationPolicy.STATELESS) }
            .exceptionHandling {
                it.authenticationEntryPoint(HttpStatusEntryPoint(HttpStatus.UNAUTHORIZED))
            }
            .authorizeHttpRequests {
                it.requestMatchers(PathRequest.toStaticResources().atCommonLocations()).permitAll()
                it.requestMatchers(CorsUtils::isPreFlightRequest).permitAll()
                it.requestMatchers(AntPathRequestMatcher("/docs/**")).permitAll()
                it.requestMatchers(AntPathRequestMatcher("/v3/api-docs/**")).permitAll()
                it.requestMatchers(AntPathRequestMatcher("/actuator/**")).permitAll()
                it.requestMatchers(AntPathRequestMatcher("/api/v1/posts", HttpMethod.GET.name())).permitAll()
                it.requestMatchers(AntPathRequestMatcher("/api/v1/users/**")).hasAnyRole(AuthRole.USER.name)
                it.anyRequest().authenticated()
            }
            .addFilterBefore(
                JwtAuthenticationFilter(
                    userDetailsService = customUserDetailsService,
                ),
                UsernamePasswordAuthenticationFilter::class.java,
            )
        return http.build()
    }
}
