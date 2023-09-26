package com.forumalura.domain.topics;

import com.forumalura.domain.courses.Course;
import com.forumalura.domain.users.User;
import com.forumalura.infra.exception.NotFoundException;
import com.forumalura.infra.exception.ValidationException;
import com.forumalura.services.AuthenticationFacade;
import com.forumalura.services.CourseService;
import com.forumalura.services.TopicService;
import com.forumalura.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class TopicValidation {
    @Autowired
    TopicService topicService;
    @Autowired
    UserService userService;
    @Autowired
    CourseService courseService;
    @Autowired
    AuthenticationFacade authenticationFacade;

    public Topic validationCreate(TopicCreateDTO dto){
        Optional<Topic> optionalTopic = topicService.findByTitleAndMessage(dto.title(), dto.message());
        if (optionalTopic.isPresent()) throw new ValidationException("Topic already exists!");
        Optional<Course> courseOptional = courseService.findById(dto.courseId());
        if (courseOptional.isEmpty())  throw new NotFoundException("Course not found!");
        return new Topic(dto,courseOptional.get(),getAuthor());
    }

    public Topic validationUpdate(TopicUpdateDTO dto){
        Optional<Topic> optionalTopic = topicService.findById(dto.id());
        if (optionalTopic.isEmpty()) throw new NotFoundException("Topic not found!");
        Optional<Course> courseOptional = courseService.findById(dto.courseId());
        if (courseOptional.isEmpty())  throw new NotFoundException("Course not found!");
        if (dto.title().equalsIgnoreCase(optionalTopic.get().getTitle()) &&
                dto.message().equalsIgnoreCase(optionalTopic.get().getMessage())){
            optionalTopic.get().setCourse(courseOptional.get());
        }else {
            Optional<Topic> optionalTopic2 = topicService.findByTitleAndMessage(dto.title(), dto.message());
            if (optionalTopic2.isPresent()) throw new ValidationException("Topic already exists!");
            optionalTopic.get().setTitle(dto.title());
            optionalTopic.get().setMessage(dto.message());
        }
        return optionalTopic.get();
    }

    public Topic validationUpdateStatus(TopicUpdateStatusDTO dto){
        Optional<Topic> optionalTopic = topicService.findById(dto.id());
        if (optionalTopic.isEmpty()) throw new NotFoundException("Topic not found!");
        optionalTopic.get().setStatus(dto.status());
        return optionalTopic.get();
    }

    public User getAuthor(){
        return userService.findByEmail(authenticationFacade.getEmail()).get();
    }
}
