package com.forumalura.controllers;

import com.forumalura.domain.topics.*;
import com.forumalura.infra.exception.NotFoundException;
import com.forumalura.services.TopicService;
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
@RequestMapping("/api/topics")
@SecurityRequirement(name = "Forum")
@Tag(name = "Topics", description = "Make a complete CRUD of the Topics")
public class TopicController {
    @Autowired
    TopicService topicService;
    @Autowired
    TopicValidation validation;

    @Operation(summary = "Create a topics in Database")
    @PostMapping
    public ResponseEntity<Object> createTopic(@RequestBody @Valid TopicCreateDTO requestDTO, UriComponentsBuilder uriBuilder){
        var topicRequest = validation.validationCreate(requestDTO);
        var topic = topicService.save(topicRequest);
        var uri = uriBuilder.path("/api/topics/{id}").buildAndExpand(topic.getId()).toUri();
        return ResponseEntity.created(uri).body(topic.getDataResponse());
    }

    @Operation(summary = "Find all topics in Database")
    @GetMapping("/all")
    public ResponseEntity<Page<Topic>> getAllTopic(@ParameterObject Pageable pageable){
        return ResponseEntity.ok(topicService.findAll(pageable));
    }

    @Operation(summary = "Find all topics in Database where the user is the author")
    @GetMapping("/author")
    public ResponseEntity<Page<Topic>> getAllTopicByAuthor(@ParameterObject Pageable pageable){
        var author = validation.getAuthor();
        return ResponseEntity.ok(topicService.findAllByAuthor(author,pageable));
    }

    @Operation(summary = "Find all topics actives in Database")
    @GetMapping
    public ResponseEntity<Page<Topic>> getAllTopicActive(@ParameterObject Pageable pageable){
        return ResponseEntity.ok(topicService.findAllActive(pageable));
    }

    @Operation(summary = "Find a topics in Database by Id")
    @GetMapping("/{id}")
    public ResponseEntity<Object> getByIdTopic(@Parameter(description = "Id of Topic to be Searched")
                                                @PathVariable(value = "id") Long id){
        Optional<Topic> optional = topicService.findById(id);
        if (optional.isPresent())
            return ResponseEntity.ok(optional.get());
        throw new NotFoundException("Topic not found!");
    }

    @Operation(summary = "Update a topics in Database")
    @PutMapping
    public ResponseEntity<Object> updateTopic(@RequestBody @Valid TopicUpdateDTO requestDTO){
        var topicRequest = validation.validationUpdate(requestDTO);
        var topic = topicService.save(topicRequest);
        return ResponseEntity.ok(topic.getDataResponse());
    }

    @Operation(summary = "Update a topics status in Database")
    @PutMapping("/status")
    public ResponseEntity<Object> updateStatusTopic(@RequestBody @Valid TopicUpdateStatusDTO requestDTO){
        var topicRequest = validation.validationUpdateStatus(requestDTO);
        var topic = topicService.save(topicRequest);
        return ResponseEntity.ok(topic.getDataResponse());
    }

    @Operation(summary = "Disable a topics in database by its id")
    @DeleteMapping("/{id}")
    public ResponseEntity<Object> disableTopic(@Parameter(description = "Id of Topic to be Disabled")
                                                 @PathVariable(value = "id") Long id){
        if (topicService.existById(id)) {
            topicService.disable(id);
            return ResponseEntity.noContent().eTag("Topic disabled successfully.").build();
        }
        throw new NotFoundException("Topic not found!");
    }

    @Operation(summary = "Delete a topics in database by its id")
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Object> deleteTopic(@Parameter(description = "Id of Topic to be Deleted")
                                               @PathVariable(value = "id") Long id){
        if (topicService.existById(id)){
            topicService.delete(id);
            return  ResponseEntity.noContent().eTag("Topic deleted successfully.").build();
        }
        throw new NotFoundException("Topic not found!");
    }
}
