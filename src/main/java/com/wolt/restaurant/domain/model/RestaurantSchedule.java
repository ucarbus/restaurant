package com.wolt.restaurant.domain.model;

import com.wolt.restaurant.application.model.request.RestaurantAvailability;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@RequiredArgsConstructor
public class RestaurantSchedule {
    private final RestaurantAvailability restaurantAvailability;
    private final Integer unixTimeInSeconds;
}