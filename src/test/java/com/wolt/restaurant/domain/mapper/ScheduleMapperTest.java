package com.wolt.restaurant.domain.mapper;

import com.wolt.restaurant.application.model.request.RestaurantAvailability;
import com.wolt.restaurant.application.model.request.RestaurantScheduleRequest;
import com.wolt.restaurant.application.model.request.ScheduleRequest;
import com.wolt.restaurant.domain.model.ScheduleCommand;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.DayOfWeek;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

@ExtendWith(MockitoExtension.class)
class ScheduleMapperTest {

    @InjectMocks
    private ScheduleMapper scheduleMapper;

    @Test
    public void should_map_to_schedule_commands() {
        // given
        ScheduleRequest scheduleRequest = createScheduleRequest();

        // when
        List<ScheduleCommand> scheduleCommands = scheduleMapper.mapToScheduleCommands(scheduleRequest);

        // then
        assertEquals(7, scheduleCommands.size());

        assertEquals(DayOfWeek.MONDAY, scheduleCommands.get(0).getDay());
        assertEquals(0, scheduleCommands.get(0).getSchedule().size());
        assertEquals(2, scheduleCommands.get(0).getScheduleOfNextDay().size());

        assertEquals(DayOfWeek.TUESDAY, scheduleCommands.get(1).getDay());
        assertEquals(2, scheduleCommands.get(1).getSchedule().size());
        assertNull(scheduleCommands.get(1).getScheduleOfNextDay());

        assertEquals(DayOfWeek.WEDNESDAY, scheduleCommands.get(2).getDay());
        assertNull(scheduleCommands.get(2).getSchedule());
        assertEquals(2, scheduleCommands.get(2).getScheduleOfNextDay().size());

        assertEquals(DayOfWeek.THURSDAY, scheduleCommands.get(3).getDay());
        assertEquals(2, scheduleCommands.get(3).getSchedule().size());
        assertEquals(1, scheduleCommands.get(3).getScheduleOfNextDay().size());

        assertEquals(DayOfWeek.FRIDAY, scheduleCommands.get(4).getDay());
        assertEquals(1, scheduleCommands.get(4).getSchedule().size());
        assertEquals(2, scheduleCommands.get(4).getScheduleOfNextDay().size());

        assertEquals(DayOfWeek.SATURDAY, scheduleCommands.get(5).getDay());
        assertEquals(2, scheduleCommands.get(5).getSchedule().size());
        assertEquals(3, scheduleCommands.get(5).getScheduleOfNextDay().size());

        assertEquals(DayOfWeek.SUNDAY, scheduleCommands.get(6).getDay());
        assertEquals(3, scheduleCommands.get(6).getSchedule().size());
        assertEquals(0, scheduleCommands.get(6).getScheduleOfNextDay().size());
    }

    private ScheduleRequest createScheduleRequest() {
        ScheduleRequest scheduleRequest = new ScheduleRequest();

        // Day is provided, but no schedule is given
        scheduleRequest.setScheduleForMonday(Collections.emptyList());

        scheduleRequest.setScheduleForTuesday(Arrays.asList(
                createRestaurantScheduleRequest(RestaurantAvailability.OPEN, 36000),
                createRestaurantScheduleRequest(RestaurantAvailability.CLOSE, 64800)
        ));

        // Day is not provided in json document
        scheduleRequest.setScheduleForWednesday(null);

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
