package com.baseurak.AwesomeGreat.user;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
/**
 * 일반 로그인 관련 요청을 HTTP 프로토콜로 받아 처리합니다.
 * @Author: Uju, Ru
 */
@Slf4j
@RestController
public class UserController {
    @Autowired
    UserService userService;
    @Autowired
    PasswordEncoder passwordEncoder;

    @GetMapping("/user")
    public UserInfo findUser() {
        return userService.findUserInfo();
    }

    @PostMapping("/user")
    public void create(UserDto userDto) { //
        User newUser = new User();
        newUser.setEmail(userDto.getEmail());
        newUser.setPassword(passwordEncoder.encode(userDto.getPassword())); //비밀번호 암호화
        newUser.setRole(Role.USER);
        newUser.setDemerit(0);
        userService.create(newUser);
    }

    //@GetMapping("/user")
    public List<User> readUserList(){
        return userService.readUserList();
    }

    @DeleteMapping("/user")
    public void delete(){
        userService.delete();
    }
}
