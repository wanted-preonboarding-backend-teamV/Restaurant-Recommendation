package com.wanted.teamV.openapi.converter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.wanted.teamV.openapi.OpenApiRestaurantType;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

class OpenApiRawConverterTest {
    private final ObjectMapper objectMapper = new ObjectMapper();

    private final OpenApiRawConverter converter = new OpenApiRawConverter(objectMapper);

    @Test
    @DisplayName("Open API 응답 JSON 데이터를 파싱하여 응답 정보 객체를 얻는다.")
    public void parse_json_and_get_head_info() throws Exception {
        // given
        String path = "src/test/resources/json/success.json";
        String rawJson = getRawJsonString(path);
        OpenApiRestaurantType restaurantType = OpenApiRestaurantType.LUNCH;

        // when
        OpenApiRawHead rawHead = converter.convertToHead(rawJson, restaurantType);

        // then
        Assertions.assertEquals(5, rawHead.getListTotalCount());
        Assertions.assertEquals("INFO-000", rawHead.getResultCode());
        Assertions.assertEquals("정상 처리되었습니다.", rawHead.getResultMessage());
        Assertions.assertEquals("1.0", rawHead.getApiVersion());
    }

    @Test
    @DisplayName("Open API 응답 JSON 데이터를 파싱하여 맛집 정보 리스트를 얻는다.")
    public void parse_json_and_get_restaurants_data() throws Exception {
        // given
        String path = "src/test/resources/json/success.json";
        String rawJson = getRawJsonString(path);
        OpenApiRestaurantType restaurantType = OpenApiRestaurantType.LUNCH;

        // when
        List<OpenApiRawRestaurant> rawRestaurants = converter.convertToRestaurants(rawJson, restaurantType);

        // then
        Assertions.assertEquals(5, rawRestaurants.size());
        Assertions.assertEquals("화성시 ", rawRestaurants.get(0).getSigunNm());
        Assertions.assertEquals("마니또김밥", rawRestaurants.get(0).getBizplcNm());
        Assertions.assertEquals("20150205", rawRestaurants.get(0).getLicenseDe());
        Assertions.assertEquals("영업", rawRestaurants.get(0).getBsnStateNm());
        Assertions.assertEquals("김밥(도시락)", rawRestaurants.get(0).getSanittnBizcondNm());
        Assertions.assertEquals("경기도 화성시 영통로 59, 1층 112호 (반월동, 현대프라자)", rawRestaurants.get(0).getRefineRoadnmAddr());
        Assertions.assertEquals("경기도 화성시 반월동 869 현대프라자 112호", rawRestaurants.get(0).getRefineLotnoAddr());
        Assertions.assertEquals("18378", rawRestaurants.get(0).getRefineZipCd());
        Assertions.assertEquals("127.0619018628", rawRestaurants.get(0).getRefindWgs84Logt());
        Assertions.assertEquals("37.2351254448", rawRestaurants.get(0).getRefineWgs84Lat());
    }

    private String getRawJsonString(String path) throws IOException {
        StringBuilder sb = new StringBuilder();
        for (String str : Files.readAllLines(Paths.get(path))) {
            sb.append(str.trim());
        }
        return sb.toString();
    }

}