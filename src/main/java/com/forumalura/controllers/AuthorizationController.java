package com.forumalura.controllers;

import com.forumalura.domain.users.AuthenticationDTO;
import com.forumalura.domain.users.User;
import com.forumalura.domain.users.UserCreateDTO;
import com.forumalura.infra.security.TokenService;
import com.forumalura.services.impl.AuthorizationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
@RequestMapping("/api/auth")
@SecurityRequirement(name = "Forum")
@Tag(name = "Authentication", description = "User creation and authentication")
public class AuthorizationController {
    final AuthenticationManager authenticationManager;
    final AuthorizationService authorizationService;
    final TokenService tokenService;

    public AuthorizationController(AuthenticationManager authenticationManager,
                                   AuthorizationService authorizationService,
                                   TokenService tokenService) {
        this.authenticationManager = authenticationManager;
        this.authorizationService = authorizationService;
        this.tokenService = tokenService;
    }

    @Operation(summary = "User login and generate a token")
    @PostMapping("/login")
    public ResponseEntity login(@RequestBody @Valid AuthenticationDTO dto){
        var usernamePassword = new UsernamePasswordAuthenticationToken(dto.email(), dto.password());
        var auth = this.authenticationManager.authenticate(usernamePassword);
        var token = tokenService.generateToken((User) auth.getPrincipal());
        return ResponseEntity.ok(token);
    }
    @Operation(summary = "Creates a User in the Database")
    @PostMapping("/register")
    public ResponseEntity register(@RequestBody @Valid UserCreateDTO dto){
        Optional<User> optionalUserModel = authorizationService.findByEmail(dto.email());
        if(optionalUserModel.isPresent()) return ResponseEntity.badRequest().body("impossible to record! User already registered.");
        authorizationService.save(new User(dto));
        return ResponseEntity.ok("Successfully created user");
    }
}
