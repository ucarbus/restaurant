package com.wolt.restaurant.infrastructure.rest;

import com.wolt.restaurant.application.model.request.RestaurantAvailability;
import com.wolt.restaurant.application.model.request.RestaurantScheduleRequest;
import com.wolt.restaurant.application.model.request.ScheduleRequest;
import com.wolt.restaurant.application.model.response.OpeningHoursResponse;
import com.wolt.restaurant.base.BaseWebIT;
import org.junit.jupiter.api.Test;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static com.wolt.restaurant.domain.model.enumtype.ResponseStatusType.FAILURE;
import static com.wolt.restaurant.domain.model.enumtype.ResponseStatusType.SUCCESS;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.springframework.http.HttpStatus.OK;
import static org.springframework.http.HttpStatus.UNPROCESSABLE_ENTITY;

class RestRestaurantOpeningHoursControllerIT extends BaseWebIT {

    @Test
    public void should_retrieve_readable_opening_hours() {
        ScheduleRequest request = createScheduleRequest();

        ResponseEntity<OpeningHoursResponse> responseEntity = testRestTemplate.postForEntity(
                "/api/opening-hours", request, OpeningHoursResponse.class);

        assertEquals(OK, responseEntity.getStatusCode());

        String expectedResult = "Monday: CLOSED\n" +
                "Tuesday: 10 AM - 6 PM\n" +
                "Wednesday: CLOSED\n" +
                "Thursday: 10:30 AM - 6 PM\n" +
                "Friday: 10 AM - 1 AM\n" +
                "Saturday: 10 AM - 1 AM\n" +
                "Sunday: 12 PM - 9 PM";

        OpeningHoursResponse response = responseEntity.getBody();
        assertNotNull(response);
        assertEquals(SUCCESS.getValue(), response.getStatus());
        assertEquals(expectedResult, response.getFormattedOpeningHours());
        assertNull(response.getErrorMessage());
    }

    @Test
    public void should_return_http_status_as_unprocessable_entity_when_input_is_invalid() {
        //given
        //Given input file is missing schedule information for one or more days
        ScheduleRequest request = new ScheduleRequest();

        ResponseEntity<OpeningHoursResponse> responseEntity = testRestTemplate.postForEntity(
                "/api/opening-hours", request, OpeningHoursResponse.class);

        assertEquals(UNPROCESSABLE_ENTITY, responseEntity.getStatusCode());

        OpeningHoursResponse response = responseEntity.getBody();
        assertEquals(FAILURE.getValue(), response.getStatus());
        assertEquals("Validation failed: Given input file is missing schedule information for one or more days!", response.getErrorMessage());
    }

    private ScheduleRequest createScheduleRequest() {
        ScheduleRequest scheduleRequest = new ScheduleRequest();

        scheduleRequest.setScheduleForMonday(Collections.emptyList());

        scheduleRequest.setScheduleForTuesday(Arrays.asList(
                createRestaurantScheduleRequest(RestaurantAvailability.OPEN, 36000),
                createRestaurantScheduleRequest(RestaurantAvailability.CLOSE, 64800)
        ));

        scheduleRequest.setScheduleForWednesday(Collections.emptyList());

        scheduleRequest.setScheduleForThursday(Arrays.asList(
                createRestaurantScheduleRequest(RestaurantAvailability.OPEN, 37800),
                createRestaurantScheduleRequest(RestaurantAvailability.CLOSE, 64800)
        ));

        scheduleRequest.setScheduleForFriday(List.of(
                createRestaurantScheduleRequest(RestaurantAvailability.OPEN, 36000)
        ));

        scheduleRequest.setScheduleForSaturday(Arrays.asList(
                createRestaurantScheduleRequest(RestaurantAvailability.CLOSE, 3600),
                createRestaurantScheduleRequest(RestaurantAvailability.OPEN, 36000)
        ));

        scheduleRequest.setScheduleForSunday(Arrays.asList(
                createRestaurantScheduleRequest(RestaurantAvailability.CLOSE, 3600),
                createRestaurantScheduleRequest(RestaurantAvailability.OPEN, 43200),
                createRestaurantScheduleRequest(RestaurantAvailability.CLOSE, 75600)

        ));

        return scheduleRequest;
    }

    private RestaurantScheduleRequest createRestaurantScheduleRequest(RestaurantAvailability availability, int unixTimeInSeconds) {
        RestaurantScheduleRequest scheduleRequest = new RestaurantScheduleRequest();
        scheduleRequest.setRestaurantAvailability(availability);
        scheduleRequest.setUnixTimeInSeconds(unixTimeInSeconds);
        return scheduleRequest;
    }
}