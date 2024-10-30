package com.example.ecommerce.security;

import com.example.ecommerce.entities.Users;
import com.example.ecommerce.exceptions.request.RequestNotFoundException;
import com.example.ecommerce.repository.UsersRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.util.ObjectUtils;

import java.util.stream.Collectors;


public class UserDetailsServiceCustom implements UserDetailsService {
    @Autowired
    private UsersRepository usersRepository;
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Users user =  usersRepository.findUsersEntitiesByEmail(username);
        if(ObjectUtils.isEmpty(user)){
            throw new RequestNotFoundException("user not found");
        }
        UserDetailsCustom userDetailsCustom =  new UserDetailsCustom(
                user.getId(),
                user.getEmail(),
                user.getPasswordHash(),
                user.getListRolesOfUsers().stream().map(
                        r->new SimpleGrantedAuthority(r.getRoleName())
                ).collect(Collectors.toList())
        );
        return userDetailsCustom;
    }
}
