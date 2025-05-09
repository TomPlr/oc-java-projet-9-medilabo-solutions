package org.medilabo.msnotes.configuration;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.converter.Converter;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.web.SecurityFilterChain;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SpringSecurityConfig {

    private static final String CLIENT_NAME = "medilabo-auth";

    /**
     * Configures the security filter chain.
     *
     * @param http the HttpSecurity object to configure
     * @return the configured SecurityFilterChain
     * @throws Exception if an error occurs during configuration
     */
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(authorize -> authorize
                        .anyRequest().authenticated()
                )
                .oauth2ResourceServer((oauth2) -> oauth2.jwt(Customizer.withDefaults()));
        return http.build();
    }

    /**
     * Creates a customized {@link JwtAuthenticationConverter} for Keycloak integration.
     *
     * <p>Overrides the default behavior by configuring a custom {@link Converter} to extract roles
     * from the "resource_access" claim in a JWT token, specifically for the "medilabo-auth" client.
     * Maps these roles to Spring Security's {@link SimpleGrantedAuthority} to enable proper
     * authorization logic.
     *
     * <p><b>Default Behavior:</b> The default {@link JwtAuthenticationConverter} extracts
     * authorities from the "scope" claim in a flat structure. Keycloak, however, nests roles under
     * "resource_access", making it incompatible.
     *
     * <p><b>Customized Behavior:</b> This implementation extracts roles from the nested structure
     * in the JWT, ensuring Keycloak roles are correctly identified and mapped for Spring Security.
     *
     * @return a configured {@link JwtAuthenticationConverter} instance
     */
    @Bean
    public JwtAuthenticationConverter jwtAuthenticationConverterForKeycloak() {
        JwtAuthenticationConverter jwtAuthenticationConverter = new JwtAuthenticationConverter();
        jwtAuthenticationConverter.setJwtGrantedAuthoritiesConverter(jwtGrantedAuthoritiesConverter());
        return jwtAuthenticationConverter;
    }

    private Converter<Jwt, Collection<GrantedAuthority>> jwtGrantedAuthoritiesConverter() {
        return jwt -> Optional.ofNullable(jwt.getClaimAsMap("resource_access"))
                .flatMap(resourceAccess -> Optional.ofNullable((Map<?, ?>) resourceAccess.get(CLIENT_NAME)))
                .map(clientMap -> (Map<?, ?>) clientMap)
                .map(clientMap -> (List<String>) clientMap.get("roles"))
                .stream()
                .flatMap(Collection::stream)
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toUnmodifiableList()); // Ensure immutability
    }
}