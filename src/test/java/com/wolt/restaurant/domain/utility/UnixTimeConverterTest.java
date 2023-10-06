package com.wolt.restaurant.domain.utility;

import com.wolt.restaurant.application.model.request.RestaurantAvailability;
import com.wolt.restaurant.domain.model.RestaurantSchedule;
import com.wolt.restaurant.domain.model.ScheduleCommand;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.DayOfWeek;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
class UnixTimeConverterTest {

    @Test
    public void should_convert_to_readable_time() {
        //given
        String expectedResult = "Monday: 9 AM - 11 AM, 4 PM - 11 PM";
        ScheduleCommand scheduleCommand = createScheduleCommand();

        // when
        String result = UnixTimeConverter.convertToReadableTime(scheduleCommand);

        // then
        assertThat(result).isNotNull();
        assertEquals(expectedResult, result);
    }

    private ScheduleCommand createScheduleCommand() {
        List<RestaurantSchedule> schedule = new ArrayList<>();
        schedule.add(new RestaurantSchedule(RestaurantAvailability.OPEN, 32400));
        schedule.add(new RestaurantSchedule(RestaurantAvailability.CLOSE, 39600));
        schedule.add(new RestaurantSchedule(RestaurantAvailability.OPEN, 57600));
        schedule.add(new RestaurantSchedule(RestaurantAvailability.CLOSE, 82800));
        return new ScheduleCommand(DayOfWeek.valueOf("Monday".toUpperCase()), schedule, Collections.emptyList());
    }
}