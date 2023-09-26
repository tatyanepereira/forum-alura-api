package com.forumalura.domain.answers;

import com.forumalura.domain.topics.Topic;
import com.forumalura.domain.topics.TopicStatus;
import com.forumalura.domain.users.User;
import com.forumalura.infra.exception.NotFoundException;
import com.forumalura.infra.exception.ValidationException;
import com.forumalura.services.AnswerService;
import com.forumalura.services.AuthenticationFacade;
import com.forumalura.services.TopicService;
import com.forumalura.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class AnswerValidation {
    @Autowired
    AnswerService answerService;
    @Autowired
    AuthenticationFacade authenticationFacade;
    @Autowired
    TopicService topicService;
    @Autowired
    UserService userService;
    public Answer validationCreate(AnswerCreateDTO dto){
        Optional<Topic> topicOptional = topicService.findById(dto.topicId());
        if (topicOptional.isEmpty()) throw new NotFoundException("Topic not found!");
        if (topicOptional.get().getStatus()== TopicStatus.FECHADO)
            throw new ValidationException("This Topic is Closed!");
        Optional<Answer> optional = answerService.findByMessageAndTopic(dto.message(),topicOptional.get());
        if (optional.isPresent())
            throw new ValidationException("Answer already registered in this Topic!");
        return new Answer(dto.message(), topicOptional.get(),getAuthor());
    }
    public Answer validationUpdate(AnswerUpdateDTO dto){
        Optional<Answer> answerOptional = answerService.findById(dto.id());
        if (answerOptional.isEmpty()) throw new NotFoundException("Answer not found!");
        if (answerOptional.get().getTopic().getStatus()== TopicStatus.FECHADO)
            throw new ValidationException("This Topic is Closed!");
        if (!dto.message().equalsIgnoreCase(answerOptional.get().getMessage())){
            Optional<Answer> optional = answerService.findByMessageAndTopic(dto.message(),answerOptional.get().getTopic());
            if (optional.isPresent())
                throw new ValidationException("Answer already registered in this Topic!");
        }
        answerOptional.get().setMessage(dto.message());
        return answerOptional.get();
    }
    public User getAuthor(){
        return userService.findByEmail(authenticationFacade.getEmail()).get();
    }
}
