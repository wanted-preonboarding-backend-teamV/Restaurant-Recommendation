package com.wanted.teamV.dto.req;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import org.springframework.beans.factory.annotation.Value;

import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class DiscordWebhookReqDto {

    @Value("${discord.bot}")
    private String username;

    @JsonProperty("avatar_url")
    private String avatarUrl;
    private String content;

    @Setter
    private List<Embed> embeds;

    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class Embed {

        private Author author;
        private String title;
        private String url;
        private int color;
        private List<Field> fields;
        private Footer footer;

        public void addBlankField() {
            Field field = new Field("", "", false);
            fields.add(field);
        }
    }

    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class Author {
        private String name;
        private String url;
        @JsonProperty("icon_url")
        private String iconUrl;
    }

    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class Field {

        private String name;
        private String value;
        private boolean inline;

    }

    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class Footer {
        private String text;
        @JsonProperty("icon_url")
        private String iconUrl;
    }

}
