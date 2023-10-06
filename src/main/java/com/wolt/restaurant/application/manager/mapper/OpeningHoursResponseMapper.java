package com.wolt.restaurant.application.manager.mapper;

import com.wolt.restaurant.application.model.response.OpeningHoursResponse;
import com.wolt.restaurant.domain.model.enumtype.ResponseStatusType;
import org.springframework.stereotype.Component;

@Component
public class OpeningHoursResponseMapper {

    public OpeningHoursResponse mapAndReturnSuccessResponse(String formattedOpeningHours) {
        OpeningHoursResponse response = new OpeningHoursResponse();
        response.setStatus(ResponseStatusType.SUCCESS.getValue());
        response.setFormattedOpeningHours(formattedOpeningHours);
        return response;
    }
}
