package com.wolt.restaurant.application.manager;

import com.wolt.restaurant.application.manager.mapper.OpeningHoursResponseMapper;
import com.wolt.restaurant.application.model.request.ScheduleRequest;
import com.wolt.restaurant.application.model.response.OpeningHoursResponse;
import com.wolt.restaurant.domain.mapper.ScheduleMapper;
import com.wolt.restaurant.domain.model.ScheduleCommand;
import com.wolt.restaurant.domain.service.ReadableOpeningHoursService;
import com.wolt.restaurant.domain.service.ScheduleValidationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class OpeningHoursManager {

    private final ScheduleMapper scheduleMapper;
    private final ReadableOpeningHoursService readableOpeningHoursService;
    private final ScheduleValidationService scheduleValidationService;
    private final OpeningHoursResponseMapper openingHoursResponseMapper;

    public OpeningHoursResponse generateOpeningHours(ScheduleRequest scheduleRequest) {
        log.info("Generation of opening hours has started");
        List<ScheduleCommand> scheduleCommands = scheduleMapper.mapToScheduleCommands(scheduleRequest);
        scheduleValidationService.validate(scheduleCommands);
        String result = readableOpeningHoursService.deriveReadableOpeningHours(scheduleCommands);
        log.info("Generation of opening hours ends with result: {}", result);
        return openingHoursResponseMapper.mapAndReturnSuccessResponse(result);
    }
}
