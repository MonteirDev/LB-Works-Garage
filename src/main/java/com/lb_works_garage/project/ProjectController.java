package com.lb_works_garage.project;

import com.lb_works_garage.participant.ParticipantService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/projects")
public class ProjectController {

    @Autowired
    private ParticipantService participantService;

    @Autowired
    private ProjectRepository repository;

    @PostMapping
    public ResponseEntity<ProjectCreateResponse> createProject(@RequestBody ProjectRequestPayload payload){
        Project newProject = new Project(payload);

        this.repository.save(newProject);
        this.participantService.registerParticipantsToProject(payload.emails_to_invite(), newProject.getId());

        return ResponseEntity.ok(new ProjectCreateResponse(newProject.getId()));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Project> getProjectDetails(@PathVariable UUID id){
        Optional<Project> project = this.repository.findById(id);
        return project.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<Project> updateProject(@PathVariable UUID id, @RequestBody ProjectRequestPayload payload){
        Optional<Project> project = this.repository.findById(id);

        if (project.isPresent()){
            Project rawProject = project.get();
            rawProject.setStartsAt(LocalDateTime.parse(payload.starts_at(), DateTimeFormatter.ISO_DATE_TIME));
            rawProject.setEndsAt(LocalDateTime.parse(payload.ends_at(), DateTimeFormatter.ISO_DATE_TIME));
            rawProject.setCarModel(payload.car_model());

            this.repository.save(rawProject);

            return ResponseEntity.ok(rawProject);
        }
        return ResponseEntity.notFound().build();
    }
}
