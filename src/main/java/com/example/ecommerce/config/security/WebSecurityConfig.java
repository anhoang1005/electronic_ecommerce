package com.example.ecommerce.config.security;

import com.example.ecommerce.config.security.user_detail.CustomUserDetailService;
import com.example.ecommerce.entities.enums.BaseRole;
import com.example.ecommerce.exceptions.security.CustomAccessDeniedHandler;
import com.example.ecommerce.exceptions.security.CustomAuthenticationEntryPoint;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class WebSecurityConfig {
	private final CustomUserDetailService customUserDetailService;
	private final CustomFilterJwt customFilterJwt;
	private final CustomAuthenticationEntryPoint customAuthenticationEntryPoint;
	private final CustomAccessDeniedHandler customAccessDeniedHandler;

	//Ham bean ma hoa mat khau
	@Bean
	PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@Bean
	SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
		http.cors(cors -> cors.configurationSource(corsConfigurationSource()));
		http.csrf(AbstractHttpConfigurer::disable);
		http.userDetailsService(customUserDetailService);
        http.sessionManagement(management -> management.sessionCreationPolicy(SessionCreationPolicy.STATELESS));
		http.exceptionHandling(exceptionHandling ->
				exceptionHandling
						.authenticationEntryPoint(customAuthenticationEntryPoint)
						.accessDeniedHandler(customAccessDeniedHandler)
		);
        http.addFilterBefore(customFilterJwt, UsernamePasswordAuthenticationFilter.class);
        http.authorizeHttpRequests(requests -> requests
                                .requestMatchers(
                                		"/api/guest/**",
										"/api/faker/**",
										"/ws/**",
										"/ws").permitAll()
				.requestMatchers("/api/fake/**").permitAll()
								.requestMatchers(
										"/api/root/**").hasRole(BaseRole.ROOT.toString())
                                .requestMatchers(
                                		"/api/admin/**").hasAnyRole(BaseRole.QUANLY.toString(),BaseRole.ROOT.toString())
                                .requestMatchers(
                                		"/api/customer/**").hasRole(BaseRole.KHACHHANG.toString())
								.requestMatchers(
										"/api/owner/**").hasAnyRole(BaseRole.CHUCUAHANG.toString(),BaseRole.QUANLY.toString())
								.requestMatchers(
										"/api/staff/**").hasRole(BaseRole.CONGTACVIEN.toString())
								.requestMatchers(
										"/api/all/**",
										"/**")
										.hasAnyRole("ROOT", "CHUCUAHANG", "QUANLI", "KHACHHANG", "CONGTACVIEN")
                                .anyRequest().authenticated());
		return http.build();
	}

	@Bean
	public CorsConfigurationSource corsConfigurationSource() {
		CorsConfiguration configuration = new CorsConfiguration();
		configuration.setAllowedOrigins(List.of("http://127.0.0.1:5500", "http://127.0.0.1:3000", "http://localhost:3000/"));
		configuration.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "PATCH", "OPTIONS"));
		configuration.setAllowedHeaders(List.of("*"));
		configuration.setAllowCredentials(true);

		UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
		source.registerCorsConfiguration("/**", configuration);
		return source;
	}
}
