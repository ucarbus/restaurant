package com.wolt.restaurant.application.controller;

import com.wolt.restaurant.application.model.request.ScheduleRequest;
import com.wolt.restaurant.application.model.response.OpeningHoursResponse;
import org.springframework.http.ResponseEntity;

public interface RestaurantOpeningHoursController {

    ResponseEntity<OpeningHoursResponse> retrieveReadableOpeningHours(ScheduleRequest openingHours);
}
