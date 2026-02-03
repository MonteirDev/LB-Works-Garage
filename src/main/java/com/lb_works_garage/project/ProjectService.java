package com.lb_works_garage.project;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class ProjectService {

    public static boolean validationDateProject(LocalDateTime StartAt, LocalDateTime EndsAt){
        return EndsAt.isAfter(StartAt);
    }
}
