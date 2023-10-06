package com.wolt.restaurant.application.manager.mapper;

import com.wolt.restaurant.application.model.response.OpeningHoursResponse;
import com.wolt.restaurant.domain.model.enumtype.ResponseStatusType;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
class OpeningHoursResponseMapperTest {

    @InjectMocks
    private OpeningHoursResponseMapper openingHoursResponseMapper;

    @Test
    public void should_map_and_return_success_response() {
        //given
        String formattedOpeningHours = "Opening Hours";

        //when
        OpeningHoursResponse response = openingHoursResponseMapper.mapAndReturnSuccessResponse(formattedOpeningHours);

        //then
        assertThat(response).isNotNull();
        assertThat(response.getFormattedOpeningHours()).isEqualTo(formattedOpeningHours);
        assertThat(response.getStatus()).isEqualTo(ResponseStatusType.SUCCESS.getValue());
    }

}