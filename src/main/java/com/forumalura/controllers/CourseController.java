package com.forumalura.controllers;

import com.forumalura.domain.courses.Course;
import com.forumalura.domain.courses.CourseCreateDTO;
import com.forumalura.domain.courses.CourseUpdateDTO;
import com.forumalura.domain.courses.CourseValidation;
import com.forumalura.infra.exception.NotFoundException;
import com.forumalura.services.CourseService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.Optional;

@RestController
@RequestMapping("/api/courses")
@SecurityRequirement(name = "Forum")
@Tag(name = "Courses", description = "Make a complete CRUD of the Courses")
public class CourseController {
    @Autowired
    CourseService courseService;
    @Autowired
    CourseValidation validation;

    @Operation(summary = "Create a course in Database")
    @PostMapping
    public ResponseEntity<Object> createCourse(@RequestBody @Valid CourseCreateDTO requestDTO, UriComponentsBuilder uriBuilder){
        validation.validationCreate(requestDTO);
        var course = courseService.save(new Course(requestDTO));
        var uri = uriBuilder.path("/api/courses/{id}").buildAndExpand(course.getId()).toUri();
        return ResponseEntity.created(uri).body(course.getDataResponse());
    }

    @Operation(summary = "Find all courses in Database")
    @GetMapping("/all")
    public ResponseEntity<Page<Course>> getAllCourse(@ParameterObject Pageable pageable){
        return ResponseEntity.ok(courseService.findAll(pageable));
    }

    @Operation(summary = "Find all courses actives in Database")
    @GetMapping
    public ResponseEntity<Page<Course>> getAllCourseActive(@ParameterObject Pageable pageable){
        return ResponseEntity.ok(courseService.findAllActive(pageable));
    }

    @Operation(summary = "Find a course in Database by Id")
    @GetMapping("/{id}")
    public ResponseEntity<Object> getByIdCourse(@Parameter(description = "Id of Course to be Searched")
                                              @PathVariable(value = "id") Long id){
        Optional<Course> optional = courseService.findById(id);
        if (optional.isPresent())
            return ResponseEntity.ok(optional.get().getDataResponse());
        throw new NotFoundException("Course not found!");
    }

    @Operation(summary = "Find a course in Database by Id")
    @GetMapping("/name/{name}")
    public ResponseEntity<Object> getByNameCourse(@Parameter(description = "Name of Course to be Searched")
                                                @PathVariable(value = "name") String name){
        Optional<Course> optional = courseService.findByName(name);
        if (optional.isPresent())
            return ResponseEntity.ok(optional.get().getDataResponse());
        throw new NotFoundException("Course not found!");
    }

    @Operation(summary = "Update a course in Database")
    @PutMapping
    public ResponseEntity<Object> updateCourse(@RequestBody @Valid CourseUpdateDTO requestDTO){
        validation.validationUpdate(requestDTO);
        Optional<Course> optional = courseService.findById(requestDTO.id());
        if (optional.isPresent()){
            optional.get().update(requestDTO);
            var course = courseService.save(optional.get());
            return ResponseEntity.ok(course.getDataResponse());
        }
        throw new NotFoundException("Course not found!");
    }

    @Operation(summary = "Disable a course in database by its id")
    @DeleteMapping("/{id}")
    public ResponseEntity<Object> disableCourse (@Parameter(description = "Id of Course to be Disabled")
                                               @PathVariable(value = "id") Long id){
        if (courseService.existById(id)) {
            courseService.disable(id);
            return ResponseEntity.noContent().eTag("Course disabled successfully.").build();
        }
        throw new NotFoundException("Course not found!");
    }

    @Operation(summary = "Delete a course in database by its id")
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Object> deleteCourse(@Parameter(description = "Id of Course to be Deleted")
                                              @PathVariable(value = "id") Long id){
        validation.validationDelete(id);
        courseService.delete(id);
        return  ResponseEntity.noContent().eTag("Course deleted successfully.").build();
    }
}
