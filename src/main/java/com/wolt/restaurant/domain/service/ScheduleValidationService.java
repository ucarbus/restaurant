package com.wolt.restaurant.domain.service;

import com.wolt.restaurant.application.exception.ValidationException;
import com.wolt.restaurant.application.model.request.RestaurantAvailability;
import com.wolt.restaurant.domain.model.RestaurantSchedule;
import com.wolt.restaurant.domain.model.ScheduleCommand;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.function.Predicate;
import java.util.stream.IntStream;

@Service
@Slf4j
public class ScheduleValidationService {

    public void validate(List<ScheduleCommand> commands) {
        validateAllDaysIncluded(commands);
        validateRestaurantAvailability(commands);
        validateInvalidTimeValues(commands);
        validateOpeningClosingSequence(commands);
    }

    private void validateAllDaysIncluded(List<ScheduleCommand> commands) {
        boolean isValid = commands.stream()
                .allMatch(command -> command.getSchedule() != null);

        if (!isValid) {
            throw new ValidationException("Validation failed: Given input file is missing schedule information for one or more days!");
        }
    }

    private void validateRestaurantAvailability(List<ScheduleCommand> commands) {
        boolean hasNullAvailability = commands.stream()
                .flatMap(command -> command.getSchedule().stream())
                .anyMatch(schedule -> schedule.getRestaurantAvailability() == null);

        if (hasNullAvailability) {
            throw new ValidationException("Validation failed: Restaurant availability not found");
        }
    }


    private void validateOpeningClosingSequence(List<ScheduleCommand> commands) {
        commands.forEach(this::validateScheduleAvailabilitySequence);

        commands.stream()
                .filter(c -> c.isRestaurantClosingNextDay() && c.isRestaurantClosedAtTheEndOfTheDay())
                .findFirst()
                .ifPresent(cmd -> {
                    log.error("The day cannot start with a closing time if the previous day had no opening time for given day: {}", cmd.getDay());
                    throw new ValidationException("Validation failed: When restaurant is already closed for the given day, next day cannot start with close schedule!");
                });

        commands.stream()
                .filter(c -> c.isRestaurantStillOpenAtTheEndOfTheDay() && !c.isRestaurantClosingNextDay())
                .findFirst()
                .ifPresent(cmd -> {
                    log.error("The day cannot start with an opening time if the previous day had no closing time for given day: {}", cmd.getDay());
                    throw new ValidationException("Validation failed: When restaurant is still open for the given day, next day should start with close schedule!");
                });

        commands.stream()
                .filter(hasUnorderedOpenCloseTimes())
                .findFirst()
                .ifPresent(cmd -> {
                    log.error("Opening and closing times should be in order within the same day for given day: {}", cmd.getDay());
                    throw new ValidationException("Validation failed: Opening and closing times should be in order within the same day.");
                });
    }

    private void validateInvalidTimeValues(List<ScheduleCommand> commands) {
        commands.stream()
                .filter(hasInvalidTimeValues())
                .findFirst()
                .ifPresent(cmd -> {
                    log.error("Invalid time value(s) found for given day: {}", cmd.getDisplayName());
                    throw new ValidationException("Validation failed: Invalid time value(s) found for day: " + cmd.getDisplayName());
                });
    }

    private Predicate<ScheduleCommand> hasInvalidTimeValues() {
        return cmd -> {
            List<RestaurantSchedule> schedules = cmd.getSchedule();

            return schedules.stream().anyMatch(schedule ->
                    schedule.getUnixTimeInSeconds() == null ||
                    schedule.getUnixTimeInSeconds() < 0 || schedule.getUnixTimeInSeconds() > 86399

            );
        };
    }

    private void validateScheduleAvailabilitySequence(ScheduleCommand scheduleCommand) {
        IntStream.range(0, scheduleCommand.getSchedule().size() - 1)
                .forEach(i -> {
                    RestaurantSchedule currentSchedule = scheduleCommand.getSchedule().get(i);
                    RestaurantSchedule nextSchedule = scheduleCommand.getSchedule().get(i + 1);

                    if (currentSchedule.getRestaurantAvailability() == RestaurantAvailability.OPEN &&
                            nextSchedule.getRestaurantAvailability() == RestaurantAvailability.OPEN) {
                        log.error("Restaurant is already open for given day: {}", scheduleCommand.getDisplayName());
                        throw new ValidationException("Validation failed: Restaurant shouldn't be reopened while it is still open for the given day: " + scheduleCommand.getDisplayName());
                    }

                    if (currentSchedule.getRestaurantAvailability() == RestaurantAvailability.CLOSE &&
                            nextSchedule.getRestaurantAvailability() == RestaurantAvailability.CLOSE) {
                        log.error("Restaurant is already closed for given day: {}", scheduleCommand.getDisplayName());
                        throw new ValidationException("Validation failed: While the restaurant is closed, it cannot be closed again for given day " + scheduleCommand.getDisplayName());
                    }
                });
    }

    private Predicate<ScheduleCommand> hasUnorderedOpenCloseTimes() {
        return cmd -> {
            List<RestaurantSchedule> schedules = cmd.getSchedule();

            return IntStream.range(0, schedules.size() - 1)
                    .anyMatch(i -> {
                        RestaurantSchedule currentSchedule = schedules.get(i);
                        RestaurantSchedule nextSchedule = schedules.get(i + 1);
                        return currentSchedule.getUnixTimeInSeconds() >= nextSchedule.getUnixTimeInSeconds();
                    });
        };
    }
}
