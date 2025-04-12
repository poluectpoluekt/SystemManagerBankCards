package com.ed.sysbankcards.advice.response;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@NoArgsConstructor
@Getter
@Setter
public class ErrorResponse {

    private String message;
    private LocalDateTime timestamp;
}
