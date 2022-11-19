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

        if (serverDate.get(Calendar.HOUR_OF_DAY)<15){
            serverDate.add(Calendar.DAY_OF_MONTH, -1);
        }

        Date queryDate = new Date(
                serverDate.get(Calendar.YEAR),
                serverDate.get(Calendar.MONTH),
                serverDate.get(Calendar.DAY_OF_MONTH)
        );
        queryDate.setHours(15);
        log.info("day = {}", queryDate);
    }

}