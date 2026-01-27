package com.lb_works_garage.activities;

import com.lb_works_garage.project.Project;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

@Entity
@Table(name = "activities")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Activity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @Column(name = "occurs_at", nullable = false)
    private LocalDateTime occursAt;

    @Column(nullable = false)
    private String title;

    @ManyToOne
    @JoinColumn(name = "project_id", nullable = false)
    private Project project;

    public Activity(String title, String occureAt, Project project){
        this.title = title;
        this.occursAt = LocalDateTime.parse(occureAt , DateTimeFormatter.ISO_DATE_TIME);
        this.project = project;
    }
}
