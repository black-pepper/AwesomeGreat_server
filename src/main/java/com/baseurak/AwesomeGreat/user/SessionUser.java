package com.baseurak.AwesomeGreat.user;

import lombok.Getter;

import java.io.Serializable;

@Getter
public class SessionUser implements Serializable {
    private String personalId;
    private String email;

    public SessionUser(User user) {
        this.personalId = user.getEmail();
        this.email = user.getEmail();
    }
}