package com.wolt.restaurant.infrastructure.rest;

import com.wolt.restaurant.application.controller.RestaurantOpeningHoursController;
import com.wolt.restaurant.application.manager.OpeningHoursManager;
import com.wolt.restaurant.application.model.request.ScheduleRequest;
import com.wolt.restaurant.application.model.response.OpeningHoursResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/api/opening-hours")
@RequiredArgsConstructor
public class RestRestaurantOpeningHoursController implements RestaurantOpeningHoursController {

    private final OpeningHoursManager openingHoursManager;

    @PostMapping()
    public ResponseEntity<OpeningHoursResponse> retrieveReadableOpeningHours(@RequestBody ScheduleRequest scheduleRequest) {
        OpeningHoursResponse response = openingHoursManager.generateOpeningHours(scheduleRequest);
        return ResponseEntity.ok(response);
    }
}