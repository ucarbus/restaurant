package com.wolt.restaurant.domain.service;

import com.wolt.restaurant.domain.model.ScheduleCommand;
import com.wolt.restaurant.domain.service.rules.ScheduleRuleInterface;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.DayOfWeek;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ReadableOpeningHoursServiceTest {

    @InjectMocks
    private ReadableOpeningHoursService readableOpeningHoursService;

    @Mock
    private ScheduleRuleInterface scheduleRuleInterface;

    @Test
    public void should_derive_readable_opening_hours() {
        // given
        List<ScheduleCommand> days = generateScheduleCommands();

        when(scheduleRuleInterface.apply(any(ScheduleCommand.class))).thenReturn("Opening Hours");

        // when
        String readableOpeningHours = readableOpeningHoursService.deriveReadableOpeningHours(days);

        // then
        String expectedReadableOpeningHours = "Opening Hours\nOpening Hours\nOpening Hours\n" +
                "Opening Hours\nOpening Hours\nOpening Hours\nOpening Hours";

        verify(scheduleRuleInterface, times(7)).apply(any(ScheduleCommand.class));
        assertEquals(expectedReadableOpeningHours, readableOpeningHours);
    }

    private List<ScheduleCommand> generateScheduleCommands() {
        return Arrays.asList(
                createScheduleCommand(DayOfWeek.MONDAY),
                createScheduleCommand(DayOfWeek.TUESDAY),
                createScheduleCommand(DayOfWeek.WEDNESDAY),
                createScheduleCommand(DayOfWeek.THURSDAY),
                createScheduleCommand(DayOfWeek.FRIDAY),
                createScheduleCommand(DayOfWeek.SATURDAY),
                createScheduleCommand(DayOfWeek.SUNDAY)
        );
    }

    private ScheduleCommand createScheduleCommand(DayOfWeek dayOfWeek) {
        return new ScheduleCommand(dayOfWeek, Collections.emptyList(), Collections.emptyList());
    }

}
