package com.epam.esm.service.security.jwt;

import com.epam.esm.service.dto.role.Role;
import com.epam.esm.service.dto.user.UserDto;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class JwtUserFactory {

    public JwtUser create(UserDto user) {
        return new JwtUser(user.getId(),
                user.getFirstname(),
                user.getLastname(),
                user.getUsername(),
                user.getPassword(),
                convert(List.of(user.getRole())));
    }

    private List<GrantedAuthority> convert(List<Role> roles) {
        return roles.stream()
                .map(role -> new SimpleGrantedAuthority(role.name()))
                .collect(Collectors.toList());
    }

}
