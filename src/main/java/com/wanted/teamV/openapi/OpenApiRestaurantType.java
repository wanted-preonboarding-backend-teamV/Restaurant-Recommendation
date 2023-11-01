package com.wanted.teamV.openapi;

public enum OpenApiRestaurantType {
    LUNCH("Genrestrtlunch", "김밥(도시락)"),
    CHINA_FOOD("Genrestrtchifood", "중국식"),
    JAPAN_FOOD("Genrestrtjpnfood", "일식"),
    FAST_FOOD("Genrestrtfastfood", "패스트푸드")
    ;

    private final String path;
    private final String type;

    OpenApiRestaurantType(String path, String type) {
        this.path = path;
        this.type = type;
    }

    public String getPath() {
        return path;
    }

    public String getType() {
        return type;
    }
}
