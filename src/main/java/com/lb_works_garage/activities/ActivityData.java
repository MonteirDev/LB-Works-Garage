package com.lb_works_garage.activities;

import java.time.LocalDateTime;
import java.util.UUID;

public record ActivityData(UUID id, String title, LocalDateTime ocuurs_at) {
}
