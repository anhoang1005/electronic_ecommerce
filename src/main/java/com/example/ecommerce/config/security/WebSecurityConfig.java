package com.example.ecommerce.config.security;

import com.example.ecommerce.config.security.CustomFilterJwt;
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
		http.csrf(AbstractHttpConfigurer::disable);
        http.sessionManagement(management -> management.sessionCreationPolicy(SessionCreationPolicy.STATELESS));
        http.addFilterBefore(customFilterJwt, UsernamePasswordAuthenticationFilter.class);
        http.authorizeHttpRequests(requests -> requests
                                .requestMatchers(
                                		"/api/guest/**").permitAll()
				                .requestMatchers("/api/fake/**").permitAll()
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
										.hasAnyRole("CHUCUAHANG", "QUANLI", "KHACHHANG", "CONGTACVIEN")
                                .anyRequest().authenticated());
		return http.build();
	}
	
	@Bean
	UserDetailsService userDetailsService(){
		UserDetails admin = User.withUsername("vananhoang10052002@gmail.com")
				.password(passwordEncoder().encode("test0000"))
				.roles("ROOT").build();
		return new InMemoryUserDetailsManager(admin);
	}
}
