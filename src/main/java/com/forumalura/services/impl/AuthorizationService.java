package com.forumalura.services.impl;

import com.forumalura.domain.users.User;
import com.forumalura.repositories.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class AuthorizationService implements UserDetailsService {
    final UserRepository userRepository;
    public AuthorizationService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<User> optional =  userRepository.findByEmail(username);
        return optional.orElse(null);
    }

    @Transactional
    public User save(User user){return userRepository.save(user);}

    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }
}
