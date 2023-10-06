package com.wolt.restaurant.application.model.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RestaurantScheduleRequest {
    @JsonProperty("type")
    private RestaurantAvailability restaurantAvailability;
    @JsonProperty("value")
    private Integer unixTimeInSeconds;
}