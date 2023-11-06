package com.wanted.teamV.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/redis")
public class RedisController {

    private final RedisTemplate<String, String> redisTemplate;

    //set
    @PostMapping()
    public String setRedisKey(@RequestBody Map<String, String> req) {
        ValueOperations<String, String> vop = redisTemplate.opsForValue();
        try {
            vop.set(req.get("key").toString(), req.get("value").toString());
            return "set message success";
        } catch (Exception e) {
            return "set message fail";
        }
    }

    //get
    @GetMapping("/{key}")
    public String getRedisKey(@PathVariable String key) {
        ValueOperations<String, String> vop = redisTemplate.opsForValue();
        return vop.get(key);
    }
}
