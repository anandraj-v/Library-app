package com.luv2code.spring_boot_library.config;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import com.luv2code.spring_boot_library.filter.JsoupXssFilter;
import com.luv2code.spring_boot_library.filter.JwtAuthenticationFilter;



@Configuration
public class SecurityConfig {
	
	private JwtAuthenticationFilter jwtFilter;
	private final JsoupXssFilter jsoupXssFilter;
	
	public SecurityConfig(JwtAuthenticationFilter jwtFilter, JsoupXssFilter jsoupXssFilter) {
        this.jwtFilter = jwtFilter;
        this.jsoupXssFilter = jsoupXssFilter;
    }
	
	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http) {
		//"style-src 'self' https://cdn.jsdelivr.net; "  Allows your Bootstrap CSS
			http.headers(headers -> headers
					.contentSecurityPolicy(csp -> csp
							.policyDirectives("default-src 'self': " +
								"script-src 'self': "+
								"object-src 'none': "+
								"connect-src 'self' http://localhost:8081;"
									)))
				.csrf(csrf -> csrf.disable())
				.cors(cors -> cors.configurationSource(corsConfigurationSource()))
				.authorizeHttpRequests(
				requst -> requst.requestMatchers("/me").authenticated()
				.requestMatchers("/register","/login","/api/books","/api/auth/logout").permitAll()
				.anyRequest().authenticated())
				.sessionManagement(session ->
				session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
				.addFilterBefore(jsoupXssFilter, UsernamePasswordAuthenticationFilter.class)
				.addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class)
				.formLogin(c-> c.disable())
				.httpBasic(c-> c.disable());
				
		return http.build();

	}
	@Bean
	public UrlBasedCorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(List.of("http://localhost:3000")); // Your React App
        configuration.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        configuration.setAllowedHeaders(List.of("Authorization", "Content-Type"));
        configuration.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration); // Applies to ALL paths
        return source;
    }
	
	 
}
