package com.forumalura.domain.users;

import jakarta.validation.constraints.NotBlank;

public record AuthenticationDTO(@NotBlank String email,@NotBlank String password) {
}
