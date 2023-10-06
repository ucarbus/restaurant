package com.wolt.restaurant.domain.service.rules;

import com.wolt.restaurant.domain.model.ScheduleCommand;

public interface ScheduleRuleInterface {
    String apply(ScheduleCommand scheduleCommand);
}