package com.example.ecommerce.config.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig {

	private final CustomFilterJwt customFilterJwt;
	public WebSecurityConfig(CustomFilterJwt customFilterJwt) {
		this.customFilterJwt = customFilterJwt;
	}

	//Ham bean ma hoa mat khau
	@Bean
	PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
	
	@Bean
	SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
		http.cors(cors -> cors.configurationSource(corsConfigurationSource()));
		http.csrf(AbstractHttpConfigurer::disable);
        http.sessionManagement(management -> management.sessionCreationPolicy(SessionCreationPolicy.STATELESS));
        http.addFilterBefore(customFilterJwt, UsernamePasswordAuthenticationFilter.class);
        http.authorizeHttpRequests(requests -> requests
                                .requestMatchers(
                                		"/api/guest/**").permitAll()
								.requestMatchers(
										"/api/root/**").hasRole("ROOT")
                                .requestMatchers(
                                		"/api/admin/**").hasRole("QUANLI")
                                .requestMatchers(
                                		"/api/customer/**").hasRole("KHACHHANG")
								.requestMatchers(
										"/api/owner/**").hasRole("CHUCUAHANG")
								.requestMatchers(
										"/api/staff/**").hasRole("CONGTACVIEN")
								.requestMatchers(
										"/api/all/**")
										.hasAnyRole("ROOT", "CHUCUAHANG", "QUANLI", "KHACHHANG", "CONGTACVIEN")
                                .anyRequest().authenticated());
		return http.build();
	}

	@Bean
	public CorsConfigurationSource corsConfigurationSource() {
		CorsConfiguration configuration = new CorsConfiguration();
		configuration.setAllowedOrigins(List.of("http://127.0.0.1:5500", "http://127.0.0.1:3000"));
		configuration.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "PATCH", "OPTIONS"));
		configuration.setAllowedHeaders(List.of("*"));
		configuration.setAllowCredentials(true);

		UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
		source.registerCorsConfiguration("/**", configuration);
		return source;
	}
	
	@Bean
	UserDetailsService userDetailsService(){
		UserDetails admin = User.withUsername("vananhoang10052002@gmail.com")
				.password(passwordEncoder().encode("test0000"))
				.roles("ROOT").build();
		return new InMemoryUserDetailsManager(admin);
	}
}
