package com.example.ecommerce.utils;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class AuthenticationUtils {
	
	public boolean checkUsersAuthentication(String email) {
		boolean isSuccess = true;
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		if (authentication == null || !authentication.getName().equals(email)) {
			isSuccess = false;
		}
		return isSuccess;
	}

	public String getEmailFromAuthentication() {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		if (authentication == null || authentication.getName()==null) {
			return null;
		}
		return authentication.getName();
	}
	public List<String> getRoleFromAuthentication() {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		if (authentication == null || authentication.getAuthorities() == null) {
			return null;
		}
		return authentication.getAuthorities().stream()
				.map(GrantedAuthority::getAuthority)
				.collect(Collectors.toList());
	}
}
