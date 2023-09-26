package com.forumalura.domain.topics;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record TopicUpdateStatusDTO(@NotNull Long id, @NotBlank TopicStatus status) {
}
