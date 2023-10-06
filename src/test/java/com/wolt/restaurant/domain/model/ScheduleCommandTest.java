package com.wolt.restaurant.domain.model;

import com.wolt.restaurant.application.model.request.RestaurantAvailability;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.DayOfWeek;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(MockitoExtension.class)
public class ScheduleCommandTest {

    @ParameterizedTest
    @MethodSource("scheduleCommandsProviderForRestaurantClosedAllDay")
    public void should_correctly_determine_closed_for_all_day_status(ScheduleCommand scheduleCommand, boolean expected) {
        // when
        Boolean result = scheduleCommand.isClosedForAllDay();

        // then
        assertThat(result).isEqualTo(expected);
    }

    @Test
    public void should_get_display_name_for_the_day() {
        //given
        ScheduleCommand scheduleCommand = createClosedForAllDayScheduleCommand();

        //when
        String displayName = scheduleCommand.getDisplayName();

        //then
        assertEquals("Monday", displayName);
    }

    @ParameterizedTest
    @MethodSource("scheduleCommandsProvider")
    public void should_correctly_determine_whether_restaurant_still_open_from_previous_day(ScheduleCommand scheduleCommand, boolean expected) {
        // when
        Boolean result = scheduleCommand.isStillOpenFromPreviousDay();

        // then
        assertThat(result).isEqualTo(expected);
    }

    @ParameterizedTest
    @MethodSource("scheduleCommandsProvider")
    public void should_correctly_determine_whether_restaurant_closing_next_day(ScheduleCommand scheduleCommand, boolean expected) {
        // when
        Boolean result = scheduleCommand.isRestaurantClosingNextDay();

        // then
        assertThat(result).isEqualTo(expected);
    }

    @ParameterizedTest
    @MethodSource("scheduleCommandsProvider")
    public void should_correctly_determine_whether_restaurant_still_open_at_the_end_of_the_day(ScheduleCommand scheduleCommand, boolean expected) {
        // when
        Boolean result = scheduleCommand.isRestaurantStillOpenAtTheEndOfTheDay();

        // then
        assertThat(result).isEqualTo(expected);
    }

    @ParameterizedTest
    @MethodSource("scheduleCommandsProviderForRestaurantClosedAtTheEndOfTheDay")
    public void should_correctly_determine_whether_restaurant_closed_at_the_end_of_the_day(ScheduleCommand scheduleCommand, boolean expected) {
        // when
        Boolean result = scheduleCommand.isRestaurantClosedAtTheEndOfTheDay();

        // then
        assertThat(result).isEqualTo(expected);
    }

    @Test
    public void should_get_open_schedules() {
        //given
        ScheduleCommand scheduleCommand = createScheduleCommand();

        //when
        List<RestaurantSchedule> openSchedules = scheduleCommand.getOpenSchedules();

        //then
        boolean allOpeningStatus = openSchedules.stream()
                .allMatch(schedule -> RestaurantAvailability.OPEN.equals(schedule.getRestaurantAvailability()));

        assertTrue(allOpeningStatus);
    }

    @Test
    public void should_get_closed_schedules() {
        //given
        ScheduleCommand scheduleCommand = createScheduleCommand();

        //when
        List<RestaurantSchedule> openSchedules = scheduleCommand.getCloseSchedules();

        //then
        boolean allClosingStatus = openSchedules.stream()
                .allMatch(schedule -> RestaurantAvailability.CLOSE.equals(schedule.getRestaurantAvailability()));

        assertTrue(allClosingStatus);
    }

    private static Object[] scheduleCommandsProviderForRestaurantClosedAllDay() {
        return new Object[]{
                new Object[]{createClosedForAllDayScheduleCommand(), true},
                new Object[]{createScheduleCommand(), false}
        };
    }

    private static Object[] scheduleCommandsProvider() {
        return new Object[]{
                new Object[]{createClosedForAllDayScheduleCommand(), false},
                new Object[]{createScheduleCommand(), true}
        };
    }

    private static Object[] scheduleCommandsProviderForRestaurantClosedAtTheEndOfTheDay() {
        return new Object[]{
                new Object[]{createScheduleCommand(), false},
                new Object[]{createScheduleCommandForRestaurantClosedAtTheEndOfTheDay(), true}
        };
    }

    private static ScheduleCommand createClosedForAllDayScheduleCommand() {
        return new ScheduleCommand(DayOfWeek.MONDAY, Collections.emptyList(), Collections.emptyList());
    }

    private static ScheduleCommand createScheduleCommand() {
        List<RestaurantSchedule> schedule = new ArrayList<>();
        schedule.add(new RestaurantSchedule(RestaurantAvailability.CLOSE, 36000));
        schedule.add(new RestaurantSchedule(RestaurantAvailability.OPEN, 64800));

        List<RestaurantSchedule> nextDaySchedule = new ArrayList<>();
        nextDaySchedule.add(new RestaurantSchedule(RestaurantAvailability.CLOSE, 36000));
        nextDaySchedule.add(new RestaurantSchedule(RestaurantAvailability.OPEN, 64800));

        return new ScheduleCommand(DayOfWeek.MONDAY, schedule, nextDaySchedule);
    }

    private static ScheduleCommand createScheduleCommandForRestaurantClosedAtTheEndOfTheDay() {
        List<RestaurantSchedule> schedule = new ArrayList<>();
        schedule.add(new RestaurantSchedule(RestaurantAvailability.CLOSE, 36000));

        return new ScheduleCommand(DayOfWeek.MONDAY, schedule, Collections.emptyList());
    }
}