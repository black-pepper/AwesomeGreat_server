package com.baseurak.AwesomeGreat.comment;

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
    CommentRepository commentRepository;

    public String redirect(Long postId){
        return "<meta http-equiv=\"refresh\" content=\"0;url=/post/"+ postId.toString() +"\">";
    }

    @GetMapping("/comment/{id}") //댓글 읽기
    public List<Comment> readComments(@PathVariable("id") Long postId)
    {
        return commentService.read(postId);
    }

    @GetMapping("/user-comment")
    public List<Comment> readUserComments(Long userId)
    {
        return commentService.read(userId);
    }

    @PostMapping("/comment") //새 댓글 작성
    public String writeComment(String content, Long postId) {
        Comment comment = new Comment();
        comment.setPostId(postId);
        comment.setContent(content);
        commentService.write(comment);
        return redirect(comment.getPostId());
    }

    @DeleteMapping("/comment/{id}") //댓글 삭제
    public String deleteComment(@PathVariable("id") Long id) {
        commentService.delete(id);
        return "<meta http-equiv=\"refresh\" content=\"0;url=/\">";
    }

    @PutMapping("/comment") //댓글 수정
    public String modifyComment(Long commentId, String content) {
        commentService.modify(commentId, content);
        return redirect(commentRepository.readById(commentId).getPostId());
    }
}