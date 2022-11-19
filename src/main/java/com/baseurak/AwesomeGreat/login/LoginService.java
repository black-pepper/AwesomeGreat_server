package com.baseurak.AwesomeGreat.login;

import com.baseurak.AwesomeGreat.user.User;
import com.baseurak.AwesomeGreat.user.UserRepository;
import org.springframework.stereotype.Service;

@Service
public class LoginService {
    private UserRepository userRepository;

    public LoginService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User login(String loginId, String password) {
        User findUser = userRepository.findByPersonalId(loginId);
        if (findUser == null) { return null; }
        else { return findUser; }
    }


}
