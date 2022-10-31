package com.baseurak.AwesomeGreat;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

@Slf4j
class JavaTest {
    @Test
    void DateTest(){
        LocalDateTime now = LocalDateTime.now();
        Date date = new Date(now.getYear()-1900, now.getMonthValue(), now.getDayOfMonth());
        System.out.println(now.getYear());
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.HOUR, -9);
        Date result = cal.getTime();
        System.out.println(result);
    }

    @Test
    void ChangeDateTest(){
        Calendar serverDate = Calendar.getInstance();
        serverDate.setTime(new Date());
        serverDate.add(Calendar.HOUR, 9);
        Date koreanDate = serverDate.getTime();
        koreanDate.setHours(0);
        koreanDate.setMinutes(0);
        koreanDate.setSeconds(0);
        Calendar result = Calendar.getInstance();
        result.setTime(koreanDate);
        result.add(Calendar.HOUR, -9);
        log.info("day = {}", result.getTime());

    }
}