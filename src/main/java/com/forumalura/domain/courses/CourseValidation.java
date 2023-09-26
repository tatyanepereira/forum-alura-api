package com.forumalura.domain.courses;

import com.forumalura.infra.exception.NotFoundException;
import com.forumalura.infra.exception.ValidationException;
import com.forumalura.services.CourseService;
import com.forumalura.services.TopicService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class CourseValidation {
    @Autowired
    CourseService courseService;
    @Autowired
    TopicService topicService;

    public void validationCreate(CourseCreateDTO dto){
        Optional<Course> optional = courseService.findByName(dto.name());
        if (optional.isPresent())
            throw new ValidationException("Course already registered!");
    }
    public void validationUpdate(CourseUpdateDTO dto){
        Optional<Course> courseOptional = courseService.findById(dto.id());
        if (courseOptional.isEmpty())
            throw new NotFoundException("Course not found!");
        if (!dto.name().equalsIgnoreCase(courseOptional.get().getName())){
            Optional<Course> optional = courseService.findByName(dto.name());
            if (optional.isPresent())
                throw new ValidationException("Course already registered!");
        }
    }
    public void validationDelete(Long courseId){
        Optional<Course> courseOptional = courseService.findById(courseId);
        if (courseOptional.isEmpty())
            throw new NotFoundException("Course not found!");
        var page = topicService.findAllByCourse(courseOptional.get(), Pageable.unpaged());
        if (!page.hasContent())
            throw new ValidationException("There is a registered topic with this course");
    }
}
