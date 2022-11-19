package com.baseurak.AwesomeGreat.security;

import com.baseurak.AwesomeGreat.user.User;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;

import java.util.ArrayList;

@Getter @Setter
public class UserContext extends org.springframework.security.core.userdetails.User {
    private User user;

    public UserContext(User user, ArrayList<GrantedAuthority> roles) {
        super(user.getPersonalId(), user.getPassword(), roles);
        this.user = user;
    }
}