package com.fraudDetection.FraudGuard.auth;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.fraudDetection.FraudGuard.services.UserDetailsServiceImpl;

@Configuration
@EnableWebSecurity
public class SecurityConfig {


    private  final UserDetailsServiceImpl userDetailsService;

    private  final JwtAuthFilter jwtAuthFilter;

    @Autowired
    public SecurityConfig(@Lazy UserDetailsServiceImpl userDetailsService, JwtAuthFilter jwtAuthFilter){
        this.userDetailsService = userDetailsService;
        this.jwtAuthFilter = jwtAuthFilter;
    }


    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                .cors(Customizer.withDefaults())
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/auth/v1/login", "/auth/v1/signup").permitAll()
                        .requestMatchers("/ws/**").permitAll()

                        .requestMatchers(HttpMethod.POST, "/api/v1/transactions").hasAnyRole("USER", "ADMIN")     
                        .requestMatchers(HttpMethod.GET, "/api/v1/transactions/**").hasAnyRole("ADMIN")
                        .requestMatchers(HttpMethod.GET, "/api/v1/transactions/sender/**").hasAnyRole("USER", "ADMIN")
                        
                        .requestMatchers(HttpMethod.GET, "/api/v1/reports/**").hasRole( "ADMIN")
                        .requestMatchers(HttpMethod.GET,"/api/v1/transactions/receiver/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.GET,"/api/v1/alerts/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.GET, "/api/v1/transactions/history/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.POST, "/api/v1/reports").hasRole("ADMIN")
                        .anyRequest().authenticated()
                )
                .sessionManagement(sess -> sess.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authenticationProvider(authenticationProvider())
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public AuthenticationProvider authenticationProvider(){
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setUserDetailsService(userDetailsService);
        provider.setPasswordEncoder(passwordEncoder());
        return provider;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

}
