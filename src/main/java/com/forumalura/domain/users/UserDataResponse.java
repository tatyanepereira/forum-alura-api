package com.forumalura.domain.users;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record UserDataResponse(@NotNull Long id,
                               @NotBlank String name,
                               @NotBlank String email,
                               @NotBlank String password,
                               boolean active) {
}
