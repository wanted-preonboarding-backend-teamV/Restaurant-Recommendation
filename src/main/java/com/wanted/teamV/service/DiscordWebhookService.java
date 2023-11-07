package com.wanted.teamV.service;

import com.wanted.teamV.dto.req.DiscordWebhookReqDto;

public interface DiscordWebhookService {
    void sendMessage(DiscordWebhookReqDto request);
}
