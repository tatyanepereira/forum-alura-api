package com.forumalura.services;

import com.forumalura.domain.users.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface UserService {
    User save(User user);
    Optional<User> findByUserId(Long id);
    Optional<User> findByEmail(String email);
    Page<User> findAll(Pageable pageable);
    Page<User> findAllActive(Pageable pageable);
    void delete(Long id);
    User disable(Long id);
    boolean existById(Long id);
}
