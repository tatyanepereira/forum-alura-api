package com.forumalura.domain.answers;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record AnswerCreateDTO(@NotBlank String message, @NotNull Long topicId) {
}
