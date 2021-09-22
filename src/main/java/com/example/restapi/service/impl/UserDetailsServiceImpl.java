package com.example.restapi.service.impl;

import com.example.restapi.domain.CustomUser;
import com.example.restapi.repository.UserRepository;
import com.example.restapi.security.UserPrincipal;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserRepository userRepository;

    public UserDetailsServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
//        Optional<CustomUser> userOptional = userRepository.findByEmail(email);
//        CustomUser user = userOptional
//                .orElseThrow(() -> new UsernameNotFoundException("No user " +
//                        "Found with email : " + email));
//        return new UserPrincipal(user.getEmail(), user.getPassword(), true,
//                true, true, true, Collections.emptyList());

        CustomUser userEntity = userRepository.findByEmail(email).orElseThrow(() -> new BadCredentialsException("bad.credentials"));
        return new UserPrincipal(userEntity.getId(), userEntity.getEmail(), userEntity.getPassword(), Collections.emptyList());

    }
}
