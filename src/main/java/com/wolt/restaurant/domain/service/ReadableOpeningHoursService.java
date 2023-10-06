package com.wolt.restaurant.domain.service;

import com.wolt.restaurant.domain.model.ScheduleCommand;
import com.wolt.restaurant.domain.service.rules.ScheduleRuleInterface;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ReadableOpeningHoursService {

    private final ScheduleRuleInterface scheduleRuleInterface;

    public ReadableOpeningHoursService(@Qualifier("checkIfOpenFromPreviousDayRule") ScheduleRuleInterface scheduleRuleInterface) {
        this.scheduleRuleInterface = scheduleRuleInterface;
    }

    public String deriveReadableOpeningHours(List<ScheduleCommand> days) {
        return days.stream()
                .map(scheduleRuleInterface::apply)
                .collect(Collectors.joining("\n"));
    }
}
