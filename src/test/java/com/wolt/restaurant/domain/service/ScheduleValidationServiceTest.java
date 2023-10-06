package com.wolt.restaurant.domain.service;

import com.wolt.restaurant.application.exception.ValidationException;
import com.wolt.restaurant.domain.model.RestaurantSchedule;
import com.wolt.restaurant.domain.model.ScheduleCommand;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.DayOfWeek;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static com.wolt.restaurant.application.model.request.RestaurantAvailability.CLOSE;
import static com.wolt.restaurant.application.model.request.RestaurantAvailability.OPEN;
import static java.time.DayOfWeek.MONDAY;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;

@ExtendWith(MockitoExtension.class)
class ScheduleValidationServiceTest {

    @InjectMocks
    private ScheduleValidationService scheduleValidationService;

    @Test
    public void should_validate_without_exception() {
        //given
        List<ScheduleCommand> commands = generateScheduleCommands();

        //when
        Throwable throwable = catchThrowable(() -> scheduleValidationService.validate(commands));

        //then
        assertThat(throwable).isNull();
    }

    @Test
    public void should_throw_exception_when_request_has_missing_schedule_information() {
        //given
        List<ScheduleCommand> commands = generateScheduleCommandsWithMissingSchedule();

        //when
        Throwable throwable = catchThrowable(() -> scheduleValidationService.validate(commands));

        //when
        assertThat(throwable).isNotNull()
                .isInstanceOf(ValidationException.class)
                .hasMessage("Validation failed: Given input file is missing schedule information for one or more days!");
    }

    @Test
    public void should_throw_exception_when_restaurant_availability_not_found() {
        //given
        List<ScheduleCommand> commands = generateScheduleCommandForMissingRestaurantAvailability();

        //when
        Throwable throwable = catchThrowable(() -> scheduleValidationService.validate(commands));

        //when
        assertThat(throwable).isNotNull()
                .isInstanceOf(ValidationException.class)
                .hasMessage("Validation failed: Restaurant availability not found");
    }

    @Test
    public void should_throw_exception_when_opening_or_closing_time_not_found() {
        //given
        List<ScheduleCommand> commands = generateScheduleCommandForMissingTime();

        //when
        Throwable throwable = catchThrowable(() -> scheduleValidationService.validate(commands));

        //when
        assertThat(throwable).isNotNull()
                .isInstanceOf(ValidationException.class)
                .hasMessage("Validation failed: Invalid time value(s) found for day: Monday");
    }

    @Test
    public void should_throw_exception_when_restaurant_reopens_while_still_open() {
        //given
        List<ScheduleCommand> commands = generateScheduleCommandForRestaurantReopens();

        //when
        Throwable throwable = catchThrowable(() -> scheduleValidationService.validate(commands));

        //when
        assertThat(throwable).isNotNull()
                .isInstanceOf(ValidationException.class)
                .hasMessage("Validation failed: Restaurant shouldn't be reopened while it is still open for the given day: Monday");
    }

    @Test
    public void should_throw_exception_when_restaurant_is_closing_while_it_is_already_closed() {
        //given
        List<ScheduleCommand> commands = generateScheduleCommandForCloseRestaurantWhileItIsAlreadyClosed();

        //when
        Throwable throwable = catchThrowable(() -> scheduleValidationService.validate(commands));

        //when
        assertThat(throwable).isNotNull()
                .isInstanceOf(ValidationException.class)
                .hasMessage("Validation failed: While the restaurant is closed, it cannot be closed again for given day Monday");
    }

    @Test
    public void should_throw_exception_when_restaurant_is_already_closed_for_given_day_and_next_day_starts_with_close_schedule() {
        //given
        List<ScheduleCommand> commands = generateScheduleCommandStartWithClosingAndPreviousDayEndsNotEndWithOpening();

        //when
        Throwable throwable = catchThrowable(() -> scheduleValidationService.validate(commands));

        //when
        assertThat(throwable).isNotNull()
                .isInstanceOf(ValidationException.class)
                .hasMessage("Validation failed: When restaurant is already closed for the given day, next day cannot start with close schedule!");
    }

    @Test
    public void should_throw_exception_when_restaurant_is_still_open_for_given_day_and_next_day_starts_with_open_schedule() {
        //given
        List<ScheduleCommand> commands = generateScheduleCommandEndWithOpeningAndNextDayStartsWithOpening();

        //when
        Throwable throwable = catchThrowable(() -> scheduleValidationService.validate(commands));

        //when
        assertThat(throwable).isNotNull()
                .isInstanceOf(ValidationException.class)
                .hasMessage("Validation failed: When restaurant is still open for the given day, next day should start with close schedule!");
    }

    @Test
    public void should_throw_exception_when_opening_and_closing_time_are_not_in_order() {
        //given
        List<ScheduleCommand> commands = generateScheduleCommandOpeningAndClosingTimeAreNotInOrder();

        //when
        Throwable throwable = catchThrowable(() -> scheduleValidationService.validate(commands));

        //when
        assertThat(throwable).isNotNull()
                .isInstanceOf(ValidationException.class)
                .hasMessage("Validation failed: Opening and closing times should be in order within the same day.");
    }

    private List<ScheduleCommand> generateScheduleCommands() {
        return Arrays.asList(
                createScheduleCommand(MONDAY),
                createScheduleCommand(DayOfWeek.TUESDAY),
                createScheduleCommand(DayOfWeek.WEDNESDAY),
                createScheduleCommand(DayOfWeek.THURSDAY),
                createScheduleCommand(DayOfWeek.FRIDAY),
                createScheduleCommand(DayOfWeek.SATURDAY),
                createScheduleCommand(DayOfWeek.SUNDAY)
        );
    }

