package com.ed.sysbankcards.advice.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class RuntimeExceptionResponse {

    private String message;
    private LocalDateTime timestamp;
}
