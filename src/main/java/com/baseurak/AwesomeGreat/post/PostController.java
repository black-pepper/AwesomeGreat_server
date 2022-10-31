package com.baseurak.AwesomeGreat.post;

import com.baseurak.AwesomeGreat.exception.BadRequestException;
import com.baseurak.AwesomeGreat.user.User;
import com.baseurak.AwesomeGreat.user.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
public class PostController {

    @Autowired
    PostService postService;
    @Autowired
    UserService userService;

    @GetMapping("/post") //메인 페이지 글 읽기
    public List<Post> readPosts(Long postId, int cnt) {
        return postService.read(postId, cnt);
    }

    @GetMapping("/user-post")
    public List<Post> readUserPosts(Long userId, Long postId, Integer cnt){
        if (postId==null) postId = 99999L;
        if (cnt==null) cnt = 10;
        if (userId==null) throw new BadRequestException();
        return postService.findByUserId(userId, postId, cnt);
    }

    @GetMapping("/random")
    public PostDto readRandomPost(){
        return postService.readRandom(userService.findUserBySessionId());
    }

    @GetMapping("/post/{id}") //세부 페이지 글 읽기
    public PostDto readOnePost(@PathVariable("id") Long postId){
        return postService.readOne(postId, userService.findUserBySessionId());
    }

    @PostMapping("/post") //새 게시글 작성
    public void writePost(String content) {
        if (content.isEmpty()) throw new BadRequestException();
        postService.write(content, userService.findUserInfo());
    }

    @DeleteMapping("/post/{id}") //게시글 삭제
    public String deletePost(@PathVariable("id") Long postId) {
        postService.delete(postId, userService.findUserBySessionId());
        return "redirect:/";
    }

    @PutMapping("/post") //게시글 수정
    public void modifyPost(Long postId, String content) {
        if (content.isEmpty()) throw new BadRequestException();
        postService.modify(postId, content, userService.findUserBySessionId());
    }

    @GetMapping("/search") //검색
    public List<Post> search(Long postId, Integer cnt, String keyword) {
        if (postId==null) postId = 99999L;
        if (cnt==null) cnt = 10;
        return postService.search(postId, cnt, keyword);
    }

    @PostMapping("/post/recommend/{id}")
    void addRecommend(@PathVariable("id") Long postId){
        postService.addRecommend(postId, userService.findUserBySessionId());
    }
    @DeleteMapping("/post/recommend/{id}")
    void deleteRecommend(@PathVariable("id") Long postId){
        postService.deleteRecommend(postId, userService.findUserBySessionId());
    }
    @PostMapping("/post/report/{id}")
    void addReport(@PathVariable("id") Long postId){
        postService.addReport(postId, userService.findUserBySessionId());
    }
    @DeleteMapping("/post/report/{id}")
    void deleteReport(@PathVariable("id") Long postId){
        postService.deleteReport(postId, userService.findUserBySessionId());
    }
}
