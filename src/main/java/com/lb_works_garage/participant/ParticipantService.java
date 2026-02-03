package com.lb_works_garage.participant;

import com.lb_works_garage.project.Project;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class ParticipantService {
    @Autowired
    private ParticipantRepository repository;

    public void registerParticipantsToProject(List<String> participantsToInvite, Project project){
        List<Participant> participants = participantsToInvite.stream().map(email -> new Participant(email, project)).toList();

        this.repository.saveAll(participants);
    }

    public void triggerConfirmationEmailToParticipants(UUID projectId){}

    public void triggerConfirmationEmailToParticipant(String email){}

    public ParticipantCreateResponse registerParticipantToEvent(String email, Project project){
        Participant newParticipant = new Participant(email, project);
        this.repository.save(newParticipant);

        return new ParticipantCreateResponse(newParticipant.getId());
    }

    public List<ParticipantData> getAllParticipantsFromEvents (UUID projectId){
        return this.repository.findByProjectId(projectId).stream().map(participant -> new ParticipantData(participant.getId(), participant.getName(), participant.getEmail(), participant.getFunction(), participant.getIsConfirmed())).toList();
    }
}


