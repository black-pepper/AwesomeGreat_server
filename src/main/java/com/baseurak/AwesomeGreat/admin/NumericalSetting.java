package com.baseurak.AwesomeGreat.admin;

import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Component;
/**
 * 서비스에 필요한 값을 모아둡니다.
 * @Author: Uju
 */
@Component
@Getter @Setter
public class NumericalSetting {
    private int postLimit = 5;
    private int commentLimit = 10;
}