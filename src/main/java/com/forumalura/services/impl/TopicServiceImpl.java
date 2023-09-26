package com.forumalura.services.impl;

import com.forumalura.domain.courses.Course;
import com.forumalura.domain.topics.Topic;
import com.forumalura.domain.users.User;
import com.forumalura.repositories.TopicRepository;
import com.forumalura.services.TopicService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.Month;
import java.util.Optional;
@Service
public class TopicServiceImpl implements TopicService {
    @Autowired
    TopicRepository topicRepository;

    @Transactional
    @Override
    public Topic save(Topic topic) {
        return topicRepository.save(topic);
    }

    @Override
    public Optional<Topic> findById(Long id) {
        return topicRepository.findById(id);
    }

    @Override
    public Optional<Topic> findByTitleAndMessage(String title,String message){
        return topicRepository.findByTitleAndMessage(title, message);
    }

    @Override
    public Page<Topic> findAll(Pageable pageable) {
        return topicRepository.findAll(pageable);
    }

    @Override
    public Page<Topic> findAllActive(Pageable pageable) {
        return topicRepository.findAllByActiveDateTrue(pageable);
    }

    @Override
    public Page<Topic> findAllByAuthor(User author, Pageable pageable) {
        return topicRepository.findAllByActiveDateTrueAndAuthor(author,pageable);
    }

    @Override
    public Page<Topic> findAllByCourse(Course course,Pageable pageable){
        return topicRepository.findAllByActiveDateTrueAndCourse(course,pageable);
    }

    @Override
    public Page<Topic> findAllByCreationDateYear(Integer year,Pageable pageable){
        LocalDateTime dateStart = LocalDateTime.of(year, Month.JANUARY, 1, 0, 0);
        LocalDateTime dateEnd = LocalDateTime.of(year, Month.DECEMBER, 31, 23,59);
        return topicRepository.findAllByActiveDateTrueAndCreationDateBetween(dateStart,dateEnd,pageable);
    }

    @Transactional
    @Override
    public void delete(Long id) {
        if (topicRepository.existsById(id))topicRepository.deleteById(id);
    }

    @Transactional
    @Override
    public Topic disable(Long id) {
        Optional<Topic> optional = topicRepository.findById(id);
        if (optional.isPresent()){
            optional.get().setActiveDate(false);
            return topicRepository.save(optional.get());
        }
        return null;
    }

    @Override
    public boolean existById(Long id) {
        return topicRepository.existsById(id);
    }
}
