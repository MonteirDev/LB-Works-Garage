package com.lb_works_garage.participant;

import java.util.UUID;

public record ParticipantData(UUID id, String name, String email, String function, boolean isConfirmed) {

}
