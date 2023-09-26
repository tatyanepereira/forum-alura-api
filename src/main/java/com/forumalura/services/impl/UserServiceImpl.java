package com.forumalura.services.impl;

import com.forumalura.domain.users.User;
import com.forumalura.repositories.UserRepository;
import com.forumalura.services.UserService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
@Service
public class UserServiceImpl implements UserService {
    final UserRepository userRepository;
    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Transactional
    @Override
    public User save(User user) {
        return userRepository.save(user);
    }

    @Override
    public Optional<User> findByUserId(Long id) {
        return userRepository.findById(id);
    }

    @Override
    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    @Override
    public Page<User> findAll(Pageable pageable) {
        return userRepository.findAll(pageable);
    }

    @Override
    public Page<User> findAllActive(Pageable pageable) {
        return userRepository.findAllByActiveDateTrue(pageable);
    }

    @Transactional
    @Override
    public void delete(Long id) {
        userRepository.deleteById(id);
    }

    @Transactional
    @Override
    public User disable(Long id) {
        Optional<User> optional= userRepository.findById(id);
        if (optional.isPresent()){
            optional.get().setActiveDate(false);
            return userRepository.save(optional.get());
        }
        return null;
    }

    @Override
    public boolean existById(Long id) {
        return userRepository.existsById(id);
    }
}
