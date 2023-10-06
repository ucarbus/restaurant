package com.wolt.restaurant.domain.service.rules;

import com.wolt.restaurant.domain.model.ScheduleCommand;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class CheckIfClosedAllDayRule implements ScheduleRuleInterface {

    private final ScheduleRuleInterface nextRule;

    public CheckIfClosedAllDayRule(@Qualifier("humanReadableTimeConversionRule") ScheduleRuleInterface nextRule) {
        this.nextRule = nextRule;
    }

    @Override
    public String apply(ScheduleCommand scheduleCommand) {
        log.info("RestaurantClosedAllDayRule has started for day: {}", scheduleCommand.getDisplayName());
        if (scheduleCommand.isClosedForAllDay()) return scheduleCommand.getDisplayName() + ": CLOSED";
        else return nextRule.apply(scheduleCommand);
    }
}

