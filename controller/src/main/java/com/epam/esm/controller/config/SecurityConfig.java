package com.epam.esm.controller.config;

import com.epam.esm.controller.filter.JwtFilterConfigurer;
import com.epam.esm.service.dto.role.Role;
import com.epam.esm.service.security.jwt.JwtTokenProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;

@Configuration
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private final JwtTokenProvider jwtTokenProvider;

    @Autowired
    public SecurityConfig(JwtTokenProvider jwtTokenProvider) {
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Override
    protected void configure(HttpSecurity httpSecurity) throws Exception {
        httpSecurity
                .httpBasic().disable()
                .csrf().disable()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .authorizeRequests()
                .antMatchers("/auth").permitAll()
                .antMatchers(HttpMethod.GET, "/tags/**", "/certificates/**", "/users/**", "/users/{userId}/orders/**").hasAnyAuthority(Role.USER.name(), Role.ADMIN.name())
                .antMatchers(HttpMethod.POST, "/users/{userId}/orders/**", "/users/**").hasAnyAuthority(Role.USER.name(), Role.ADMIN.name())
                .anyRequest().hasAuthority(Role.ADMIN.name())
                .and()
                .apply(new JwtFilterConfigurer(jwtTokenProvider));
    }
}
