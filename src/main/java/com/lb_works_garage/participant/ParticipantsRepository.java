package com.lb_works_garage.participant;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface ParticipantsRepository extends JpaRepository<Participants, UUID> {
}
