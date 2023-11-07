package com.wanted.teamV.component;

import com.wanted.teamV.dto.req.DiscordWebhookReqDto;
import com.wanted.teamV.entity.Member;
import com.wanted.teamV.entity.Restaurant;
import com.wanted.teamV.repository.MemberRepository;
import com.wanted.teamV.repository.RestaurantRepository;
import com.wanted.teamV.service.DiscordWebhookService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class RecommendationScheduler {

    private final RestaurantRepository restaurantRepository;
    private final MemberRepository memberRepository;
    private final DiscordWebhookService discordWebhookService;

    @Value("${discord.webhook.avatar_url}")
    private String AVATAR_URL;

    @Value("${discord.webhook.author_url}")
    private String AUTHOR_URL;

    @Value("${discord.webhook.author_icon}")
    private String AUTHOR_ICON;

    @Value("${discord.webhook.footer_icon}")
    private String FOOTER_ICON;

    @Scheduled(cron = "${schedules.cron.lunch-recommendation}")
    @Transactional(readOnly = true)
    public void sendRecommendations() {
        // 점심 추천에 동의한 모든 사용자 가져오기
        List<Member> members = memberRepository.findAllByRecommendTrue();

        members.forEach(member -> {
            // 사용자에게 해당하는 메세지 생성
            DiscordWebhookReqDto request = createRequest(member);

            // 추천할 맛집 목록 가져오기
            List<Restaurant> restaurants = restaurantRepository.findRecommendRestaurants(member.getLat(), member.getLon());

            // Embed 생성
            List<DiscordWebhookReqDto.Embed> embeds = createEmbeds(restaurants);

            // 메세지에 embeds 설정
            request.setEmbeds(embeds);

            // 전송
            log.info("점심추천 알림 전송 완료. 수신 : {}", member.getAccount());
            discordWebhookService.sendMessage(request);
        });
    }

    private DiscordWebhookReqDto createRequest(Member member) {
        return DiscordWebhookReqDto.builder()
                .content(member.getAccount() + "님! 오늘도 즐거운 점심시간 보내세요!")
                .avatarUrl(AVATAR_URL)
                .embeds(new ArrayList<>())
                .build();
    }

    private List<DiscordWebhookReqDto.Embed> createEmbeds(List<Restaurant> restaurants) {
        List<DiscordWebhookReqDto.Embed> embeds = new ArrayList<>();
        DiscordWebhookReqDto.Embed embed = null;
        String type = "";

        for (int i = 0; i < restaurants.size(); i++) {
            Restaurant restaurant = restaurants.get(i);
            String currentType = restaurant.getType();

            // 타입으로 구분하여 만약 5개가 되지 않는 타입이 있더라도 처리가능
            if (!type.equals(currentType)) {
                embed = createEmbed(restaurant);
                embeds.add(embed);
                type = currentType;
            }

            DiscordWebhookReqDto.Field field = DiscordWebhookReqDto.Field.builder()
                    .name(restaurant.getName() + " ⭐ " + restaurant.getAverageRating())
                    .value(restaurant.getRoadnameAddress())
                    .inline(true)
                    .build();

            embed.getFields().add(field);

            // Embed 사이에 빈 필드 추가 (수직 정렬을 위해)
            if (i < restaurants.size() - 1) {
                embed.addBlankField();
            }
        }

        return embeds;
    }

    private DiscordWebhookReqDto.Embed createEmbed(Restaurant restaurant) {
        return DiscordWebhookReqDto.Embed.builder()
                .author(createAuthor())
                .title("오늘의 추천 " + restaurant.getType())
                .color(37411)
                .fields(new ArrayList<>())
                .footer(createFooter())
                .build();
    }

    private DiscordWebhookReqDto.Author createAuthor() {
        return DiscordWebhookReqDto.Author.builder()
                .name("Team V")
                .url(AUTHOR_URL)
                .iconUrl(AUTHOR_ICON)
                .build();
    }

    private DiscordWebhookReqDto.Footer createFooter() {
        return DiscordWebhookReqDto.Footer.builder()
                .text("언제나 당신을 위한 맛집과 함께 돌아올게요, Enjoy your LunchHere :)")
                .iconUrl(FOOTER_ICON)
                .build();
    }

}