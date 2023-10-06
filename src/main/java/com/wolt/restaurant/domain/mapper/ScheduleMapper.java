package com.wolt.restaurant.domain.mapper;

import com.wolt.restaurant.application.model.request.RestaurantAvailability;
import com.wolt.restaurant.application.model.request.RestaurantScheduleRequest;
import com.wolt.restaurant.application.model.request.ScheduleRequest;
import com.wolt.restaurant.domain.model.RestaurantSchedule;
import com.wolt.restaurant.domain.model.ScheduleCommand;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static java.time.DayOfWeek.FRIDAY;
import static java.time.DayOfWeek.MONDAY;
import static java.time.DayOfWeek.SATURDAY;
import static java.time.DayOfWeek.SUNDAY;
import static java.time.DayOfWeek.THURSDAY;
import static java.time.DayOfWeek.TUESDAY;
import static java.time.DayOfWeek.WEDNESDAY;
import static java.util.Collections.emptyList;

@Component
public class ScheduleMapper {

    public List<ScheduleCommand> mapToScheduleCommands(ScheduleRequest scheduleRequest) {
        return List.of(
                new ScheduleCommand(
                        MONDAY,
                        toRestaurantDailySchedule(scheduleRequest.getScheduleForMonday()),
                        toRestaurantDailySchedule(scheduleRequest.getScheduleForTuesday())),

                new ScheduleCommand(
                        TUESDAY,
                        toRestaurantDailySchedule(scheduleRequest.getScheduleForTuesday()),
                        toRestaurantDailySchedule(scheduleRequest.getScheduleForWednesday())),

                new ScheduleCommand(
                        WEDNESDAY,
                        toRestaurantDailySchedule(scheduleRequest.getScheduleForWednesday()),
                        toRestaurantDailySchedule(scheduleRequest.getScheduleForThursday())),

                new ScheduleCommand(
                        THURSDAY,
                        toRestaurantDailySchedule(scheduleRequest.getScheduleForThursday()),
                        toRestaurantDailySchedule(scheduleRequest.getScheduleForFriday())),

                new ScheduleCommand(
                        FRIDAY,
                        toRestaurantDailySchedule(scheduleRequest.getScheduleForFriday()),
                        toRestaurantDailySchedule(scheduleRequest.getScheduleForSaturday())),

                new ScheduleCommand(
                        SATURDAY,
                        toRestaurantDailySchedule(scheduleRequest.getScheduleForSaturday()),
                        toRestaurantDailySchedule(scheduleRequest.getScheduleForSunday())),

                new ScheduleCommand(
                        SUNDAY,
                        toRestaurantDailySchedule(scheduleRequest.getScheduleForSunday()),
                        emptyList()));
    }

    private List<RestaurantSchedule> toRestaurantDailySchedule(List<RestaurantScheduleRequest> request) {
        return Optional.ofNullable(request)
                .map(req -> req.stream()
                        .map(this::mapToRestaurantSchedule)
                        .collect(Collectors.toList()))
                .orElse(null);
    }

    private RestaurantSchedule mapToRestaurantSchedule(RestaurantScheduleRequest request) {
        Optional<RestaurantAvailability> availability = Optional.ofNullable(request.getRestaurantAvailability());

        return availability.map(a -> new RestaurantSchedule(RestaurantAvailability.valueOf(a.name()), request.getUnixTimeInSeconds()))
                .orElse(new RestaurantSchedule(null, request.getUnixTimeInSeconds()));
    }


}
