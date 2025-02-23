package org.medilabo.mspatient.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SpringSecurityConfig {


    private final CustomUserDetailsService userDetailsService;

    public SpringSecurityConfig(CustomUserDetailsService userDetailsService) {
        this.userDetailsService = userDetailsService;
    }

    /**
     * Configures the security filter chain.
     *
     * @param http the HttpSecurity object to configure
     * @return the configured SecurityFilterChain
     * @throws Exception if an error occurs during configuration
     */
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        return http.authorizeHttpRequests(auth -> {
            auth.anyRequest().permitAll();
        }).csrf(AbstractHttpConfigurer::disable).build();
//        return http.authorizeHttpRequests(auth -> {
//                    auth.requestMatchers("/register","/user/registration","/styles/**").permitAll();
//                    auth.requestMatchers("/admin").hasRole("ADMIN");
//                    auth.anyRequest().authenticated();
//                })
//                .formLogin(httpSecurityFormLoginConfigurer -> httpSecurityFormLoginConfigurer
//                        .loginPage("/login")
//                        .usernameParameter("email")
//                        .permitAll()
//                        .defaultSuccessUrl("/add-connection", true))
//                .logout(Customizer.withDefaults())
//                .csrf(AbstractHttpConfigurer::disable)
//                .build();
    }

//    /**
//     * Provides a BCryptPasswordEncoder bean.
//     *
//     * @return a new instance of BCryptPasswordEncoder
//     */
//    @Bean
//    public BCryptPasswordEncoder passwordEncoder() {
//        return new BCryptPasswordEncoder();
//    }
//
//    /**
//     * Configures and provides an AuthenticationManager bean.
//     *
//     * @param http the HttpSecurity object
//     * @param bCryptPasswordEncoder the BCryptPasswordEncoder bean
//     *
//     * @return the configured AuthenticationManager
//     * @throws Exception if an error occurs during configuration
//     */
//    @Bean
//    public AuthenticationManager authenticationManager(HttpSecurity http, BCryptPasswordEncoder bCryptPasswordEncoder) throws Exception {
//        AuthenticationManagerBuilder authenticationManagerBuilder = http.getSharedObject(AuthenticationManagerBuilder.class);
//        authenticationManagerBuilder.userDetailsService(userDetailsService).passwordEncoder(bCryptPasswordEncoder);
//        return authenticationManagerBuilder.build();
//    }
}
