package com.forumalura.domain.answers;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record AnswerUpdateDTO(@NotNull Long id, @NotBlank String message) {
}
