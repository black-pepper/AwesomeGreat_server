package com.baseurak.AwesomeGreat.post;

import com.baseurak.AwesomeGreat.nickname.NicknameService;
import com.baseurak.AwesomeGreat.user.User;
import com.baseurak.AwesomeGreat.user.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class PostServiceImpl implements PostService {

    private final PostRepository postRepository;
    private final UserService userService;
    private final NicknameService nicknameService;

    @Override
    public void write(Post post) {
        LocalDateTime now = LocalDateTime.now();
        post.setUploadDate(Timestamp.valueOf(now));
        User findUser = userService.findUserBySessionId();
        post.setUserId(findUser.getId());
        post.setNickname(nicknameService.makeNickname());
        post.setReport(0);
        postRepository.create(post);
    }



    @Override
    public List<Post> read(Long postId, int cnt) {
        return postRepository.read(postId, cnt);
    }

    @Override
    public Post read(Long postId) {
        return postRepository.read(postId);
    }

    @Override
    public void modify(Long postId, String contents) {
        if (checkUser(postId)) postRepository.update(postId, contents);
    }

    @Override
    public void delete(Long postId) {
        if (checkUser(postId)) postRepository.delete(postId);
    }

    @Override
    public List<Post> findByUserId(Long userId) {
        return postRepository.findByUserId(userId);
    }

    public Boolean checkUser(Long postId) {
        Long findPostUserId = postRepository.read(postId).getUserId();
        Long findUserId = userService.findUserBySessionId().getId();
        return findPostUserId.equals(findUserId);
    }
}
