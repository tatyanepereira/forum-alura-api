package com.forumalura.services;

import com.forumalura.domain.courses.Course;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface CourseService {
    Course save(Course course);
    Optional<Course> findById(Long id);
    Optional<Course> findByName(String name);
    Page<Course> findAll(Pageable pageable);
    Page<Course> findAllActive(Pageable pageable);
    void delete(Long id);
    Course disable(Long id);
    boolean existById(Long id);
}
