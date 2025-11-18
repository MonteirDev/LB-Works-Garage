package com.lb_works_garage.project;

import java.util.List;

public record ProjectRequestPayload(String car_model, String starts_at, String ends_at, List<String> emails_to_invite, String client_email, String client_name) {
}
