package com.baseurak.AwesomeGreat.user;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
@Slf4j
@Service
@RequiredArgsConstructor
public class UserService  implements UserDetailsService {

    //@Autowired
    private final UserRepository userRepository;

    public User findUser(User user) {
        return userRepository.findByPersonalId(user.getPersonalId());
    }

    public void createUser(User user) {
        userRepository.save(user);
    }

    public void deleteUser(String personalId) {
        User user = userRepository.findByPersonalId(personalId);
        userRepository.deleteById(user.getId());
    }

    public List<User> readAllUsers() {
        return userRepository.findAll();
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException { // 시큐리티에서 지정한 서비스이기 때문에 이 메소드를 필수로 구현

        User findUser = userRepository.findByPersonalId(username);
        log.info("loadUserByUsername - username: {}, findUser : {}", username, findUser);
        if (findUser != null){
            return findUser;
        } else {
            log.info("사용자를 찾지 못함");
            throw new UsernameNotFoundException((username));
        }
    }

    public User findUserBySessionId(){
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User findUser = (User)principal;
        return findUser;
    }

}