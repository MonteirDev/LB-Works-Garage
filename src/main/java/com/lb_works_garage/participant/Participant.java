package com.lb_works_garage.participant;


import com.lb_works_garage.project.Project;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Locale;
import java.util.UUID;

@Entity
@Table(name = "participants")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Participant {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String email;

    @Column(nullable = false)
    private String function;

    @Column(name = "is_confirmed", nullable = false)
    private Boolean isConfirmed;

    @ManyToOne
    @JoinColumn(name = "project_id", nullable = false)
    private Project project;

    public Participant (String email, Project project){
        this.email = email;
        this.project = project;
        this.isConfirmed = false;
        this.name = "";
        this.function = "";
    }
}
