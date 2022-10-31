package com.baseurak.AwesomeGreat.post;

import com.baseurak.AwesomeGreat.admin.NumericalSetting;
import com.baseurak.AwesomeGreat.exception.BadRequestException;
import com.baseurak.AwesomeGreat.exception.ForbiddenException;
import com.baseurak.AwesomeGreat.nickname.NicknameService;
import com.baseurak.AwesomeGreat.user.User;
import com.baseurak.AwesomeGreat.user.UserInfo;
import com.baseurak.AwesomeGreat.user.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;
    private final NicknameService nicknameService;
    private final NumericalSetting numericalSetting;

    public Post write(String content, UserInfo findUser) {
        Post post = new Post();
        post.setContent(content);

        if (findUser.getPostToday()>=numericalSetting.getPostLimit()){
            throw new BadRequestException();
        }
        LocalDateTime now = LocalDateTime.now();
        post.setUploadDate(Timestamp.valueOf(now));

        post.setUserId(findUser.getUserId());
        post.setNickname(nicknameService.makeNickname());

        log.info("post: {}", post);
        postRepository.create(post);
        return post;
    }

    public List<Post> read(Long postId, int cnt) {
        return postRepository.read(postId, cnt);
    }

    public Post read(Long postId) {
        return postRepository.read(postId);
    }

    public PostDto readOne(Long postId, User user) {
        Post post = postRepository.read(postId);
        ModelMapper modelMapper = new ModelMapper();
        PostDto postDto = modelMapper.map(post, PostDto.class);

        postDto.setMyRecommend(0);
        postDto.setMyReport(0);
        if (user == null) return postDto; //익명사용자

        PostState postState = postRepository.findPostState(postId, user.getId());
        if (postState != null) {
            postDto.setMyRecommend(postState.getRecommend());
            postDto.setMyReport(postState.getReport());
        }
        return postDto;
    }

    public void modify(Long postId, String contents, User user) {
        if (checkUser(postId, user)) postRepository.update(postId, contents);
        else throw new ForbiddenException();
    }

    public void delete(Long postId, User user) {
        if (checkUser(postId, user)) postRepository.delete(postId);
        else throw new ForbiddenException();
    }

    public void addRecommend(Long postId, User user) {
        Long userId = user.getId();
        postRepository.addRecommend(postId, userId);
    }

    public void deleteRecommend(Long postId, User user) {
        postRepository.deleteRecommend(postId, user.getId());
    }

    public void addReport(Long postId, User user) {
        postRepository.addReport(postId, user.getId());
    }

    public void deleteReport(Long postId, User user) {
        postRepository.deleteReport(postId, user.getId());
    }

    public List<Post> findByUserId(Long userId, Long postId, int cnt) {
        return postRepository.findByUserId(userId, postId, cnt);
    }

    public PostDto readRandom(User user) {
        List<Post> readPost = postRepository.read(99999L, 100);
        Post findPost = readPost.get((int) (Math.random() * readPost.size()));
        return readOne(findPost.getId(), user);
    }

    public List<Post> search(Long postId, int cnt, String keyword) {
        return postRepository.search(postId, cnt, keyword);
    }

    private Boolean checkUser(Long postId, User user) {
        Long findPostUserId = postRepository.read(postId).getUserId();
        return findPostUserId.equals(user.getId());
    }
}