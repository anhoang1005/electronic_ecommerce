package com.example.ecommerce.config.security;

import com.example.ecommerce.config.security.user_detail.CustomUserDetailService;
import com.example.ecommerce.config.security.user_detail.CustomUserDetails;
import com.example.ecommerce.utils.JwtTokenUtils;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Slf4j
@Component
@RequiredArgsConstructor
public class CustomFilterJwt extends OncePerRequestFilter {
	private final JwtTokenUtils jwtUtils;
	private final CustomUserDetailService customUserDetailService;

	//Ham lay ra token tu header trong http request client gui
	public String getTokenFromHeader(HttpServletRequest request) {

		String header = request.getHeader("Authorization");
		String token = null;
		if (StringUtils.hasText(header) && header.startsWith("Bearer ")) {
			token = header.substring(7);
		}
		return token;
	}

	//Ham xet token trong OncePerRequest
	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		String token = getTokenFromHeader(request);
		if (token != null) {
			if (jwtUtils.verifyToken(token)) {
				String username = jwtUtils.extractUsername(token);
				if (username!=null) {
					CustomUserDetails userDetails = customUserDetailService.loadUserByUsername(username);
					Authentication authentication = new UsernamePasswordAuthenticationToken(
							userDetails,
							null,
							userDetails.getAuthorities());
					SecurityContext securityContext = SecurityContextHolder.getContext();
					securityContext.setAuthentication(authentication);
				}
			}
		}
		filterChain.doFilter(request, response);
	}
}
