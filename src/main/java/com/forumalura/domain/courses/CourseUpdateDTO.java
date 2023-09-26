package com.forumalura.domain.courses;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record CourseUpdateDTO(@NotNull Long id, @NotBlank String name, @NotBlank String category) {
}