    private ScheduleCommand createScheduleCommand(DayOfWeek dayOfWeek) {
        List<RestaurantSchedule> schedule = new ArrayList<>();
        schedule.add(new RestaurantSchedule(OPEN, 36000));
        schedule.add(new RestaurantSchedule(CLOSE, 64800));
        return new ScheduleCommand(dayOfWeek, schedule, Collections.emptyList());
    }

    private List<ScheduleCommand> generateScheduleCommandsWithMissingSchedule() {
        return Arrays.asList(
                createNullScheduleCommand(MONDAY),
                createScheduleCommand(DayOfWeek.TUESDAY),
                createScheduleCommand(DayOfWeek.WEDNESDAY),
                createScheduleCommand(DayOfWeek.THURSDAY),
                createScheduleCommand(DayOfWeek.FRIDAY),
                createScheduleCommand(DayOfWeek.SATURDAY),
                createScheduleCommand(DayOfWeek.SUNDAY)
        );
    }

    private ScheduleCommand createNullScheduleCommand(DayOfWeek dayOfWeek) {
        return new ScheduleCommand(dayOfWeek, null, Collections.emptyList());
    }

    private List<ScheduleCommand> generateScheduleCommandForMissingRestaurantAvailability() {
        List<RestaurantSchedule> scheduleForMonday = new ArrayList<>();
        scheduleForMonday.add(new RestaurantSchedule(null, 64800));
        scheduleForMonday.add(new RestaurantSchedule(CLOSE, 36000));
        ScheduleCommand scheduleCommandForMonday = new ScheduleCommand(MONDAY, scheduleForMonday, Collections.emptyList());

        return List.of(scheduleCommandForMonday);
    }

    private List<ScheduleCommand> generateScheduleCommandForMissingTime() {
        List<RestaurantSchedule> scheduleForMonday = new ArrayList<>();
        scheduleForMonday.add(new RestaurantSchedule(OPEN, null));
        scheduleForMonday.add(new RestaurantSchedule(CLOSE, 36000));
        ScheduleCommand scheduleCommandForMonday = new ScheduleCommand(MONDAY, scheduleForMonday, Collections.emptyList());

        return List.of(scheduleCommandForMonday);
    }

    private List<ScheduleCommand> generateScheduleCommandStartWithClosingAndPreviousDayEndsNotEndWithOpening() {

        List<RestaurantSchedule> scheduleForTuesday = new ArrayList<>();
        scheduleForTuesday.add(new RestaurantSchedule(CLOSE, 36000));
        ScheduleCommand scheduleCommandForTuesday = new ScheduleCommand(DayOfWeek.TUESDAY, scheduleForTuesday, Collections.emptyList());

        List<RestaurantSchedule> scheduleForMonday = new ArrayList<>();
        scheduleForMonday.add(new RestaurantSchedule(OPEN, 36000));
        scheduleForMonday.add(new RestaurantSchedule(CLOSE, 64800));
        ScheduleCommand scheduleCommandForMonday = new ScheduleCommand(MONDAY, scheduleForMonday, scheduleForTuesday);

        return Arrays.asList(
                scheduleCommandForMonday,
                scheduleCommandForTuesday
        );
    }

    private List<ScheduleCommand> generateScheduleCommandEndWithOpeningAndNextDayStartsWithOpening() {

        List<RestaurantSchedule> scheduleForTuesday = new ArrayList<>();
        scheduleForTuesday.add(new RestaurantSchedule(OPEN, 36000));
        ScheduleCommand scheduleCommandForTuesday = new ScheduleCommand(DayOfWeek.TUESDAY, scheduleForTuesday, Collections.emptyList());

        List<RestaurantSchedule> scheduleForMonday = new ArrayList<>();
        scheduleForMonday.add(new RestaurantSchedule(OPEN, 36000));
        ScheduleCommand scheduleCommandForMonday = new ScheduleCommand(MONDAY, scheduleForMonday, scheduleForTuesday);

        return Arrays.asList(
                scheduleCommandForMonday,
                scheduleCommandForTuesday
        );
    }

    private List<ScheduleCommand> generateScheduleCommandOpeningAndClosingTimeAreNotInOrder() {
        List<RestaurantSchedule> scheduleForMonday = new ArrayList<>();
        scheduleForMonday.add(new RestaurantSchedule(OPEN, 64800));
        scheduleForMonday.add(new RestaurantSchedule(CLOSE, 36000));
        ScheduleCommand scheduleCommandForMonday = new ScheduleCommand(MONDAY, scheduleForMonday, Collections.emptyList());

        return List.of(scheduleCommandForMonday);
    }

    private List<ScheduleCommand> generateScheduleCommandForRestaurantReopens() {
        List<RestaurantSchedule> scheduleForMonday = new ArrayList<>();
        scheduleForMonday.add(new RestaurantSchedule(OPEN, 64800));
        scheduleForMonday.add(new RestaurantSchedule(OPEN, 36000));
        ScheduleCommand scheduleCommandForMonday = new ScheduleCommand(MONDAY, scheduleForMonday, Collections.emptyList());

        return List.of(scheduleCommandForMonday);
    }

    private List<ScheduleCommand> generateScheduleCommandForCloseRestaurantWhileItIsAlreadyClosed() {
        List<RestaurantSchedule> scheduleForMonday = new ArrayList<>();
        scheduleForMonday.add(new RestaurantSchedule(CLOSE, 64800));
        scheduleForMonday.add(new RestaurantSchedule(CLOSE, 36000));
        ScheduleCommand scheduleCommandForMonday = new ScheduleCommand(MONDAY, scheduleForMonday, Collections.emptyList());

        return List.of(scheduleCommandForMonday);
    }
}