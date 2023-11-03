package com.wanted.teamV.openapi.converter;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.wanted.teamV.openapi.OpenApiRestaurantType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class OpenApiRawConverter {

    private final ObjectMapper objectMapper;

    public OpenApiRawHead convertToHead(String raw, OpenApiRestaurantType restaurantType) throws JsonProcessingException {
        OpenApiRawHead response = new OpenApiRawHead();
        try {
            Map<String, List<LinkedHashMap<String, Object>>> map = objectMapper.readValue(raw, new TypeReference<>() {});
            List<LinkedHashMap<String, Object>> heads = (List<LinkedHashMap<String, Object>>) map.get(restaurantType.getPath()).get(0).get("head");

            for (LinkedHashMap<String, Object> object : heads) {
                if (object.containsKey("list_total_count")) {
                    response.setListTotalCount((Integer) object.get("list_total_count"));
                }

                if (object.containsKey("RESULT")) {
                    LinkedHashMap<String, String> values = (LinkedHashMap<String, String>) object.get("RESULT");
                    response.setResultCode(values.get("CODE"));
                    response.setResultMessage(values.get("MESSAGE"));
                }

                if (object.containsKey("api_version")) {
                    response.setApiVersion((String) object.get("api_version"));
                }
            }
        } catch (JsonProcessingException ex) {
            LinkedHashMap<String, LinkedHashMap<String, String>> result = objectMapper.readValue(raw, new TypeReference<>() {});
            if (result.containsKey("RESULT")) {
                LinkedHashMap<String, String> values = result.get("RESULT");
                response.setResultCode(values.get("CODE"));
                response.setResultMessage(values.get("MESSAGE"));
            }
        }
        return response;
    }

    public List<OpenApiRawRestaurant> convertToRestaurants(String raw, OpenApiRestaurantType restaurantType) throws JsonProcessingException {
        Map<String, List<LinkedHashMap<String, List<OpenApiRawRestaurant>>>> map = objectMapper.readValue(raw, new TypeReference<>() {});
        return map.get(restaurantType.getPath()).get(1).get("row");
    }

}
