package com.wanted.teamV.dto.res;

public record RestaurantDistrictResDto(
        String doSi, //도, 시(특별시 등)
        String sgg, //시군구
        double lat, //위도
        double lon //경도
) {

    public static RestaurantDistrictResDto toDto(String district) {
        String[] districtData = district.split("\\,");

        return new RestaurantDistrictResDto(
                districtData[0],
                districtData[1],
                Double.parseDouble(districtData[3]),
                Double.parseDouble(districtData[2])
        );
    }
}
