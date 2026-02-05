package com.lb_works_garage.exceptions;

import java.time.LocalDateTime;

public record ErrorResponde(String message, LocalDateTime timestamp) {
}
