package com.baseurak.AwesomeGreat.admin;

import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Component;

@Component
@Getter @Setter
public class NumericalSetting {
    private int postLimit = 5;
    private int commentLimit = 10;
}