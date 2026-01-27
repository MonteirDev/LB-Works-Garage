package com.lb_works_garage.project;

import com.lb_works_garage.activities.ActivityData;
import com.lb_works_garage.activities.ActivityRequestPayload;
import com.lb_works_garage.activities.ActivityResponse;
import com.lb_works_garage.activities.ActivityService;
import com.lb_works_garage.participant.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/projects")
public class ProjectController {

    @Autowired
    private ActivityService activityService;

    @Autowired
    private ParticipantService participantService;

    @Autowired
    private ProjectRepository repository;

    @PostMapping
    public ResponseEntity<ProjectCreateResponse> createProject(@RequestBody ProjectRequestPayload payload){
        Project newProject = new Project(payload);

        this.repository.save(newProject);
        this.participantService.registerParticipantsToProject(payload.emails_to_invite(), newProject);

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

    @GetMapping("/{id}/confirm")
    public ResponseEntity<Project> confirmProject(@PathVariable UUID id){
        Optional<Project> project = this.repository.findById(id);

        if (project.isPresent()){
            Project rawProject = project.get();
            rawProject.setIsConfirmerd(true);

            this.repository.save(rawProject);
            this.participantService.triggerConfirmationEmailToParticipants(id);

            return ResponseEntity.ok(rawProject);
        }
        return ResponseEntity.notFound().build();
    }

    @PostMapping("/{id}/invite")
    public ResponseEntity<ParticipantCreateResponse> inviteParticipant(@PathVariable UUID id, @RequestBody ParticipantRequestPayload payload){
        Optional<Project> project = this.repository.findById(id);

        if (project.isPresent()){
            Project rawProject = project.get();
            ParticipantCreateResponse participantResponse = this.participantService.registerParticipantToEvent(payload.email(),  rawProject);

            if (rawProject.getIsConfirmerd()) this.participantService.triggerConfirmationEmailToParticipant(payload.email());

            return ResponseEntity.ok(participantResponse);
        }
        return ResponseEntity.notFound().build();
    }

    @GetMapping("/{id}/participants")
    public ResponseEntity<List<ParticipantData>> getAllParticipants(@PathVariable UUID id){
        List<ParticipantData> participantList = this.participantService.getAllParticipantsFromEvents(id);

        return ResponseEntity.ok(participantList);
    }

    @PostMapping("/{id}/activities")
    public ResponseEntity<ActivityResponse> RegisterActivity(@PathVariable UUID id, @RequestBody ActivityRequestPayload payload){
        Optional<Project> project = this.repository.findById(id);

        if (project.isPresent()){
            Project rawProject = project.get();
            ActivityResponse activityResponse = this.activityService.RegisterActtivity(payload, rawProject);
            return ResponseEntity.ok(activityResponse);
        }
        return ResponseEntity.notFound().build();
    }
    @GetMapping("/{id}/activities")
    public ResponseEntity<List<ActivityData>> getAllActivities(@PathVariable UUID id){
        List<ActivityData> activityDataList = this.activityService.getAllActivitiesFromId(id);

        return ResponseEntity.ok(activityDataList);
    }

}
