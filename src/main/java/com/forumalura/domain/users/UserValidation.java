package com.forumalura.domain.users;

import com.forumalura.infra.exception.NotFoundException;
import com.forumalura.infra.exception.ValidationException;
import com.forumalura.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class UserValidation {
    @Autowired
    UserService userService;

    public void validationCreate(UserCreateDTO dto){
        Optional<User> optional = userService.findByEmail(dto.email());
        if (optional.isPresent())
            throw new ValidationException("User email already registered!");
    }
    public void validationUpdate(UserUpdateDTO dto){
        Optional<User> optional = userService.findByUserId(dto.id());
        if (optional.isEmpty())
            throw new NotFoundException("User not found!");
        if (!dto.email().equalsIgnoreCase(optional.get().getEmail())){
            Optional<User> optional2 = userService.findByEmail(dto.email());
            if (optional2.isPresent())
                throw new ValidationException("User email already registered!");
        }
    }
}
