package com.example.ecommerce.utils;

import com.example.ecommerce.entities.Roles;
import com.example.ecommerce.entities.Users;
import com.example.ecommerce.entities.enums.ErrorType;
import com.example.ecommerce.exceptions.users.UnauthorizedException;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.security.Principal;
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

	public void globalCheckOwnerObjectItemByUserId(Long ownerObjectId, Roles role){
		if(!ownerObjectId.equals(getUserIdFromAuthentication()) || !getRoleFromAuthentication().contains(role) || getRoleFromAuthentication().isEmpty()){
			throw new UnauthorizedException(ErrorType.U_DONT_HAVE_PERRMISSION_TO_ACCESS_THIS_RESOURCE.toString());
		}
	}
	public Long getUserIdFromAuthentication(){
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		if(authentication == null || !authentication.isAuthenticated() || authentication instanceof AnonymousAuthenticationToken){
			return null;
		}
		Object principal = authentication.getPrincipal();
		if(principal instanceof Users user){
			return user.getId();
		}
		return null;
	}
}
