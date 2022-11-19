package com.baseurak.AwesomeGreat.user;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
/**
 * 오늘 작성한 글, 댓글 개수가 포함된 사용자 정보입니다.
 * @Author: Uju
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserInfo {
    private Long userId;
    private String email;
    @Enumerated(EnumType.STRING)
    private Role role;
    private int demerit;
    private Long postToday;
    private Long commentToday;

    public UserInfo(User user) {
        this.userId = user.getId();
        this.email = user.getEmail();
        this.role = user.getRole();
        this.demerit = user.getDemerit();
    }
}