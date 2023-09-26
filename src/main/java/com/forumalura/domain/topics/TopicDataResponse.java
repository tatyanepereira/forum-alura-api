package com.forumalura.domain.topics;

import java.time.LocalDateTime;

public record TopicDataResponse(Long id, String title, String message,
                                LocalDateTime creationDate, TopicStatus status,
                                Long authorId, Long courseId) {
}
