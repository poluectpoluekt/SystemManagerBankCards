package com.ed.sysbankcards.exception;

public class ErrorValueIdempotencyKeyException extends RuntimeException{

    public ErrorValueIdempotencyKeyException() {
        super("Incorrect idempotency key");
    }
}
