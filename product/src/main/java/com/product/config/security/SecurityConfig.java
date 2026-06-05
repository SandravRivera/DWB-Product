package com.product.config.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.product.config.jwt.JwtAuthFilter;

@Configuration
public class SecurityConfig {

    @Autowired
    private JwtAuthFilter jwtFilter;

    // Cambiar si los roles tienen otro nombre
    String ADMIN = "Administrator", CUSTOMER = "User";
    
    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http, CorsConfig corsConfig) throws Exception {
    
        http.csrf(AbstractHttpConfigurer::disable)
        .authorizeHttpRequests(
                auth -> auth
                .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()
                .requestMatchers("/error", "/swagger-ui/**", "/v3/api-docs/**", "/actuator/info", "/actuator/health").permitAll()
                // El cliente puede ver las categorías activas
				.requestMatchers(HttpMethod.GET, "/category/active").hasAnyAuthority(ADMIN, CUSTOMER)
				.requestMatchers("/category", "/category/**").hasAuthority(ADMIN)
                // El cliente puede ver los detalles de un producto y sus imágenes
				.requestMatchers(HttpMethod.GET, "/product/{id}", "/product/{id}/**").hasAnyAuthority(ADMIN, CUSTOMER)
				.requestMatchers("/product", "/product/**").hasAuthority(ADMIN)
                // Solo el usuario tiene carrito de compras
				.requestMatchers("/cart-item", "/cart-item/**").hasAuthority(CUSTOMER)
                // Solo el usuario puede generar facturas, el administrador puede ver todas
				.requestMatchers(HttpMethod.GET, "/invoice/**").hasAnyAuthority(ADMIN, CUSTOMER)
				.requestMatchers(HttpMethod.POST,"/invoice").hasAuthority(CUSTOMER)
                .anyRequest().authenticated()
                )
        .cors(cors -> cors.configurationSource(corsConfig))
        .httpBasic(Customizer.withDefaults())
        .formLogin(form -> form.disable())
        .sessionManagement(httpSecuritySessionManagementConfigurer -> httpSecuritySessionManagementConfigurer.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
        .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);
            
        return http.build();
    }
}