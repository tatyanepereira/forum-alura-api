package com.forumalura.domain.topics;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.forumalura.domain.answers.Answer;
import com.forumalura.domain.courses.Course;
import com.forumalura.domain.users.User;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Data
@EqualsAndHashCode(of = "id")
@ToString
@Entity
@Table(name = "topics")
public class Topic {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false, length = 170)
    private String title;
    @Column(nullable = false)
    private String message;
    private LocalDateTime creationDate = LocalDateTime.now();
    @Enumerated(EnumType.STRING)
    private TopicStatus status = TopicStatus.NAO_RESPONDIDO;
    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "author_id")
    private User author;
    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "course_id")
    private Course course;
    @OneToMany(mappedBy = "topic")
    @JsonIgnore
    private List<Answer> answers = new ArrayList<>();
    @Column(nullable = false)
    private boolean activeDate = true;

    public Topic (TopicCreateDTO dto,Course course,User author){
        this.title = dto.title();
        this.message = dto.message();
        this.course = course;
        this.author = author;
    }

    public void updateTopic(TopicUpdateDTO dto,Course course){
        setTitle(dto.title());
        setMessage(dto.message());
        setCourse(course);
    }
    public TopicDataResponse getDataResponse(){
        return new TopicDataResponse(this.id,this.title,this.message,this.creationDate,
                                    this.status,this.author.getUserId(),this.course.getId());
    }
}
