package com.ed.sysbankcards.service;

import com.ed.sysbankcards.exception.ErrorValueIdempotencyKeyException;
import com.ed.sysbankcards.model.entity.BaseEntity;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.concurrent.TimeUnit;

@Slf4j
@RequiredArgsConstructor
@Service
public class IdempotencyService {

//    private final RedisTemplate<String, BaseEntity> redisTemplate;
//
//    public boolean idempotencyKeyCheck(String idempotencyKey) {
//
//        if (idempotencyKey.isBlank()) {
//            throw new ErrorValueIdempotencyKeyException();
//        }
//
//        return redisTemplate.hasKey(idempotencyKey);
//
//    }
//
//    @Transactional(readOnly = true)
//    public BaseEntity getResultByIdempotencyKey(String idempotencyKey) {
//
//        return redisTemplate.opsForValue().get(idempotencyKey);
//    }
//
//    @Transactional
//    public void saveIdempotencyKey(String idempotencyKey, BaseEntity resultMethod, long ttlSecond) {
//        redisTemplate.opsForValue().set(idempotencyKey, resultMethod, ttlSecond, TimeUnit.SECONDS);
//    }
}
