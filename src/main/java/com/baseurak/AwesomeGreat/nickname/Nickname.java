package com.baseurak.AwesomeGreat.nickname;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
/**
 * 닉네임 정보입니다.
 * sequence가 0이면 명사, 1이면 형용사 입니다.
 * @Author: Uju
 */
@Data
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Nickname {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String nickname;
    private int sequence;
    public Nickname (String nickname, int sequence){
        this.nickname = nickname;
        this.sequence = sequence;
    }
}