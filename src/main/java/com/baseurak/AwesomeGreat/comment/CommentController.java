package com.baseurak.AwesomeGreat.comment;

import com.baseurak.AwesomeGreat.exception.BadRequestException;
import com.baseurak.AwesomeGreat.user.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
public class CommentController {
    @Autowired
    CommentService commentService;
    @Autowired
    UserService userService;

    @GetMapping("/comment/{id}") //댓글 읽기
    public List<CommentDto> readComments(@PathVariable("id") Long postId)
    {
        return commentService.read(postId, userService.findUserBySessionId());
    }

    @GetMapping("/user-comment")
    public List<Comment> readUserComments(Long userId, Long commentId, Integer cnt)
    {
        if(commentId == null) commentId = 99999L;
        if(cnt == null) cnt = 10;
        if (userId==null) throw new BadRequestException();
        return commentService.findByUserId(userId, commentId, cnt);
    }

    @PostMapping("/comment") //새 댓글 작성
    public void writeComment(String content, Long postId) {
        if (content.isEmpty()) throw new BadRequestException();
        commentService.write(postId, content, userService.findUserInfo());
    }

    @DeleteMapping("/comment/{id}") //댓글 삭제
    public void deleteComment(@PathVariable("id") Long id) {
        commentService.delete(id, userService.findUserBySessionId());
    }

    @PutMapping("/comment") //댓글 수정
    public void modifyComment(Long commentId, String content) {
        if (content.isEmpty()) throw new BadRequestException();
        commentService.modify(commentId, content, userService.findUserBySessionId());
    }

    @PostMapping("/comment/recommend/{id}")
    void addRecommend(@PathVariable("id") Long commentId){
        commentService.addRecommend(commentId, userService.findUserBySessionId());
    }
    @DeleteMapping("/comment/recommend/{id}")
    void deleteRecommend(@PathVariable("id") Long commentId){
        commentService.deleteRecommend(commentId, userService.findUserBySessionId());
    }
    @PostMapping("/comment/report/{id}")
    void addReport(@PathVariable("id") Long commentId){
        commentService.addReport(commentId, userService.findUserBySessionId());
    }
    @DeleteMapping("/comment/report/{id}")
    void deleteReport(@PathVariable("id") Long commentId){
        commentService.deleteReport(commentId, userService.findUserBySessionId());
    }
}