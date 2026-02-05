package com.lb_works_garage.participant;

import com.lb_works_garage.exceptions.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.format.DateTimeParseException;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/participant")
public class ParticipantController {
    @Autowired
    private ParticipantRepository repository;


    @PostMapping("/{id}/confirm")
    public ResponseEntity<Participant> confirmParticipants (@PathVariable UUID id, @RequestBody ParticipantRequestPayload payload){
        Participant participants = this.repository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Participants not found"));

        participants.setIsConfirmed(true);
        participants.setName(payload.name());
        participants.setFunction(payload.function());

        this.repository.save(participants);


        return ResponseEntity.ok(participants);
    }

}
