package com.wolt.restaurant.application.model.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class ScheduleRequest {
    @JsonProperty("monday")
    private List<RestaurantScheduleRequest> scheduleForMonday;
    @JsonProperty("tuesday")
    private List<RestaurantScheduleRequest> scheduleForTuesday;
    @JsonProperty("wednesday")
    private List<RestaurantScheduleRequest> scheduleForWednesday;
    @JsonProperty("thursday")
    private List<RestaurantScheduleRequest> scheduleForThursday;
    @JsonProperty("friday")
    private List<RestaurantScheduleRequest> scheduleForFriday;
    @JsonProperty("saturday")
    private List<RestaurantScheduleRequest> scheduleForSaturday;
    @JsonProperty("sunday")
    private List<RestaurantScheduleRequest> scheduleForSunday;
}