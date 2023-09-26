package com.forumalura.controllers;

import com.forumalura.domain.users.User;
import com.forumalura.domain.users.UserCreateDTO;
import com.forumalura.domain.users.UserUpdateDTO;
import com.forumalura.domain.users.UserValidation;
import com.forumalura.infra.exception.NotFoundException;
import com.forumalura.services.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.Optional;

@RestController
@RequestMapping("/api/users")
@SecurityRequirement(name = "Forum")
@Tag(name = "Users", description = "Make a complete CRUD of the Users")
public class UserController {
    @Autowired
    UserService userService;
    @Autowired
    UserValidation validation;

    @Operation(summary = "Create a user in Database")
    @PostMapping
    public ResponseEntity<Object> createUser(@RequestBody @Valid UserCreateDTO requestDTO, UriComponentsBuilder uriBuilder){
        validation.validationCreate(requestDTO);
        var user = userService.save(new User(requestDTO));
        var uri = uriBuilder.path("/api/users/{id}").buildAndExpand(user.getUserId()).toUri();
        return ResponseEntity.created(uri).body(user.getDataResponse());
    }
    @Operation(summary = "Find all users in Database")
    @GetMapping("/all")
    public ResponseEntity<Page<User>> getAllUser(@ParameterObject Pageable pageable){
        var page = userService.findAll(pageable);
        return ResponseEntity.ok(page);
    }
    @Operation(summary = "Find all users actives in Database")
    @GetMapping
    public ResponseEntity<Page<User>> getAllUserActive(@ParameterObject Pageable pageable){
        return ResponseEntity.ok(userService.findAllActive(pageable));
    }
    @Operation(summary = "Find a user in Database by Id")
    @GetMapping("/{id}")
    public ResponseEntity<Object> getByIdUser(@Parameter(description = "Id of User to be Searched")
                                              @PathVariable(value = "id") Long id){
        Optional<User> optional = userService.findByUserId(id);
        if (optional.isPresent()) return ResponseEntity.ok(optional.get().getDataResponse());
        throw new NotFoundException("User not found!");
    }
    @Operation(summary = "Find a User in Database by Email")
    @GetMapping("/email/{email}")
    public ResponseEntity<Object> getByLoginUser(@Parameter(description = "Email of user to be Searched")
                                                 @PathVariable(value = "email") String email){
        Optional<User> optional = userService.findByEmail(email);
        if (optional.isPresent()) return ResponseEntity.ok(optional.get().getDataResponse());
        throw new NotFoundException("User not found!");
    }
    @Operation(summary = "Update a user in Database")
    @PutMapping
    public ResponseEntity<Object> updateUser(@RequestBody @Valid UserUpdateDTO requestDTO){
        validation.validationUpdate(requestDTO);
        Optional<User> optional = userService.findByUserId(requestDTO.id());
        if (optional.isPresent()){
            optional.get().update(requestDTO);
            var user = userService.save(optional.get());
            return ResponseEntity.ok(user.getDataResponse());
        }
        throw new NotFoundException("User not found!");
    }
    @Operation(summary = "Disable a User in Database by its Id")
    @DeleteMapping("/{id}")
    public ResponseEntity<Object> disableUser (@Parameter(description = "Id of User to be Disabled")
                                              @PathVariable(value = "id") Long id){
        if (userService.existById(id)){
            userService.disable(id);
            return ResponseEntity.noContent().eTag("User disabled successfully.").build();
        }
        throw new NotFoundException("User not found!");
    }
    @Operation(summary = "Delete a User in Database by its Id")
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Object> deleteUser (@Parameter(description = "Id of User to be Deleted")
                                              @PathVariable(value = "id") Long id){
        if (userService.existById(id)){
            userService.delete(id);
            return ResponseEntity.noContent().eTag("User deleted successfully.").build();
        }
        throw new NotFoundException("User not found!");
    }
}
