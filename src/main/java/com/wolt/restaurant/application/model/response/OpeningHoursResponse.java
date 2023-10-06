package com.wolt.restaurant.application.model.response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OpeningHoursResponse extends Response{
    private String formattedOpeningHours;
}