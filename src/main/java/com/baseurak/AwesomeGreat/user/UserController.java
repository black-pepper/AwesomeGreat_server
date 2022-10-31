package com.baseurak.AwesomeGreat.user;

import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;

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
    public void createUser(UserDto userDto) { //
        User newUser = new User();
        newUser.setEmail(userDto.getEmail());
        newUser.setPassword(passwordEncoder.encode(userDto.getPassword())); //비밀번호 암호화
        newUser.setRole(Role.USER);
        newUser.setDemerit(0);
        userService.createUser(newUser);
    }

    //@GetMapping("/user")
    public List<User> readUsers(){
        return userService.readAllUsers();
    }

    @DeleteMapping("/user")
    public void deleteUser(){
        userService.deleteUser();
    }
}
