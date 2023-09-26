package com.forumalura.services.impl;

import com.forumalura.domain.courses.Course;
import com.forumalura.repositories.CourseRepository;
import com.forumalura.services.CourseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class CourseServiceImpl implements CourseService {
    @Autowired
    CourseRepository courseRepository;

    @Transactional
    @Override
    public Course save(Course course) {
        return courseRepository.save(course);
    }

    @Override
    public Optional<Course> findById(Long id) {
        return courseRepository.findById(id);
    }

    @Override
    public Optional<Course> findByName(String name){ return courseRepository.findByName(name); }

    @Override
    public Page<Course> findAll(Pageable pageable) {
        return courseRepository.findAll(pageable);
    }

    @Override
    public Page<Course> findAllActive(Pageable pageable) {
        return courseRepository.findAllByActiveDateTrue(pageable);
    }

    @Transactional
    @Override
    public void delete(Long id) {
        if (courseRepository.existsById(id)) courseRepository.deleteById(id);
    }

    @Transactional
    @Override
    public Course disable(Long id) {
        Optional<Course> optional = findById(id);
        if (optional.isPresent()){
            optional.get().setActiveDate(false);
            return courseRepository.save(optional.get());
        }
        return null;
    }

    @Override
    public boolean existById(Long id) {
        return courseRepository.existsById(id);
    }
}
