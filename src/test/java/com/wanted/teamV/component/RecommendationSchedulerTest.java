package com.wanted.teamV.component;

import com.wanted.teamV.entity.Member;
import com.wanted.teamV.entity.Restaurant;
import com.wanted.teamV.repository.MemberRepository;
import com.wanted.teamV.repository.RestaurantRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.system.CapturedOutput;
import org.springframework.boot.test.system.OutputCaptureExtension;

import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(OutputCaptureExtension.class)
@SpringBootTest(properties = {"schedules.cron.lunch-recommendation=0/10 * * * * *"})
class RecommendationSchedulerTest {

    @Autowired
    private RecommendationScheduler recommendationScheduler;

    @Autowired
    private RestaurantRepository restaurantRepository;

    @Autowired
    private MemberRepository memberRepository;

    @Test
    @DisplayName("점심 추천 기능 스케줄 테스트")
    void send_recommendations_test(CapturedOutput output) {
        //given
        double initialLat = 37.1234567; // 초기 lat 값
        double initialLon = 127.1234567; // 초기 lon 값

        Member member1 = new Member("member1", "member1234!", initialLat, initialLon, true);
        Member member2 = new Member("member2", "member1234!", initialLat, initialLon, false);
        Member member3 = new Member("member3", "member1234!", initialLat, initialLon, true);

        memberRepository.save(member1);
        memberRepository.save(member2);
        memberRepository.save(member3);

        Restaurant restaurant1 = Restaurant.builder()
                .name("Fast Food")
                .sigun("서울시")
                .type("패스트푸드")
                .roadnameAddress("서울특별시 송파구 어쩌구")
                .lotAddress("서울특별시 송파구 어쩌구")
                .zipCode("123456")
                .lat(initialLat + 0.001)
                .lon(initialLon + 0.001)
                .averageRating(Math.floor(Math.random() * 51) / 10.0)
                .build();

        Restaurant restaurant2 = Restaurant.builder()
                .name("중국집")
                .sigun("서울시")
                .type("중국식")
                .roadnameAddress("서울특별시 송파구 어쩌구")
                .lotAddress("서울특별시 송파구 어쩌구")
                .zipCode("123456")
                .lat(initialLat + 0.001)
                .lon(initialLon + 0.0015)
                .averageRating(Math.floor(Math.random() * 51) / 10.0)
                .build();

        restaurantRepository.save(restaurant1);
        restaurantRepository.save(restaurant2);

        //when
        recommendationScheduler.sendRecommendations();

        //then
        assertTrue(output.getOut().contains("점심추천 알림 전송 완료. 수신 : " + member1.getAccount()));
        assertTrue(output.getOut().contains("점심추천 알림 전송 완료. 수신 : " + member3.getAccount()));
    }
}