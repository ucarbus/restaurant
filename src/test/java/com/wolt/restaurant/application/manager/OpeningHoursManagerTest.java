package com.wolt.restaurant.application.manager;

import com.wolt.restaurant.application.manager.mapper.OpeningHoursResponseMapper;
import com.wolt.restaurant.application.model.request.RestaurantAvailability;
import com.wolt.restaurant.application.model.request.ScheduleRequest;
import com.wolt.restaurant.application.model.response.OpeningHoursResponse;
import com.wolt.restaurant.domain.mapper.ScheduleMapper;
import com.wolt.restaurant.domain.model.RestaurantSchedule;
import com.wolt.restaurant.domain.model.ScheduleCommand;
import com.wolt.restaurant.domain.service.ReadableOpeningHoursService;
import com.wolt.restaurant.domain.service.ScheduleValidationService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InOrder;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.DayOfWeek;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class OpeningHoursManagerTest {

    @InjectMocks
    private OpeningHoursManager openingHoursManager;

    @Mock
    private ScheduleMapper scheduleMapper;
    @Mock
    private ReadableOpeningHoursService readableOpeningHoursService;
    @Mock
    private ScheduleValidationService scheduleValidationService;
    @Mock
    private OpeningHoursResponseMapper openingHoursResponseMapper;

    @Test
    public void should_generate_opening_hours() {
        // given
        ScheduleRequest scheduleRequest = new ScheduleRequest();

        List<ScheduleCommand> scheduleCommands = Arrays.asList(
                createScheduleCommand(DayOfWeek.MONDAY),
                createScheduleCommand(DayOfWeek.MONDAY)
        );

        String result = "Monday: 10 AM - 6 PM";

        OpeningHoursResponse mockResponse = new OpeningHoursResponse();
        mockResponse.setFormattedOpeningHours(result);

        when(scheduleMapper.mapToScheduleCommands(scheduleRequest)).thenReturn(scheduleCommands);
        doNothing().when(scheduleValidationService).validate(scheduleCommands);
        when(readableOpeningHoursService.deriveReadableOpeningHours(scheduleCommands)).thenReturn(result);
        when(openingHoursResponseMapper.mapAndReturnSuccessResponse(result)).thenReturn(mockResponse);

        // when
        OpeningHoursResponse response = openingHoursManager.generateOpeningHours(scheduleRequest);

        // then
        InOrder inOrder = Mockito.inOrder(
                scheduleValidationService,
                scheduleMapper,
                readableOpeningHoursService,
                openingHoursResponseMapper);

        inOrder.verify(scheduleMapper).mapToScheduleCommands(scheduleRequest);
        inOrder.verify(scheduleValidationService).validate(scheduleCommands);
        inOrder.verify(readableOpeningHoursService).deriveReadableOpeningHours(scheduleCommands);
        inOrder.verify(openingHoursResponseMapper).mapAndReturnSuccessResponse(result);
        inOrder.verifyNoMoreInteractions();
        assertEquals(result, response.getFormattedOpeningHours());
    }

    private ScheduleCommand createScheduleCommand(DayOfWeek dayOfWeek) {
        List<RestaurantSchedule> schedule = new ArrayList<>();
        schedule.add(new RestaurantSchedule(RestaurantAvailability.OPEN, 36000));
        schedule.add(new RestaurantSchedule(RestaurantAvailability.CLOSE, 64800));
        return new ScheduleCommand(dayOfWeek, schedule, new ArrayList<>());
    }
}
