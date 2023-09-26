package com.forumalura.controllers;

import com.forumalura.domain.answers.Answer;
import com.forumalura.domain.answers.AnswerCreateDTO;
import com.forumalura.domain.answers.AnswerUpdateDTO;
import com.forumalura.domain.answers.AnswerValidation;
import com.forumalura.domain.topics.Topic;
import com.forumalura.domain.topics.TopicStatus;
import com.forumalura.infra.exception.NotFoundException;
import com.forumalura.services.AnswerService;
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
@RequestMapping("/api/answers")
@SecurityRequirement(name = "Forum")
@Tag(name = "Answers", description = "Make a complete CRUD of the Answers")
public class AnswerController {
    @Autowired
    AnswerService answerService;
    @Autowired
    TopicService topicService;
    @Autowired
    AnswerValidation validation;

    @Operation(summary = "Create a answers in Database")
    @PostMapping
    public ResponseEntity<Object> createAnswer(@RequestBody @Valid AnswerCreateDTO requestDTO, UriComponentsBuilder uriBuilder){
        var answerRequest = validation.validationCreate(requestDTO);
        var answer = answerService.save(answerRequest);
        if (answer.getTopic().getStatus()==TopicStatus.NAO_RESPONDIDO){
            answer.getTopic().setStatus(TopicStatus.NAO_SOLUCIONADO);
            topicService.save(answer.getTopic());
        }
        var uri = uriBuilder.path("/api/answers/{id}").buildAndExpand(answer.getId()).toUri();
        return ResponseEntity.created(uri).body(answer.getDataResponse());
    }

    @Operation(summary = "Find all answers in Database")
    @GetMapping("/all")
    public ResponseEntity<Page<Answer>> getAllAnswer(@ParameterObject Pageable pageable){
        return ResponseEntity.ok(answerService.findAll(pageable));
    }

    @Operation(summary = "Find all answers in Database where the user is the author")
    @GetMapping("/author")
    public ResponseEntity<Page<Answer>> getAllAnswerByAuthor(@ParameterObject Pageable pageable){
        var author = validation.getAuthor();
        return ResponseEntity.ok(answerService.findAllByAuthor(author,pageable));
    }

    @Operation(summary = "Find all answers in Database of a topic")
    @GetMapping("/topic/{id}")
    public ResponseEntity<Page<Answer>> getAllAnswerByTopic(@ParameterObject Pageable pageable,
                                                            @Parameter(description = "Id of Answer to be Searched")
                                                            @PathVariable(value = "id") Long id){
        Optional<Topic> topic = topicService.findById(id);
        if (topic.isPresent())
            return ResponseEntity.ok(answerService.findAllByTopic(topic.get(),pageable));
        throw new NotFoundException("Topic not found!");
    }

    @Operation(summary = "Find all answers actives in Database")
    @GetMapping
    public ResponseEntity<Page<Answer>> getAllAnswerActive(@ParameterObject Pageable pageable){
        return ResponseEntity.ok(answerService.findAllByActiveDateTrue(pageable));
    }

    @Operation(summary = "Find a answers in Database by Id")
    @GetMapping("/{id}")
    public ResponseEntity<Object> getByIdAnswer(@Parameter(description = "Id of Answer to be Searched")
                                               @PathVariable(value = "id") Long id){
        Optional<Answer> optional = answerService.findById(id);
        if (optional.isPresent())
            return ResponseEntity.ok(optional.get().getDataResponse());
        throw new NotFoundException("Answer not found!");
    }

    @Operation(summary = "Update a answers in Database")
    @PutMapping
    public ResponseEntity<Object> updateAnswer(@RequestBody @Valid AnswerUpdateDTO requestDTO){
        var answerRequest = validation.validationUpdate(requestDTO);
        var answer = answerService.save(answerRequest);
        return ResponseEntity.ok(answer.getDataResponse());
    }

    @Operation(summary = "Disable a answers in database by its id")
    @DeleteMapping("/{id}")
    public ResponseEntity<Object> disableAnswer(@Parameter(description = "Id of Answer to be Disabled")
                                               @PathVariable(value = "id") Long id){
        if (answerService.existById(id)) {
            answerService.disable(id);
            return ResponseEntity.noContent().eTag("Answer disabled successfully.").build();
        }
        throw new NotFoundException("Answer not found!");
    }

    @Operation(summary = "Delete a answers in database by its id")
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Object> deleteAnswer(@Parameter(description = "Id of Answer to be Deleted")
                                              @PathVariable(value = "id") Long id){
        if (answerService.existById(id)){
            answerService.delete(id);
            return  ResponseEntity.noContent().eTag("Answer deleted successfully.").build();
        }
        throw new NotFoundException("Answer not found!");
    }
}
