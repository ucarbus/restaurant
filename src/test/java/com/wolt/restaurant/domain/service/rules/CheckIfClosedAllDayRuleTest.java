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
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CheckIfClosedAllDayRuleTest {

    @InjectMocks
    private CheckIfClosedAllDayRule checkIfClosedAllDayRule;

    @Mock
    private ScheduleRuleInterface nextRule;

    @Test
    public void should_return_closed_when_schedule_command_is_closed_for_all_day() {
        // given
        String expectedResult = "Monday: CLOSED";
        ScheduleCommand scheduleCommand = createClosedForAllDayScheduleCommand();

        // when
        String result = checkIfClosedAllDayRule.apply(scheduleCommand);

        // then
        assertEquals(expectedResult, result);
        verifyNoInteractions(nextRule);
    }

    @Test
    public void should_delegate_to_next_rule_when_schedule_command_is_not_closed_for_all_day() {
        // given
        String expectedResult = "Next Rule Running";
        ScheduleCommand scheduleCommand = createOpenScheduleCommand();
        when(nextRule.apply(scheduleCommand)).thenReturn("Next Rule Running");

        // when
        String result = checkIfClosedAllDayRule.apply(scheduleCommand);

        // then
        assertEquals(expectedResult, result);
        verify(nextRule).apply(scheduleCommand);
    }

    private ScheduleCommand createClosedForAllDayScheduleCommand() {
        return new ScheduleCommand(DayOfWeek.MONDAY, Collections.emptyList(), Collections.emptyList());
    }

    private ScheduleCommand createOpenScheduleCommand() {
        List<RestaurantSchedule> schedule = new ArrayList<>();
        schedule.add(new RestaurantSchedule(RestaurantAvailability.OPEN, 36000));
        schedule.add(new RestaurantSchedule(RestaurantAvailability.CLOSE, 64800));
        return new ScheduleCommand(DayOfWeek.MONDAY, schedule, Collections.emptyList());
    }
}
