package com.wolt.restaurant.domain.service.rules;

import com.wolt.restaurant.domain.model.RestaurantSchedule;
import com.wolt.restaurant.domain.model.ScheduleCommand;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Slf4j
public class CheckIfOpenFromPreviousDayRule implements ScheduleRuleInterface {

    private final ScheduleRuleInterface nextRule;

    public CheckIfOpenFromPreviousDayRule(@Qualifier("checkIfClosedAllDayRule") ScheduleRuleInterface nextRule) {
        this.nextRule = nextRule;
    }

    @Override
    public String apply(ScheduleCommand scheduleCommand) {
        log.info("RestaurantIsStillOpenFromPreviousDayRule has started for day: {}", scheduleCommand.getDisplayName());
        if (scheduleCommand.isStillOpenFromPreviousDay()) {
            return nextRule.apply(toDayEventExcludingPreviousDay(scheduleCommand));
        } else {
            return nextRule.apply(scheduleCommand);
        }
    }

    private ScheduleCommand toDayEventExcludingPreviousDay(ScheduleCommand scheduleCommand) {
        List<RestaurantSchedule> schedules = scheduleCommand.getSchedule();
        List<RestaurantSchedule> schedulesExcludingPreviousDay = schedules.subList(1, schedules.size());
        return new ScheduleCommand(scheduleCommand.getDay(), schedulesExcludingPreviousDay, scheduleCommand.getScheduleOfNextDay());
    }
}