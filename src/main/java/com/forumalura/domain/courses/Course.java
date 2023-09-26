package com.forumalura.domain.courses;

import jakarta.persistence.*;
import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Data
@EqualsAndHashCode(of = "id")
@ToString
@Entity
@Table(name = "courses")
public class Course {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false, length = 170)
    private String name;
    @Column(nullable = false, length = 170)
    private String category;
    @Column(nullable = false)
    private boolean activeDate = true;
    public Course (CourseCreateDTO dto){
        this.name= dto.name();
        this.category= dto.category();
    }
    public Course (CourseUpdateDTO dto){
        this.id = dto.id();
        this.name= dto.name();
        this.category= dto.category();
    }
    public CourseDataResponse getDataResponse(){
        return new CourseDataResponse(this.id, this.name,this.category);
    }
    public void update(CourseUpdateDTO dto){
        this.name = dto.name();
        this.category = dto.category();
    }
}
