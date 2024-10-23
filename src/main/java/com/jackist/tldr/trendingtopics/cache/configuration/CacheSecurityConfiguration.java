package com.jackist.tldr.trendingtopics.cache.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsByNameServiceWrapper;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationProvider;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationToken;

@Configuration
@EnableWebSecurity
public class CacheSecurityConfiguration {
    private static final String REQUEST_HEADER = "X-MS-CLIENT-PRINCIPAL";
    private static final String CLOUD_ROLE = "Cache.Put";
    private static final String LOCAL_ROLE = "PUT";

    @Bean
    public SecurityFilterChain securityFilterChain(
            HttpSecurity http,
            CacheRequestHeaderAuthenticationFilter cacheAuthFilter,
            AuthenticationManager authenticationManager
    ) throws Exception {
        http.addFilter(cacheAuthFilter)
                .authenticationManager(authenticationManager)
                .authorizeHttpRequests(customizer -> customizer
                        .requestMatchers(HttpMethod.PUT).hasRole(LOCAL_ROLE)
                        .requestMatchers(HttpMethod.GET).permitAll())
                .cors(AbstractHttpConfigurer::disable)
                .csrf(AbstractHttpConfigurer::disable)
                .logout(AbstractHttpConfigurer::disable)
                .formLogin(AbstractHttpConfigurer::disable)
                .rememberMe(AbstractHttpConfigurer::disable)
                .passwordManagement(AbstractHttpConfigurer::disable)
                .httpBasic(AbstractHttpConfigurer::disable)
                .requestCache(AbstractHttpConfigurer::disable)
                .sessionManagement(customizer -> customizer
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS));
        return http.build();
    }

    @Bean
    public UserDetailsService userDetailsService() {
        UserDetails user = User.builder()
                .username(CLOUD_ROLE)
                .password("N/A")
                .roles(LOCAL_ROLE)
                .build();
        return new InMemoryUserDetailsManager(user);
    }

    @Bean
    public CacheRequestHeaderAuthenticationFilter cacheAuthFilter(
            AuthenticationManager authenticationManager
    ) {
        return new CacheRequestHeaderAuthenticationFilter(REQUEST_HEADER, CLOUD_ROLE, authenticationManager);
    }

    @Bean
    public AuthenticationManager authenticationManager(
            PreAuthenticatedAuthenticationProvider preAuthProvider
    ) {
        ProviderManager manager = new ProviderManager(preAuthProvider);
        manager.afterPropertiesSet();
        return manager;
    }

    @Bean
    public PreAuthenticatedAuthenticationProvider preAuthProvider(
            UserDetailsByNameServiceWrapper<PreAuthenticatedAuthenticationToken> userDetailsServiceWrapper
    ) {
        PreAuthenticatedAuthenticationProvider provider = new PreAuthenticatedAuthenticationProvider();
        provider.setPreAuthenticatedUserDetailsService(userDetailsServiceWrapper);
        provider.afterPropertiesSet();
        return provider;
    }

    @Bean
    public UserDetailsByNameServiceWrapper<PreAuthenticatedAuthenticationToken> userDetailsServiceWrapper(
            UserDetailsService userDetailsService
    ) {
        return new UserDetailsByNameServiceWrapper<>(userDetailsService);
    }
}
