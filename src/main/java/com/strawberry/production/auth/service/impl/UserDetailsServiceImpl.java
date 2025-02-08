package com.strawberry.production.auth.service.impl;

import com.strawberry.production.auth.entity.UserAuth;
import com.strawberry.production.users.repository.UsersRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {
    public final UsersRepository usersRepository;

    public UserDetailsServiceImpl(UsersRepository usersRepository) {
        this.usersRepository = usersRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        var userData = usersRepository.findByUsername(username).orElseThrow(()->new UsernameNotFoundException("Username: "+ username + " not found"));
        return new UserAuth(userData);
    }
}
