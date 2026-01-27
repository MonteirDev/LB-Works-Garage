package com.lb_works_garage.activities;

import com.lb_works_garage.participant.ParticipantData;
import com.lb_works_garage.project.Project;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class ActivityService {

    @Autowired
    private ActivityRepository repository;
    public ActivityResponse RegisterActtivity(ActivityRequestPayload payload, Project project){
        Activity newActivity = new Activity(payload.title(), payload.occurs_at(), project);

        this.repository.save(newActivity);

        return new ActivityResponse(newActivity.getId());
    }

    public List<ActivityData> getAllActivitiesFromId(UUID projectId){
        return this.repository.findByProjectId(projectId).stream().map(activity -> new ActivityData(activity.getId(), activity.getTitle(), activity.getOccursAt())).toList();
    }
}
