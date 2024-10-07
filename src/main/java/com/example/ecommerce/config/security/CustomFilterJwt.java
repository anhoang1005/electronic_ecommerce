package com.example.ecommerce.config.security;

import com.example.ecommerce.entities.RoleEntity;
import com.example.ecommerce.entities.UsersEntity;
import com.example.ecommerce.payload.ResponseBody;
import com.example.ecommerce.repository.UsersRepository;
import com.example.ecommerce.utils.JwtTokenUtils;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Component
public class CustomFilterJwt extends OncePerRequestFilter {
	private final JwtTokenUtils jwtUtils;
	private final UsersRepository userRepository;

	public CustomFilterJwt(JwtTokenUtils jwtUtils, UsersRepository userRepository) {
		this.jwtUtils = jwtUtils;
		this.userRepository = userRepository;
	}

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
				UsersEntity usersEntity = userRepository.findUsersEntitiesByEmail(username);
				if (usersEntity!=null) {
					List<RoleEntity> listRoleEntity = usersEntity.getRoles();
					List<SimpleGrantedAuthority> authorities = new ArrayList<>();
					for (RoleEntity roleEntity : listRoleEntity) {
						authorities.add(new SimpleGrantedAuthority("ROLE_" + roleEntity.getRoleName()));
					}
					Authentication authentication = new UsernamePasswordAuthenticationToken(
							username, 
							usersEntity.getPasswordHash(),
							authorities);
					SecurityContext securityContext = SecurityContextHolder.getContext();
					securityContext.setAuthentication(authentication);
					filterChain.doFilter(request, response);
				}
				else{
					filterChain.doFilter(request, response);
					sendErrorResponse(response, "USERS_NOT_EXIsTED");
				}
			} else{
				sendErrorResponse(response, "WRONG_TOKEN");
			}
		} else{
			filterChain.doFilter(request, response);
		}
	}

	private void sendErrorResponse(HttpServletResponse response, String message) throws IOException {
		response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
		response.setContentType("application/json");
		response.setCharacterEncoding("UTF-8");

		ResponseBody responseBody = new ResponseBody("", ResponseBody.Status.FAILED, message, ResponseBody.Code.UNAUTHORIZED_REQUEST);

		ObjectMapper objectMapper = new ObjectMapper();
		response.getWriter().write(objectMapper.writeValueAsString(responseBody));
	}

}
