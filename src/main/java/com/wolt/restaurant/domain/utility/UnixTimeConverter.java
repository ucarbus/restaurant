package com.wolt.restaurant.domain.utility;

import com.wolt.restaurant.domain.model.RestaurantSchedule;
import com.wolt.restaurant.domain.model.ScheduleCommand;
import lombok.experimental.UtilityClass;

import java.time.LocalTime;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@UtilityClass
public class UnixTimeConverter {

    public static String convertToReadableTime(ScheduleCommand scheduleCommand) {
        List<RestaurantSchedule> openSchedules = scheduleCommand.getOpenSchedules();
        List<RestaurantSchedule> closeSchedules = scheduleCommand.getCloseSchedules();

        String schedulesInReadableFormat = IntStream.range(0, openSchedules.size())
                .mapToObj(i -> convertToReadableTime(openSchedules.get(i).getUnixTimeInSeconds()) + " - " + convertToReadableTime(closeSchedules.get(i).getUnixTimeInSeconds()))
                .collect(Collectors.joining(", "));

        return scheduleCommand.getDisplayName() + ": " + schedulesInReadableFormat;
    }

    private static String convertToReadableTime(int unixTime) {
        LocalTime time = LocalTime.ofSecondOfDay(unixTime);
        int hour = time.getHour();
        int minute = time.getMinute();
        String period = "AM";
        if (hour >= 12) {
            if (hour > 12) hour -= 12;
            period = "PM";
        } else if (hour == 0) {
            hour = 12;
        }
        return hour + (minute == 0 ? "" : ":" + String.format("%02d", minute)) + " " + period;
    }
}
