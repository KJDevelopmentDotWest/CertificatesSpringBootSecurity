package com.epam.esm.service.security;

import com.epam.esm.service.dto.user.UserDto;
import com.epam.esm.service.exception.ExceptionCode;
import com.epam.esm.service.exception.ExceptionMessage;
import com.epam.esm.service.exception.ServiceException;
import com.epam.esm.service.impl.UserService;
import com.epam.esm.service.security.jwt.JwtTokenProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    private final AuthenticationManager authenticationManager;

    private final JwtTokenProvider jwtTokenProvider;

    private final UserService service;

    @Autowired
    public AuthService(AuthenticationManager authenticationManager, JwtTokenProvider jwtTokenProvider, UserService service) {
        this.authenticationManager = authenticationManager;
        this.jwtTokenProvider = jwtTokenProvider;
        this.service = service;
    }

    public String authenticate(String username, String password) throws ServiceException {
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
            UserDto userDto = service.getByUsername(username);
            return jwtTokenProvider.createToken(userDto.getUsername());
        } catch (AuthenticationException e){
            throw new ServiceException(ExceptionCode.BAD_CREDENTIALS, ExceptionMessage.WRONG_PASSWORD_OR_USERNAME);
        }
    }

}
