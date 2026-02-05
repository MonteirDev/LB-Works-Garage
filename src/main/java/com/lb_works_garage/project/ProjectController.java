package com.lb_works_garage.project;

import com.lb_works_garage.activities.ActivityData;
import com.lb_works_garage.activities.ActivityRequestPayload;
import com.lb_works_garage.activities.ActivityResponse;
import com.lb_works_garage.activities.ActivityService;
import com.lb_works_garage.exceptions.ActivityOutsideProjectWindowException;
import com.lb_works_garage.exceptions.InvalidRequestException;
import com.lb_works_garage.exceptions.ResourceNotFoundException;
import com.lb_works_garage.participant.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/projects")
public class ProjectController {

    @Autowired
    private ActivityService activityService;

    @Autowired
    private ParticipantService participantService;

    @Autowired
    private ProjectService projectService;

    @Autowired
    private ProjectRepository repository;

    @PostMapping
    public ResponseEntity<ProjectCreateResponse> createProject(@RequestBody ProjectRequestPayload payload){
        Project newProject = new Project(payload);
        LocalDateTime start = LocalDateTime.parse(payload.starts_at(), DateTimeFormatter.ISO_DATE_TIME);
        LocalDateTime end = LocalDateTime.parse(payload.ends_at(), DateTimeFormatter.ISO_DATE_TIME);

        if (!projectService.validationDateProject(start, end)){
            throw new InvalidRequestException("Date invalid");
        }

        this.repository.save(newProject);
        this.participantService.registerParticipantsToProject(payload.emails_to_invite(), newProject);

        return ResponseEntity.ok(new ProjectCreateResponse(newProject.getId()));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Project> getProjectDetails(@PathVariable UUID id){
        Project project = this.repository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Project not found!"));
        return ResponseEntity.ok(project);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Project> updateProject(@PathVariable UUID id, @RequestBody ProjectRequestPayload payload){
        Project project = this.repository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Project not found"));
        LocalDateTime start = LocalDateTime.parse(payload.starts_at(), DateTimeFormatter.ISO_DATE_TIME);
        LocalDateTime end = LocalDateTime.parse(payload.ends_at(), DateTimeFormatter.ISO_DATE_TIME);

        if (!projectService.validationDateProject(start, end)){
            throw new InvalidRequestException("End date must be after start date.");
        }
        project.setStartsAt(start);
        project.setEndsAt(end);
        project.setCarModel(payload.car_model());

        this.repository.save(project);
        return ResponseEntity.ok(project);
    }

    @GetMapping("/{id}/confirm")
    public ResponseEntity<Project> confirmProject(@PathVariable UUID id){
        Project project = this.repository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Project not found"));
        if (project.getIsConfirmerd()){
            throw new InvalidRequestException("This project is already confirmed");
        }
        project.setIsConfirmerd(true);

        this.repository.save(project);
        this.participantService.triggerConfirmationEmailToParticipants(id);
        return ResponseEntity.ok(project);
    }

    @PostMapping("/{id}/invite")
    public ResponseEntity<ParticipantCreateResponse> inviteParticipant(@PathVariable UUID id, @RequestBody ParticipantRequestPayload payload){
        Project project = this.repository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Project not found"));

        ParticipantCreateResponse participantResponse = this.participantService.registerParticipantToEvent(payload.email(),  project);

        if (project.getIsConfirmerd()) this.participantService.triggerConfirmationEmailToParticipant(payload.email());

        return ResponseEntity.ok(participantResponse);
    }

    @GetMapping("/{id}/participants")
    public ResponseEntity<List<ParticipantData>> getAllParticipants(@PathVariable UUID id){
        List<ParticipantData> participantList = this.participantService.getAllParticipantsFromEvents(id);

        return ResponseEntity.ok(participantList);
    }

    @PostMapping("/{id}/activities")
    public ResponseEntity<ActivityResponse> RegisterActivity(@PathVariable UUID id, @RequestBody ActivityRequestPayload payload){
        Project project = this.repository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Project not found"));
        LocalDateTime dateActivity = LocalDateTime.parse(payload.occurs_at(), DateTimeFormatter.ISO_DATE_TIME);

        if (!activityService.validationDateActivity(project.getStartsAt(), project.getEndsAt(), dateActivity)){
            throw new ActivityOutsideProjectWindowException("The activity date is outside the project's timeframe");
        }
        ActivityResponse activityResponse = this.activityService.RegisterActivity(payload, project);
        return ResponseEntity.ok(activityResponse);

    }
    @GetMapping("/{id}/activities")
    public ResponseEntity<List<ActivityData>> getAllActivities(@PathVariable UUID id){
        List<ActivityData> activityDataList = this.activityService.getAllActivitiesFromId(id);

        return ResponseEntity.ok(activityDataList);
    }

}
