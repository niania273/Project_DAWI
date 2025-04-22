package com.project.project.dawi.service;

import com.project.project.dawi.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private UserService userService;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {

        User foundUser = userService.findByEmail(email);

        if (foundUser != null){
            return org.springframework.security.core.userdetails.User.withUsername(email)
                                                                               .password(foundUser.getPassword())
                                                                               .authorities("ROLE_" + foundUser.getRole().getName())
                                                                               .build();
        }
        return null;
    }
}
