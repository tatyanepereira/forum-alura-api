package com.forumalura.domain.users;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record UserUpdateDTO(@NotNull Long id,
                            @NotBlank String name,
                            @NotBlank String email,
                            @NotBlank String password) {
}
