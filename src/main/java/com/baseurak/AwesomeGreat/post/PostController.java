package com.baseurak.AwesomeGreat.post;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
public class PostController {

    @Autowired
    PostService postService;
    public String redirect = "<meta http-equiv=\"refresh\" content=\"0;url=/main\">";

    @GetMapping("/post") //메인 페이지 글 읽기
    public List<Post> readPosts(Long postId, int cnt) {
        return postService.read(postId, cnt);
    }

    @GetMapping("/user-post")
    public List<Post> readUserPosts(Long userId){
        return postService.findByUserId(userId);
    }

    @GetMapping("/post/{id}") //세부 페이지 글 읽기
    public Post readOnePost(@PathVariable("id") Long postId){
        return postService.read(postId);
    }

    @PostMapping("/post") //새 게시글 작성
    public String writePost(String content) {
        Post post = new Post();
        post.setContent(content);
        postService.write(post);
        return redirect;
    }

    @DeleteMapping("/post/{id}") //게시글 삭제
    public String deletePost(@PathVariable("id") Long postId) {
        postService.delete(postId);
        return "redirect:/";
    }

    @PutMapping("/post") //게시글 수정
    public String modifyPost(Long postId, String content) {
        log.info("postId = {} , content = {}", postId, content);
        postService.modify(postId, content);
        return redirect;
    }
}