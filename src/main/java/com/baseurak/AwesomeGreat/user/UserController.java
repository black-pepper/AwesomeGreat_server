package com.baseurak.AwesomeGreat.user;

import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;

@Slf4j
@RestController
public class UserController {
    @Autowired
    UserService userService;
    @Autowired
    PasswordEncoder passwordEncoder;

    @GetMapping("/")
    public HashMap<String, String> findUser() {
        User user = userService.findUserBySessionId();
        HashMap<String, String> userInfo = new HashMap<>();
        userInfo.put("userId", String.valueOf(user.getId()));
        userInfo.put("personalId", user.getPersonalId());
        userInfo.put("role", String.valueOf(user.getRole()));
        userInfo.put("demerit", String.valueOf(user.getDemerit()));
        return userInfo;
    }

    @PostMapping("/user")
    public /*Message*/void createUser(UserDto userDto) { //

//        ModelMapper modelMapper = new ModelMapper();
//        User newUser = modelMapper.map(userDto, User.class);
        User newUser = new User();
        newUser.setPersonalId(userDto.getPersonalId());
        newUser.setPassword(passwordEncoder.encode(userDto.getPassword())); //비밀번호 암호화
        newUser.setRole(Role.USER);
        newUser.setDemerit(0);
        userService.createUser(newUser);

//        return message;
    }

    //@GetMapping("/user")
    public List<User> readUsers(){
        return userService.readAllUsers();
    }

    @GetMapping("/user")
    public User readUser(){
        return userService.findUserBySessionId();
    }
}
