package com.wanted.teamV.openapi;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.wanted.teamV.entity.Restaurant;
import com.wanted.teamV.openapi.converter.OpenApiRawConverter;
import com.wanted.teamV.openapi.converter.OpenApiRawRestaurant;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class OpenApiServiceTest {

    private final OpenApiRawConverter converter = new OpenApiRawConverter(new ObjectMapper());
    private final OpenApi openApi = mock(OpenApi.class);
    private final OpenApiService openApiService = new OpenApiService(openApi, converter);


    @Test
    @DisplayName("OpenAPI에서 특정 업종에 속하는 음식점들의 데이터를 모두 불러온다.")
    public void get_raw_data() throws IOException {
        // given
        String path = "src/test/resources/json/success.json";
        String rawJson = new String(Files.readAllBytes(Paths.get(path)));

        when(openApi.getRestaurantsInfo(any(), eq("json"), anyInt(), anyInt(), any()))
                .thenReturn(rawJson);

        // when
        List<OpenApiRawRestaurant> rawRestaurants = openApiService.getRawDataFromOpenapi(OpenApiRestaurantType.LUNCH);

        // then
        Assertions.assertEquals(5, rawRestaurants.size());
    }

    @Test
    @DisplayName("OpenAPI 요청에 오류가 발생하여 음식점들의 데이터를 불러오는 데 실패한다.")
    public void get_raw_data_failure() throws IOException {
        // given
        String path = "src/test/resources/json/failure.json";
        String rawJson = new String(Files.readAllBytes(Paths.get(path)));

        when(openApi.getRestaurantsInfo(any(), eq("json"), anyInt(), anyInt(), any()))
                .thenReturn(rawJson);

        // when
        List<OpenApiRawRestaurant> rawRestaurants = openApiService.getRawDataFromOpenapi(OpenApiRestaurantType.LUNCH);

        // then
        Assertions.assertEquals(0, rawRestaurants.size());
    }

    @Test
    @DisplayName("Raw 데이터를 전처리한다.")
    public void preprocess_raw_data() throws IOException {
        // given
        String path = "src/test/resources/json/success.json";
        String rawJson = new String(Files.readAllBytes(Paths.get(path)));
        OpenApiRestaurantType restaurantType = OpenApiRestaurantType.LUNCH;

        when(openApi.getRestaurantsInfo(any(), eq("json"), anyInt(), anyInt(), any()))
                .thenReturn(rawJson);
        List<OpenApiRawRestaurant> rawRestaurants = openApiService.getRawDataFromOpenapi(restaurantType);

        // when
        List<OpenApiRawRestaurant> preprocessed = openApiService.preprocessRawData(rawRestaurants, restaurantType);

        // then
        Assertions.assertEquals(4, preprocessed.size());
        Assertions.assertEquals("20150205", preprocessed.get(0).getLicenseDe());
        Assertions.assertEquals("19910618", preprocessed.get(1).getLicenseDe());
        Assertions.assertEquals("20150521", preprocessed.get(2).getLicenseDe());
        Assertions.assertEquals("20141201", preprocessed.get(3).getLicenseDe());
    }

    @Test
    @DisplayName("raw 데이터들을 Entity로 변환한다.")
    public void translate_raw_to_entity() throws IOException {
        // given
        String path = "src/test/resources/json/success.json";
        String rawJson = new String(Files.readAllBytes(Paths.get(path)));
        OpenApiRestaurantType restaurantType = OpenApiRestaurantType.LUNCH;

        when(openApi.getRestaurantsInfo(any(), eq("json"), anyInt(), anyInt(), any()))
                .thenReturn(rawJson);
        List<OpenApiRawRestaurant> rawRestaurants = openApiService.getRawDataFromOpenapi(restaurantType);
        List<OpenApiRawRestaurant> preprocessed = openApiService.preprocessRawData(rawRestaurants, restaurantType);


        // when
        List<Restaurant> restaurants = openApiService.rawToRestaurants(preprocessed);

        // then
        Assertions.assertEquals(4, restaurants.size());
        Assertions.assertEquals(LocalDateTime.of(2015,2, 5, 0,0,0), restaurants.get(0).getLicenseDe());
        Assertions.assertEquals(LocalDateTime.of(1991,6, 18, 0,0,0), restaurants.get(1).getLicenseDe());
        Assertions.assertEquals(LocalDateTime.of(2015,5, 21, 0,0,0), restaurants.get(2).getLicenseDe());
        Assertions.assertEquals(LocalDateTime.of(2014,12,1, 0,0,0), restaurants.get(3).getLicenseDe());
    }
}