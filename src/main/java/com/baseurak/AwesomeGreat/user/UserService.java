package com.baseurak.AwesomeGreat.user;

import com.baseurak.AwesomeGreat.comment.CommentRepository;
import com.baseurak.AwesomeGreat.post.PostRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.stereotype.Service;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService  implements UserDetailsService {

    private final UserRepository userRepository;
    private final PostRepository postRepository;
    private final CommentRepository commentRepository;

    public User findUser(User user) {
        return userRepository.findByEmail(user.getEmail());
    }

    public void create(User user) {
        userRepository.save(user);
    }

    public void delete() {
        User user = findUserBySessionId();
        userRepository.deleteById(user.getId());
        SecurityContextHolder.clearContext();
    }

    public List<User> readUserList() {
        return userRepository.findAll();
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException { // 시큐리티에서 지정한 서비스이기 때문에 이 메소드를 필수로 구현
        User findUser = userRepository.findByEmail(username);
        if (findUser != null){
            return findUser;
        } else {
            log.info("사용자를 찾지 못함");
            throw new UsernameNotFoundException((username));
        }
    }

    public UserInfo findUserInfo() {
        User user = findUserBySessionId();
        if (user == null) return null;
        UserInfo userInfo = new UserInfo();
        userInfo.setUserId(user.getId());
        userInfo.setEmail(user.getEmail());
        userInfo.setRole(user.getRole());
        userInfo.setDemerit(user.getDemerit());
        userInfo.setPostToday(postRepository.countPostToday(user.getId()));
        userInfo.setCommentToday(commentRepository.countCommentToday(user.getId()));
        return userInfo;
    }

    public User findUserBySessionId(){
        String email;
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (principal == "anonymousUser")
            return null;
        if (principal instanceof DefaultOAuth2User){
            log.info("principal = {}", principal);
            //log.info("{}", userRepository.findByEmail(((DefaultOAuth2User) principal).getAttribute("email")).getAuthorities());

            email = ((DefaultOAuth2User) principal).getAttribute("email");
            if(email!=null) return userRepository.findByEmail(email);

            //naver
            Map<String, Object> attribute = (Map<String, Object>) ((DefaultOAuth2User) principal).getAttribute("response");
            email = attribute.get("email").toString();
            if(email!=null) return userRepository.findByEmail(email);

//            //kakao
//            attribute = (Map<String, Object>) ((DefaultOAuth2User) principal).getAttribute("kakao_account");
//            email = attribute.get("email").toString();
//            if(email!=null) return userRepository.findByEmail(email);

        }
        return (User) principal;
    }

}