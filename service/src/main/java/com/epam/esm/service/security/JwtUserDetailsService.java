package com.epam.esm.service.security;

import com.epam.esm.service.dto.user.UserDto;
import com.epam.esm.service.exception.ServiceException;
import com.epam.esm.service.impl.UserService;
import com.epam.esm.service.security.jwt.JwtUserFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class JwtUserDetailsService implements UserDetailsService {

    private final UserService service;
    private final JwtUserFactory jwtUserFactory;

    @Autowired
    public JwtUserDetailsService(UserService service, JwtUserFactory jwtUserFactory) {
        this.service = service;
        this.jwtUserFactory = jwtUserFactory;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        try {
            UserDto user = service.getByUsername(username);
            return jwtUserFactory.create(user);
        } catch (ServiceException e) {
            e.printStackTrace();
            throw new UsernameNotFoundException("User with provided username not found");
        }
    }
}
