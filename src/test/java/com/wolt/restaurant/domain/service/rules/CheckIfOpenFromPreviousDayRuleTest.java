package com.wolt.restaurant.domain.service.rules;

import com.wolt.restaurant.application.model.request.RestaurantAvailability;
import com.wolt.restaurant.domain.model.RestaurantSchedule;
import com.wolt.restaurant.domain.model.ScheduleCommand;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.DayOfWeek;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CheckIfOpenFromPreviousDayRuleTest {

    @InjectMocks
    private CheckIfOpenFromPreviousDayRule checkIfOpenFromPreviousDayRule;

    @Mock
    private ScheduleRuleInterface nextRule;

    @Test
    public void should_apply_next_rule_when_restaurant_is_already_closed_from_previous_day() {
        // given
        String expectedResult = "Next Rule Applied";
        ScheduleCommand scheduleCommand = createScheduleCommandForRestaurantIsAlreadyClosedFromPreviousDay();
        when(nextRule.apply(scheduleCommand)).thenReturn(expectedResult);

        // when
        String result = checkIfOpenFromPreviousDayRule.apply(scheduleCommand);

        // then
        verify(nextRule).apply(scheduleCommand);
        assertEquals(expectedResult, result);
    }

    @Test
    public void should_apply_next_rule_with_modified_schedule_when_still_open_from_previous_day() {
        // given
        String expectedResult = "Next Rule Applied";
        ScheduleCommand scheduleCommand = createScheduleCommandForRestaurantIsStillOpenFromPreviousDay();

        ScheduleCommand expectedScheduleCommand = toDayEventExcludingPreviousDay(scheduleCommand);

        when(nextRule.apply(expectedScheduleCommand)).thenReturn(expectedResult);

        // when
        String result = checkIfOpenFromPreviousDayRule.apply(scheduleCommand);

        // then
        assertEquals(expectedResult, result);
    }

    private ScheduleCommand createScheduleCommandForRestaurantIsAlreadyClosedFromPreviousDay() {
        List<RestaurantSchedule> schedule = new ArrayList<>();
        schedule.add(new RestaurantSchedule(RestaurantAvailability.OPEN, 37800));
        schedule.add(new RestaurantSchedule(RestaurantAvailability.CLOSE, 64800));
        return new ScheduleCommand(DayOfWeek.MONDAY, schedule, Collections.emptyList());
    }

    private ScheduleCommand createScheduleCommandForRestaurantIsStillOpenFromPreviousDay() {
        List<RestaurantSchedule> schedule = new ArrayList<>();
        schedule.add(new RestaurantSchedule(RestaurantAvailability.CLOSE, 3600));
        schedule.add(new RestaurantSchedule(RestaurantAvailability.OPEN, 36000));
        return new ScheduleCommand(DayOfWeek.MONDAY, schedule, Collections.emptyList());
    }

    private ScheduleCommand toDayEventExcludingPreviousDay(ScheduleCommand scheduleCommand) {
        List<RestaurantSchedule> schedules = scheduleCommand.getSchedule();
        List<RestaurantSchedule> schedulesExcludingPreviousDay = schedules.subList(1, schedules.size());
        return new ScheduleCommand(scheduleCommand.getDay(), schedulesExcludingPreviousDay, scheduleCommand.getScheduleOfNextDay());
    }
}
