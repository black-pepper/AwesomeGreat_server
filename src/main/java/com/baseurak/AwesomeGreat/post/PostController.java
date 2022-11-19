package com.baseurak.AwesomeGreat.post;

import com.baseurak.AwesomeGreat.exception.BadRequestException;
import com.baseurak.AwesomeGreat.user.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
/**
 * 게시글 관련 요청을 HTTP 프로토콜로 받아 처리합니다.
 * @Author: Uju
 */
@Slf4j
@RestController
public class PostController {

    @Autowired
    PostService postService;
    @Autowired
    UserService userService;

    /**
     * GET - /post 요청 시 postId보다 작은 id를 가진 게시글을 cnt개 가져옵니다.
     */
    @GetMapping("/post") //메인 페이지 글 읽기
    public List<Post> readPostList(Long postId, int cnt) {
        return postService.readPostList(postId, cnt);
    }

    /**
     * GET - /post 요청 시 userId가 작성한 게시글을 가져옵니다.
     * @param postId: postId보다 작은 id를 가진 게시글을 가져옵니다. 기본값은 99999입니다.
     * @param cnt: 가져오는 글의 최대 개수입니다. 기본값은 10입니다.
     */
    @GetMapping("/user-post")
    public List<Post> readPostListByUserId(Long userId, Long postId, Integer cnt){
        if (postId==null) postId = 99999L;
        if (cnt==null) cnt = 10;
        if (userId==null) throw new BadRequestException();
        return postService.readPostListByUserId(userId, postId, cnt);
    }

    /**
     * GET - /post 요청 시 최신 100개의 게시글 중 하나를 랜덤으로 가져옵니다.
     */
    @GetMapping("/random")
    public PostDto readRandomPostDto(){
        return postService.readPostDtoRandom(userService.findUserBySessionId());
    }

    /**
     * GET - /post/{postId} 요청 시 postId에 해당하는 게시글을 가져옵니다.
     * @return 로그인한 유저의 추천/신고 정보가 추가된 postDto를 리턴합니다.
     */
    @GetMapping("/post/{id}") //세부 페이지 글 읽기
    public PostDto readPostDto(@PathVariable("id") Long postId){
        return postService.readPostDto(postId, userService.findUserBySessionId());
    }

    /**
     * POST - /post 요청 시 내용이 content인 새로운 게시글을 저장합니다.
     * 로그인된 사용자만 사용할 수 있습니다.
     */
    @PostMapping("/post") //새 게시글 작성
    public void write(String content) {
        if (content.isEmpty()) throw new BadRequestException();
        postService.write(content, userService.findUserInfo());
    }

    /**
     * DELETE - /post/{postId} 요청 시 postId에 해당하는 게시글 삭제합니다.
     * 해당 게시글을 작성한 사용자만 사용할 수 있습니다.
     */
    @DeleteMapping("/post/{id}") //게시글 삭제
    public void delete(@PathVariable("id") Long postId) {
        postService.delete(postId, userService.findUserBySessionId());
    }

    /**
     * PUT - /post 요청 시 postId에 해당하는 게시글의 내용을 content로 수정합니다.
     * 해당 게시글을 작성한 사용자만 사용할 수 있습니다.
     */
    @PutMapping("/post") //게시글 수정
    public void modify(Long postId, String content) {
        if (content.isEmpty()) throw new BadRequestException();
        postService.modify(postId, content, userService.findUserBySessionId());
    }

    /**
     * GET - /search 요청 시 keyword가 포함된 게시글을 가져옵니다.
     * @param postId: postId보다 작은 id를 가진 게시글을 가져옵니다. 기본값은 99999입니다.
     * @param cnt: 가져오는 글의 최대 개수입니다. 기본값은 10입니다.
     */
    @GetMapping("/search") //검색
    public List<Post> search(Long postId, Integer cnt, String keyword) {
        if (postId==null) postId = 99999L;
        if (cnt==null) cnt = 10;
        return postService.readPostListByKeyword(postId, cnt, keyword);
    }

    /**
     * PUT - /post/recommend 요청 시 postId에 해당하는 게시글의 추천 상태를 recommend로 변경합니다.
     * 로그인된 사용자만 사용할 수 있습니다.
     */
    @PutMapping("/post/recommend")
    void setRecommend(Long postId, Integer recommend) {
        postService.setRecommend(postId, recommend, userService.findUserBySessionId());
    }

    /**
     * PUT - /post/report 요청 시 postId에 해당하는 게시글의 신고 상태를 report로 변경합니다.
     * 로그인된 사용자만 사용할 수 있습니다.
     */
    @PutMapping("/post/report")
    void setReport(Long postId, Integer report) {
        postService.setReport(postId, report, userService.findUserBySessionId());
    }

    /**
     * POST - /post/recommend/{postId} postId에 해당하는 게시글을 추천합니다.
     * @deprecated 프론트엔드의 추천/신고를 PUT방식으로 변경후 제거 예정입니다.
     */
    @PostMapping("/post/recommend/{id}")
    void addRecommend(@PathVariable("id") Long postId){
        postService.addRecommend(postId, userService.findUserBySessionId());
    }
    /**
     * POST - /post/recommend/{postId} postId에 해당하는 게시글의 추천을 취소합니다.
     * @deprecated 프론트엔드의 추천/신고를 PUT방식으로 변경후 제거 예정입니다.
     */
    @DeleteMapping("/post/recommend/{id}")
    void deleteRecommend(@PathVariable("id") Long postId){
        postService.deleteRecommend(postId, userService.findUserBySessionId());
    }
    /**
     * POST - /post/recommend/{postId} postId에 해당하는 게시글을 신고합니다.
     * @deprecated 프론트엔드의 추천/신고를 PUT방식으로 변경후 제거 예정입니다.
     */
    @PostMapping("/post/report/{id}")
    void addReport(@PathVariable("id") Long postId){
        postService.addReport(postId, userService.findUserBySessionId());
    }
    /**
     * POST - /post/recommend/{postId} postId에 해당하는 게시글의 신고를 취소합니다.
     * @deprecated 프론트엔드의 추천/신고를 PUT방식으로 변경후 제거 예정입니다.
     */
    @DeleteMapping("/post/report/{id}")
    void deleteReport(@PathVariable("id") Long postId){
        postService.deleteReport(postId, userService.findUserBySessionId());
    }
}
