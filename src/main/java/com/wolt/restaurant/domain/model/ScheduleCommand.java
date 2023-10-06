package com.wolt.restaurant.domain.model;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.DayOfWeek;
import java.time.format.TextStyle;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

import static com.wolt.restaurant.application.model.request.RestaurantAvailability.CLOSE;
import static com.wolt.restaurant.application.model.request.RestaurantAvailability.OPEN;

@RequiredArgsConstructor
@Getter
@EqualsAndHashCode
public class ScheduleCommand {

    private final DayOfWeek day;
    private final List<RestaurantSchedule> schedule;
    private final List<RestaurantSchedule> scheduleOfNextDay;

    public Boolean isClosedForAllDay() {
        return schedule.isEmpty();
    }

    public String getDisplayName() {
        return day.getDisplayName(TextStyle.FULL, Locale.getDefault());
    }

    public Boolean isStillOpenFromPreviousDay() {
        return !schedule.isEmpty() && schedule.get(0).getRestaurantAvailability() == CLOSE;
    }

    public Boolean isRestaurantClosingNextDay() {
        return !scheduleOfNextDay.isEmpty() && scheduleOfNextDay.get(0).getRestaurantAvailability() == CLOSE;
    }

    public Boolean isRestaurantClosedAtTheEndOfTheDay() {
        return !schedule.isEmpty() && schedule.get(schedule.size() - 1).getRestaurantAvailability().equals(CLOSE);
    }

    public boolean isRestaurantStillOpenAtTheEndOfTheDay() {
        return !schedule.isEmpty() && schedule.get(schedule.size() - 1).getRestaurantAvailability().equals(OPEN);
    }

    public List<RestaurantSchedule> getOpenSchedules() {
        return schedule.stream().filter(availability -> availability.getRestaurantAvailability().equals(OPEN)).collect(Collectors.toList());
    }

    public List<RestaurantSchedule> getCloseSchedules() {
        List<RestaurantSchedule> closeSchedules = schedule.stream().filter(availability -> availability.getRestaurantAvailability().equals(CLOSE)).collect(Collectors.toList());
        if (isRestaurantClosingNextDay()) {return addNextDayClosingTime(closeSchedules);} else return closeSchedules;
    }

    private List<RestaurantSchedule> addNextDayClosingTime(List<RestaurantSchedule> closeSchedules) {
        RestaurantSchedule closeScheduleForNextDay = scheduleOfNextDay.get(0);
        if (closeScheduleForNextDay.getRestaurantAvailability() != CLOSE)
            throw new IllegalStateException("Provided schedule list is in illegal state because DAY does not have a close schedule!");
        List<RestaurantSchedule> allCloseSchedules = new ArrayList<>(closeSchedules);
        allCloseSchedules.add(closeScheduleForNextDay);
        return allCloseSchedules;
    }
}