package com.forumalura.domain.answers;

import com.forumalura.domain.topics.Topic;
import com.forumalura.domain.users.User;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Data
@EqualsAndHashCode(of = "id")
@ToString
@Entity
@Table(name = "answers")
public class Answer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false)
    private String message;
    @ManyToOne( cascade = CascadeType.ALL)
    @JoinColumn(name = "topic_id")
    private Topic topic;
    private LocalDateTime creationDate = LocalDateTime.now();
    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "author_id")
    private User author;
    private Boolean solution = false;
    @Column(nullable = false)
    private boolean activeDate = true;

    public Answer(String message,Topic topic,User author){
        this.message= message;
        this.topic = topic;
        this.author = author;
    }
    public AnswerDataResponse getDataResponse(){
        return new AnswerDataResponse(this.id,this.message,this.topic.getId(),
                                        this.creationDate,this.author.getUserId(),this.solution);
    }
}
