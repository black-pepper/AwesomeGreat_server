package com.baseurak.AwesomeGreat.post;

import com.baseurak.AwesomeGreat.admin.NumericalSetting;
import com.baseurak.AwesomeGreat.exception.BadRequestException;
import com.baseurak.AwesomeGreat.exception.ForbiddenException;
import com.baseurak.AwesomeGreat.nickname.NicknameService;
import com.baseurak.AwesomeGreat.user.User;
import com.baseurak.AwesomeGreat.user.UserInfo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;
/**
 * 게시글 관련 요청을 처리하기 위해 해당 로직을 실행합니다.
 * @Author: Uju
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;
    private final NicknameService nicknameService;
    private final NumericalSetting numericalSetting;

    /**
     * 새로운 게시글을 저장합니다.
     * @param userInfo: 사용자가 게시글을 작성할 수 있는지 확인하고 게시글에 사용자의 정보를 저장합니다.
     */
    public Post write(String content, UserInfo userInfo) {
        Post post = new Post();
        post.setContent(content);

        if (userInfo.getPostToday()>=numericalSetting.getPostLimit()){
            throw new BadRequestException();
        }
        LocalDateTime now = LocalDateTime.now();
        post.setUploadDate(Timestamp.valueOf(now));

        post.setUserId(userInfo.getUserId());
        post.setNickname(nicknameService.getNickname());

        postRepository.write(post);
        return post;
    }

    /**
     * 차단되지 않은 postId이하의 게시글을 최신순으로 cnt개 가져옵니다.
     */
    public List<Post> readPostList(Long postId, int cnt) {
        return postRepository.readPostList(postId, cnt);
    }

    /**
     * postId에 해당하는 게시글을 가져옵니다.
     */
    public Post readPost(Long postId) {
        return postRepository.readPost(postId);
    }

    /**
     * postId에 해당하는 게시글을 가져옵니다.
     * @return user의 추천/신고 정보가 추가된 PostDto를 반환합니다.
     */
    public PostDto readPostDto(Long postId, User user) {
        Post post = postRepository.readPost(postId);
        ModelMapper modelMapper = new ModelMapper();
        PostDto postDto = modelMapper.map(post, PostDto.class);

        postDto.setMyRecommend(0);
        postDto.setMyReport(0);
        if (user == null) return postDto; //익명사용자

        PostState postState = postRepository.readPostState(postId, user.getId());
        if (postState != null) {
            postDto.setMyRecommend(postState.getRecommend());
            postDto.setMyReport(postState.getReport());
        }
        return postDto;
    }

    /**
     * postId에 해당하는 게시글의 내용을 content로 수정합니다.
     * @param user: 요청한 사용자와 해당 게시글을 작성한 사용자가 같은지 확인합니다.
     */
    public void modify(Long postId, String content, User user) {
        if (checkUser(postId, user)) postRepository.modify(postId, content);
        else throw new ForbiddenException();
    }

    /**
     * postId에 해당하는 게시글을 삭제합니다.
     * @param user: 요청한 사용자와 해당 게시글을 작성한 사용자가 같은지 확인합니다.
     */
    public void delete(Long postId, User user) {
        if (checkUser(postId, user)) postRepository.delete(postId);
        else throw new ForbiddenException();
    }

    /**
     * userId가 작성한 게시글을 가져옵니다.
     * @param postId: postId보다 작은 id를 가진 게시글을 가져옵니다.
     * @param cnt: 가져오는 게시글의 최대 개수 입니다.
     */
    public List<Post> readPostListByUserId(Long userId, Long postId, int cnt) {
        return postRepository.readPostListByUserId(userId, postId, cnt);
    }

    /**
     * 최신 100개의 게시글 중 하나를 랜덤으로 가져옵니다.
     * @return user의 추천/신고 정보가 추가된 PostDto를 반환합니다.
     */
    public PostDto readPostDtoRandom(User user) {
        List<Post> readPost = postRepository.readPostList(99999L, 100);
        Post findPost = readPost.get((int) (Math.random() * readPost.size()));
        return readPostDto(findPost.getId(), user);
    }

    /**
     * keyword가 포함된 게시글을 가져옵니다.
     * @param postId: postId보다 작은 id를 가진 게시글을 가져옵니다.
     * @param cnt: 가져오는 글의 최대 개수입니다.
     */
    public List<Post> readPostListByKeyword(Long postId, int cnt, String keyword) {
        return postRepository.readPostListByKeyword(postId, cnt, keyword);
    }

    /**
     * postId에 해당하는 게시글의 작성자와 user가 같은지 확인합니다.
     */
    private Boolean checkUser(Long postId, User user) {
        Long findPostUserId = postRepository.readPost(postId).getUserId();
        return findPostUserId.equals(user.getId());
    }

    /**
     * user와 postId에 해당하는 게시글의 추천 상태를 recommend로 변경합니다.
     */
    public void setRecommend(Long postId, int recommend, User userBySessionId) {
        postRepository.setRecommend(postId, userBySessionId.getId(), recommend);
    }

    /**
     * user와 postId에 해당하는 게시글의 신고 상태를 report로 변경합니다.
     */
    public void setReport(Long postId, int report, User userBySessionId) {
        postRepository.setReport(postId, userBySessionId.getId(), report);
    }

    /**
     * user가 postId에 해당하는 게시글을 추천합니다.
     * @deprecated 프론트엔드의 추천/신고를 PUT방식으로 변경후 제거 예정입니다.
     */
    @Deprecated
    public void addRecommend(Long postId, User user) {
        Long userId = user.getId();
        postRepository.addRecommend(postId, userId);
    }
    /**
     * user가 postId에 해당하는 게시글의 추천을 취소합니다.
     * @deprecated 프론트엔드의 추천/신고를 PUT방식으로 변경후 제거 예정입니다.
     */
    @Deprecated
    public void deleteRecommend(Long postId, User user) {
        postRepository.deleteRecommend(postId, user.getId());
    }
    /**
     * user가 postId에 해당하는 게시글을 신고합니다.
     * @deprecated 프론트엔드의 추천/신고를 PUT방식으로 변경후 제거 예정입니다.
     */
    @Deprecated
    public void addReport(Long postId, User user) {
        postRepository.addReport(postId, user.getId());
    }
    /**
     * user가 postId에 해당하는 게시글의 신고를 취소합니다.
     * @deprecated 프론트엔드의 추천/신고를 PUT방식으로 변경후 제거 예정입니다.
     */
    @Deprecated
    public void deleteReport(Long postId, User user) {
        postRepository.deleteReport(postId, user.getId());
    }
}