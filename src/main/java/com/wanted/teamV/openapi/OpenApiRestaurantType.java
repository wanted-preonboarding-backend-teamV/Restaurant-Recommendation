package com.wanted.teamV.openapi;

public enum OpenApiRestaurantType {
    LUNCH("Genrestrtlunch"),
    CHINA_FOOD("Genrestrtchifood"),
    JAPAN_FOOD("Genrestrtjpnfood"),
    FAST_FOOD("Genrestrtfastfood")
    ;

    private String path;

    OpenApiRestaurantType(String path) {
        this.path = path;
    }

    public String getPath() {
        return path;
    }
}
