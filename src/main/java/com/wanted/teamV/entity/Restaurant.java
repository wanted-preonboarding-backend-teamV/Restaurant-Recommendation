package com.wanted.teamV.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Restaurant {
    @Id
    @Column(name = "restaurant_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @Column(nullable = false)
    String name;

    @Column(nullable = false)
    String sigun;

    @Column(nullable = false)
    String type;

    @Column(nullable = false)
    String roadnameAddress;

    @Column(nullable = false)
    String lotAddress;

    @Column(nullable = false)
    String zipCode;

    @Column(nullable = false)
    Double lat;

    @Column(nullable = false)
    Double lon;

    @Column(nullable = false)
    Double avg_rating;

    @OneToMany(mappedBy = "restaurant")
    List<Rating> ratingList = new ArrayList<>();

    @Builder
    public Restaurant(String name, String sigun, String type, String roadnameAddress, String lotAddress, String zipCode, Double lat, Double lon, Double avg_rating) {
        this.name = name;
        this.sigun = sigun;
        this.type = type;
        this.roadnameAddress = roadnameAddress;
        this.lotAddress = lotAddress;
        this.zipCode = zipCode;
        this.lat = lat;
        this.lon = lon;
        this.avg_rating = avg_rating;
    }
}
