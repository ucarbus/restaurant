package com.wolt.restaurant.domain.service.rules;

import com.wolt.restaurant.domain.model.ScheduleCommand;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import static com.wolt.restaurant.domain.utility.UnixTimeConverter.convertToReadableTime;

@Component
@Slf4j
public class HumanReadableTimeConversionRule implements ScheduleRuleInterface {

    @Override
    public String apply(ScheduleCommand scheduleCommand) {
        log.info("RestaurantClosesDayRule has started for day: {}", scheduleCommand.getDisplayName());
        return convertToReadableTime(scheduleCommand);
    }
}

