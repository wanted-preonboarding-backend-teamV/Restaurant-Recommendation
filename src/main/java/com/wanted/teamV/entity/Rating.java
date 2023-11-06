package com.wanted.teamV.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EntityListeners(AuditingEntityListener.class)
public class Rating {
    @Id
    @Column(name = "rating_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "restaurant_id", nullable = false)
    Restaurant restaurant;

    @Column(nullable = false)
    int score;

    @Column(nullable = false)
    String content;

    @Column(nullable = false)
    @CreatedDate
    LocalDateTime createdAt;

    @Builder
    public Rating(Member member, Restaurant restaurant, int score, String content, LocalDateTime createdAt) {
        this.member = member;
        this.restaurant = restaurant;
        restaurant.addRating(this);
        this.score = score;
        this.content = content;
        this.createdAt = createdAt;
    }

    public Long getMemberId() {
        return member.getId();
    }

    public Long getRestaurantId() {
        return restaurant.getId();
    }
}
