package com.wanted.teamV.controller;

import com.wanted.teamV.dto.LoginMember;
import com.wanted.teamV.dto.req.RatingCreateReqDto;
import com.wanted.teamV.service.RatingService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/ratings")
public class RatingController {
    private final RatingService ratingService;

    @PostMapping
    public ResponseEntity<Void> createRating(
            @AuthenticationPrincipal LoginMember loginMember,
            @Valid @RequestBody RatingCreateReqDto request
    ) {
        ratingService.createRating(loginMember.id(), request);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

}
