package com.forumalura.services;

import com.forumalura.domain.answers.Answer;
import com.forumalura.domain.topics.Topic;
import com.forumalura.domain.users.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface AnswerService {
    Answer save(Answer answer);
    Optional<Answer> findById(Long id);
    Optional<Answer> findByMessageAndTopic(String message,Topic topic);
    Page<Answer> findAll(Pageable pageable);
    Page<Answer> findAllByActiveDateTrue(Pageable pageable);
    Page<Answer> findAllByAuthor(User author, Pageable pageable);
    Page<Answer> findAllByTopic(Topic topic, Pageable pageable);
    void delete(Long id);
    Answer disable(Long id);
    boolean existById(Long id);
}
