package com.baseurak.AwesomeGreat.admin;

import com.baseurak.AwesomeGreat.comment.Comment;
import com.baseurak.AwesomeGreat.post.Post;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 관리자 관련 요청을 HTTP 프로토콜로 받아 처리합니다.
 * role이 ADMIN인 사용자만 사용할 수 있습니다.
 * @Author: Uju
 */
@RestController
@RequiredArgsConstructor
public class AdminController {

    private final AdminService adminService;

    /**
     * GET - /admin/post 요청 시 postId보다 작은 id를 가진 게시글을 cnt개 가져옵니다.
     */
    @GetMapping("/admin/post")
    public List<Post> readPostList(Long postId, int cnt){
        return adminService.readAllPost(postId, cnt);
    }

    /**
     * GET - /admin/comment 요청 시 commentId보다 작은 id를 가진 댓글을 cnt개 가져옵니다.
     */
    @GetMapping("/admin/comment")
    public List<Comment> readCommentList(Long commentId, int cnt){
        return adminService.readAllComment(commentId, cnt);
    }

    /**
     * GET - /admin/block/post 요청 시 postId보다 작은 id를 가진 차단된 게시글을 cnt개 가져옵니다.
     */
    @GetMapping("/admin/block/post")
    public List<Post> readBlockedPost(Long postId, int cnt){
        return adminService.readBlockedPost(postId, cnt);
    }

    /**
     * GET - /admin/block/comment 요청 시 commentId보다 작은 id를 가진 차단된 댓글을 cnt개 가져옵니다.
     */
    @GetMapping("/admin/block/comment")
    public List<Comment> readBlockedComment(Long commentId, int cnt){
        return adminService.readBlockedComment(commentId, cnt);
    }

    /**
     * PUT - /admin/block/post 요청 시 postId에 해당하는 게시글의 block을 설정합니다.
     */
    @PutMapping("/admin/block/post")
    public void setBlockPost(Long postId, Boolean block) {
        adminService.blockPost(postId, block);
    }

    /**
     * PUT - /admin/block/comment 요청 시 commentId에 해당하는 댓글의 block을 설정합니다.
     */
    @PutMapping("/admin/block/comment")
    public void setBlockComment(Long commentId, Boolean block) {
        adminService.blockComment(commentId, block);
    }

    /**
     * PUT - /admin/limit/post 요청 시 하루 최대 게시글 작성 개수를 postLimit으로 변경합니다.
     */
    @PutMapping("/admin/limit/post")
    public void setPostLimit(Integer postLimit){
        adminService.ChangePostLimit(postLimit);
    }

    /**
     * PUT - /admin/limit/comment 요청 시 하루 최대 게시글 작성 개수를 commentLimit으로 변경합니다.
     */
    @PutMapping("/admin/limit/comment")
    public void setCommentLimit(Integer commentLimit){
        adminService.ChangeCommentLimit(commentLimit);
    }
}