package com.wolt.restaurant.domain.service.rules;

import com.wolt.restaurant.application.model.request.RestaurantAvailability;
import com.wolt.restaurant.domain.model.RestaurantSchedule;
import com.wolt.restaurant.domain.model.ScheduleCommand;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.DayOfWeek;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
class HumanReadableTimeConversionRuleTest {

    @InjectMocks
    private HumanReadableTimeConversionRule humanReadableTimeConversionRule;

    @Test
    public void should_return_readable_time_when_applying_rule() {
        // given
        ScheduleCommand scheduleCommand = createScheduleCommand();

        // when
        String result = humanReadableTimeConversionRule.apply(scheduleCommand);

        // then
        assertEquals("Monday: 10 AM - 6 PM", result);
    }

    private ScheduleCommand createScheduleCommand() {
        List<RestaurantSchedule> schedule = new ArrayList<>();
        schedule.add(new RestaurantSchedule(RestaurantAvailability.OPEN, 36000));
        schedule.add(new RestaurantSchedule(RestaurantAvailability.CLOSE, 64800));
        return new ScheduleCommand(DayOfWeek.MONDAY, schedule, Collections.emptyList());
    }
}
