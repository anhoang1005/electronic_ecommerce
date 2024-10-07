package com.example.ecommerce.utils;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

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
}
