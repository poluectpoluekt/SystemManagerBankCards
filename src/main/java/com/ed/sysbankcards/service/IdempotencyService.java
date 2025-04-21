package com.ed.sysbankcards.service;

import com.ed.sysbankcards.exception.ErrorValueIdempotencyKeyException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.concurrent.TimeUnit;

@Slf4j
@RequiredArgsConstructor
@Service
public class IdempotencyService {

    private final RedisTemplate<String, Object> redisTemplate;

    public boolean idempotencyKeyCheck(String idempotencyKey) {

        if (idempotencyKey.isBlank()) {
            throw new ErrorValueIdempotencyKeyException();
        }
        return redisTemplate.hasKey(idempotencyKey);

    }

    @Transactional(readOnly = true)
    public Object getResultByIdempotencyKey(String idempotencyKey) {

        return redisTemplate.opsForValue().get(idempotencyKey);
    }

    @Transactional
    public void saveIdempotencyKey(String idempotencyKey, Object resultMethod, long ttlSecond) {
        redisTemplate.opsForValue().set(idempotencyKey, resultMethod, ttlSecond, TimeUnit.SECONDS);
    }
}
