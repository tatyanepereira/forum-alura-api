package com.forumalura.repositories;

import com.forumalura.domain.courses.Course;
import com.forumalura.domain.topics.Topic;
import com.forumalura.domain.users.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.Optional;

public interface TopicRepository extends JpaRepository<Topic, Long> {
    Optional<Topic> findByTitleAndMessage(String title,String message);
    Page<Topic> findAllByActiveDateTrue(Pageable pageable);
    Page<Topic> findAllByActiveDateTrueAndCreationDateBetween(LocalDateTime dateStart,
                                                              LocalDateTime dateEnd,
                                                              Pageable pageable);
    Page<Topic> findAllByActiveDateTrueAndAuthor(User author, Pageable pageable);
    Page<Topic> findAllByActiveDateTrueAndCourse(Course course,Pageable pageable);

    long count();
}
