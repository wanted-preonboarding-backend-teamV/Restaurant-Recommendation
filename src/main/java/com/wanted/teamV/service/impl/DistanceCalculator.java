package com.wanted.teamV.service.impl;

public class DistanceCalculator {

    //두 좌표 사이의 거리를 계산하는 로직
    public static double calculate(double point1Lat, double point1Lon, double point2Lat, double point2Lon) {
        double lat1 = point1Lat;
        double lon1 = point1Lon;
        double lat2 = point2Lat;
        double lon2 = point2Lon;

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
