package com.forumalura.domain.topics;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record TopicUpdateDTO(@NotNull Long id,
                             @NotBlank String title,
                             @NotBlank String message,
                             @NotNull Long courseId) {
}
