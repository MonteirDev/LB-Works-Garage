package com.lb_works_garage.participant;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/participant")
public class ParticipantController {
    @Autowired
    private ParticipantRepository repository;


    @PostMapping("/{id}/confirm")
    public ResponseEntity<Participant> confirmParticipants (@PathVariable UUID id, @RequestBody ParticipantRequestPayload payload){
        Optional<Participant> participants = this.repository.findById(id);

        if (participants.isPresent()){
            Participant rawParticipant = participants.get();
            rawParticipant.setIsConfirmed(true);
            rawParticipant.setName(payload.name());
            rawParticipant.setFunction(payload.function());

            this.repository.save(rawParticipant);


            return ResponseEntity.ok(rawParticipant);
        }
        return ResponseEntity.notFound().build();
    }

}
