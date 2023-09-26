package com.forumalura.domain.answers;

import java.time.LocalDateTime;

public record AnswerDataResponse(Long id, String message, Long topicId, LocalDateTime creationDate, Long authorId, Boolean solution) {
}
