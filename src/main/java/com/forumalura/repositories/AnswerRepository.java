package com.forumalura.repositories;

import com.forumalura.domain.answers.Answer;
import com.forumalura.domain.topics.Topic;
import com.forumalura.domain.users.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AnswerRepository extends JpaRepository<Answer,Long> {
    Optional<Answer> findByMessageAndTopic(String message,Topic topic);
    Page<Answer> findAllByActiveDateTrue(Pageable pageable);
    Page<Answer> findAllByAuthor(User author, Pageable pageable);
    Page<Answer> findAllByTopic(Topic topic, Pageable pageable);
    long count();
}
