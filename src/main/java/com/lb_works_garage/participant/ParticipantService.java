package com.lb_works_garage.participant;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class ParticipantService {
    public void registerParticipantsToProject(List<String> participantsToInvite, UUID projetcId){}

    public void triggerConfirmationEmailToParticipants(UUID projectId){}
}
