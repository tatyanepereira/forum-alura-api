package com.forumalura.domain.courses;

import jakarta.validation.constraints.NotBlank;

public record CourseCreateDTO(@NotBlank String name, @NotBlank String category) {
}
