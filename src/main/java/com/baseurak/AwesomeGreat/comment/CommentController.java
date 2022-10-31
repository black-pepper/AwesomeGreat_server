package com.baseurak.AwesomeGreat.comment;

import com.baseurak.AwesomeGreat.exception.BadRequestException;
import com.baseurak.AwesomeGreat.user.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 댓글 관련 요청을 HTTP 프로토콜로 받아 처리합니다.
 * @Author: Uju
 */
@Slf4j
@RestController
public class CommentController {
    @Autowired
    CommentService commentService;
    @Autowired
    UserService userService;

    /**
     * GET - /comment/{postid} 요청 시 postid에 해당하는 게시글의 댓글을 모두 가져옵니다.
     * @return 로그인한 유저의 추천/신고 정보가 추가된 CommentDto 리스트를 리턴합니다.
     */
    @GetMapping("/comment/{id}") //댓글 읽기
    public List<CommentDto> readCommentDtoList(@PathVariable("id") Long postId)
    {
        return commentService.readCommentDtoList(postId, userService.findUserBySessionId());
    }

    /**
     * GET - /user-comment 요청 시 userId가 작성한 댓글을 가져옵니다.
     * @param commentId: commentId보다 작은 id를 가진 댓글을 가져옵니다. 기본값은 99999입니다.
     * @param cnt: 가져오는 댓글의 최대 개수 입니다. 기본값은 10입니다.
     */
    @GetMapping("/user-comment")
    public List<Comment> readCommentByUser(Long userId, Long commentId, Integer cnt)
    {
        if(commentId == null) commentId = 99999L;
        if(cnt == null) cnt = 10;
        if (userId==null) throw new BadRequestException();
        return commentService.readCommentListByUserId(userId, commentId, cnt);
    }

    /**
     * POST - /comment 요청 시 postId에 해당하는 게시글의 댓글에 내용이 content인 새로운 댓글을 저장합니다.
     * 로그인된 사용자만 사용할 수 있습니다.
     */
    @PostMapping("/comment") //새 댓글 작성
    public void write(String content, Long postId) {
        if (content.isEmpty()) throw new BadRequestException();
        commentService.write(postId, content, userService.findUserInfo());
    }

    /**
     * DELETE - /comment/{commentId} 요청 시 commentId에 해당하는 댓글 삭제합니다.
     * 해당 댓글을 작성한 사용자만 사용할 수 있습니다.
     */
    @DeleteMapping("/comment/{id}") //댓글 삭제
    public void delete(@PathVariable("id") Long commentId) {
        commentService.delete(commentId, userService.findUserBySessionId());
    }

    /**
     * PUT - /comment 요청 시 commentId에 해당하는 댓글의 내용을 content로 수정합니다.
     * 해당 댓글을 작성한 사용자만 사용할 수 있습니다.
     */
    @PutMapping("/comment") //댓글 수정
    public void modify(Long commentId, String content) {
        if (content.isEmpty()) throw new BadRequestException();
        commentService.modify(commentId, content, userService.findUserBySessionId());
    }

    /**
     * PUT - /comment/recommend 요청 시 commentId에 해당하는 댓글의 추천 상태를 recommend로 변경합니다.
     * 로그인된 사용자만 사용할 수 있습니다.
     */
    @PutMapping("/comment/recommend")
    public void setRecommend(Long commentId, Integer recommend) {
        commentService.setRecommend(commentId, userService.findUserBySessionId(), recommend);
    }

    /**
     * PUT - /comment/report 요청 시 commentId에 해당하는 댓글의 신고 상태를 report로 변경합니다.
     * 로그인된 사용자만 사용할 수 있습니다.
     */
    @PutMapping("/comment/report")
    public void setReport(Long commentId, Integer report) {
        commentService.setReport(commentId, userService.findUserBySessionId(), report);
    }

    /**
     * POST - /comment/recommend/{commentId} commentId에 해당하는 댓글을 추천합니다.
     * @deprecated 프론트엔드의 추천/신고를 PUT방식으로 변경후 제거 예정입니다.
     */
    @PostMapping("/comment/recommend/{id}")
    void addRecommend(@PathVariable("id") Long commentId){
        commentService.addRecommend(commentId, userService.findUserBySessionId());
    }
    /**
     * DELETE - /comment/recommend/{commentId} commentId에 해당하는 댓글의 추천을 취소합니다.
     * @deprecated 프론트엔드의 추천/신고를 PUT방식으로 변경후 제거 예정입니다.
     */
    @DeleteMapping("/comment/recommend/{id}")
    void deleteRecommend(@PathVariable("id") Long commentId){
        commentService.deleteRecommend(commentId, userService.findUserBySessionId());
    }
    /**
     * POST - /comment/report/{commentId} commentId에 해당하는 댓글을 신고합니다.
     * @deprecated 프론트엔드의 추천/신고를 PUT방식으로 변경후 제거 예정입니다.
     */
    @PostMapping("/comment/report/{id}")
    void addReport(@PathVariable("id") Long commentId){
        commentService.addReport(commentId, userService.findUserBySessionId());
    }
    /**
     * DELETE - /comment/report/{commentId} commentId에 해당하는 댓글의 신고를 취소합니다.
     * @deprecated 프론트엔드의 추천/신고를 PUT방식으로 변경후 제거 예정입니다.
     */
    @DeleteMapping("/comment/report/{id}")
    void deleteReport(@PathVariable("id") Long commentId){
        commentService.deleteReport(commentId, userService.findUserBySessionId());
    }
}