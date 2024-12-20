package com.example.demo.db.domain;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "courses")
public class Course {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private Long id;
    @Column(unique = true)
    private String name;
    @Column(columnDefinition = "TEXT")

    private String description;
    private Boolean active;
    @OneToMany(mappedBy = "course", fetch = FetchType.EAGER)
    private List<Group> groups;

}
