package com.wanted.teamV.service.impl;

import com.wanted.teamV.dto.req.DiscordWebhookReqDto;
import com.wanted.teamV.service.DiscordWebhookService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import static org.springframework.http.HttpStatus.NO_CONTENT;

@Service
@RequiredArgsConstructor
@Slf4j
public class DiscordWebhookServiceImpl implements DiscordWebhookService {

    private final RestTemplate restTemplate;

    @Value("${discord.webhook.url}")
    private String webhookUrl;

    public void sendMessage(DiscordWebhookReqDto request) {
        try {
            HttpEntity<DiscordWebhookReqDto> message = createMessage(request);
            ResponseEntity<String> response = sendMessage(message);

            if (response.getStatusCode() != NO_CONTENT) {
                log.error("디스코드 웹훅 메세지 전송 후 에러 발생");
            }
        } catch (RestClientException e) {
            log.error("RestTemplate 호출 에러 발생: " + e.getMessage(), e);
        } catch (Exception e) {
            log.error("에러 발생: " + e.getMessage(), e);
        }
    }

    private HttpEntity<DiscordWebhookReqDto> createMessage(DiscordWebhookReqDto request) {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        return new HttpEntity<>(request, httpHeaders);
    }

    private ResponseEntity<String> sendMessage(HttpEntity<DiscordWebhookReqDto> message) {
        return restTemplate.exchange(webhookUrl, HttpMethod.POST, message, String.class);
    }

}
