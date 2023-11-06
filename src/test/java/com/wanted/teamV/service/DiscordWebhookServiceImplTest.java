package com.wanted.teamV.service;

import com.wanted.teamV.dto.req.DiscordWebhookReqDto;
import com.wanted.teamV.service.impl.DiscordWebhookServiceImpl;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class DiscordWebhookServiceImplTest {

    @InjectMocks
    private DiscordWebhookServiceImpl discordWebhookService;

    @Mock
    private RestTemplate restTemplate;

    @Value("${discord.webhook.url}")
    private String webhookUrl;

    @Test
    @DisplayName("웹훅 전송 - 성공")
    void sendMessage_Success() {
        //given
        DiscordWebhookReqDto request = new DiscordWebhookReqDto();
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<DiscordWebhookReqDto> message = new HttpEntity<>(request, httpHeaders);
        ResponseEntity<String> response = new ResponseEntity<>("message", HttpStatus.NO_CONTENT);

        when(restTemplate.exchange(webhookUrl, HttpMethod.POST, message, String.class))
                .thenReturn(response);

        //when
        discordWebhookService.sendMessage(request);

        //then
        verify(restTemplate).exchange(webhookUrl, HttpMethod.POST, message, String.class);
    }

}