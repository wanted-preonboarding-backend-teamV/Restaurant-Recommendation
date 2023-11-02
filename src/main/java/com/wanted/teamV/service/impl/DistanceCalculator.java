package com.wanted.teamV.service.impl;

import com.wanted.teamV.dto.Coordinate;

public class DistanceCalculator {

    //두 좌표 사이의 거리를 계산하는 로직
    public static double calculate(Coordinate point1, Coordinate point2) {
        double lat1 = point1.lat();
        double lon1 = point1.lon();
        double lat2 = point2.lat();
        double lon2 = point2.lon();

        double R = 6371; // km
        double dLat = Math.toRadians(lat2 - lat1);
        double dLon = Math.toRadians(lon2 - lon1);

        lat1 = Math.toRadians(lat1);
        lat2 = Math.toRadians(lat2);

        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2) +
                Math.sin(dLon / 2) * Math.sin(dLon / 2) * Math.cos(lat1) * Math.cos(lat2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));

        return R * c;
    }
}
