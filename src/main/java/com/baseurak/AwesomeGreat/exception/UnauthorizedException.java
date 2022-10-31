package com.baseurak.AwesomeGreat.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@Slf4j
@ResponseStatus(HttpStatus.UNAUTHORIZED)
public class UnauthorizedException extends RuntimeException {
    public UnauthorizedException() {
        log.info("익명 사용자 예외 발생");
    }
    public UnauthorizedException(String message) {
        super(message);
        log.info("익명 사용자 예외 발생");
    }
}