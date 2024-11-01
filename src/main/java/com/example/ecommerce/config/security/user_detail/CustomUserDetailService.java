package com.example.ecommerce.config.security.user_detail;

import com.example.ecommerce.entities.Roles;
import com.example.ecommerce.entities.Users;
import com.example.ecommerce.repository.UsersRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class CustomUserDetailService implements UserDetailsService {
    private final UsersRepository usersRepository;

    @Override
    public CustomUserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Users users = usersRepository.findUsersEntitiesByEmail(username);
        if (users == null) {
            throw new UsernameNotFoundException("Người dùng không tồn tại");
        }
        List<SimpleGrantedAuthority> authorities = new ArrayList<>();
        for (Roles roleEntity : users.getListRolesOfUsers()) {
            authorities.add(new SimpleGrantedAuthority("ROLE_" + roleEntity.getRoleName()));
        }
        return CustomUserDetails.builder()
                .id(users.getId())
                .userCode(users.getUserCode())
                .shopCode((users.getShopOfUsers()==null) ? null : users.getShopOfUsers().getShopCode())
                .email(users.getEmail())
                .username(users.getUsername())
                .password(users.getPasswordHash())
                .fullName(users.getFirstName() + " " + users.getLastName())
                .active(users.getActive())
                .enable(users.getEnable())
                .authorities(authorities)
                .build();
    }
}
