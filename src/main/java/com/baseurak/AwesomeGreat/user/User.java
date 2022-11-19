package com.baseurak.AwesomeGreat.user;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Data
@AllArgsConstructor
@Entity
@Table(name="users")
public class User  implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String personalId;
    private String password;
    @Enumerated(EnumType.STRING)
    private Role role;
    private int demerit;

    public User(){}

    @Builder
    public User(String personalId, String password){
        this.personalId = personalId;
        this.password = password;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        ArrayList<GrantedAuthority> gList = new ArrayList<>();
        GrantedAuthority g = new GrantedAuthority() {
            @Override
            public String getAuthority() {
                return "ROLE_" + role.toString();
            }
        };
        gList.add(g);

        return gList;
    }

    @Override
    public String getUsername() {
        return personalId;
    }

    // 계정 만료 확인, true : 만료안됨
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    // 계정 잠금 확인, trueh : 잠금x
    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return false;
    }

    @Override
    public boolean isEnabled() {
        return false;
    }

}
