package com.forumalura.services.impl;

import com.forumalura.domain.answers.Answer;
import com.forumalura.domain.topics.Topic;
import com.forumalura.domain.users.User;
import com.forumalura.repositories.AnswerRepository;
import com.forumalura.services.AnswerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;
@Service
public class AnswerServiceImpl implements AnswerService {
    @Autowired
    AnswerRepository answerRepository;
    @Override
    public Answer save(Answer answer) {
        return answerRepository.save(answer);
    }

    @Override
    public Optional<Answer> findById(Long id) {
        return answerRepository.findById(id);
    }

    @Override
    public Optional<Answer> findByMessageAndTopic(String message,Topic topic){
        return answerRepository.findByMessageAndTopic(message, topic);
    }

    @Override
    public Page<Answer> findAll(Pageable pageable) {
        return answerRepository.findAll(pageable);
    }

    @Override
    public Page<Answer> findAllByActiveDateTrue(Pageable pageable) {
        return answerRepository.findAllByActiveDateTrue(pageable);
    }

    @Override
    public Page<Answer> findAllByAuthor(User author, Pageable pageable) {
        return answerRepository.findAllByAuthor(author, pageable);
    }

    @Override
    public Page<Answer> findAllByTopic(Topic topic, Pageable pageable) {
        return answerRepository.findAllByTopic(topic, pageable);
    }

    @Override
    public void delete(Long id) {
        if (existById(id))answerRepository.deleteById(id);
    }

    @Override
    public Answer disable(Long id) {
        if (existById(id)){
            var answer = findById(id).get();
            answer.setActiveDate(false);
            answerRepository.save(answer);
        }
        return null;
    }

    @Override
    public boolean existById(Long id) {
        return answerRepository.existsById(id);
    }
}
