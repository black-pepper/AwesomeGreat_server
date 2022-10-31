package com.baseurak.AwesomeGreat.admin;

import com.baseurak.AwesomeGreat.comment.Comment;
import com.baseurak.AwesomeGreat.post.Post;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class AdminController {

    private final AdminService adminService;

    @GetMapping("/admin/post")
    public List<Post> allPost(Long postId, int cnt){
        return adminService.readAllPost(postId, cnt);
    }

    @GetMapping("/admin/comment")
    public List<Comment> allComment(Long commentId, int cnt){
        return adminService.readAllComment(commentId, cnt);
    }

    @GetMapping("/admin/block/post")
    public List<Post> blockedPost(Long postId, int cnt){
        return adminService.readBlockedPost(postId, cnt);
    }

    @GetMapping("/admin/block/comment")
    public List<Comment> blockedComment(Long commentId, int cnt){
        return adminService.readBlockedComment(commentId, cnt);
    }

    @PostMapping("/admin/block/post/{id}")
    public void blockPost(@PathVariable("id") Long postId) {
        adminService.blockPost(postId);
    }

    @PostMapping("/admin/block/comment/{id}")
    public void blockComment(@PathVariable("id") Long commentId) {
        adminService.blockComment(commentId);
    }

    @PostMapping("/admin/unblock/post/{id}")
    public void unblockPost(@PathVariable("id") Long postId) {
        adminService.unBlockPost(postId);
    }

    @PostMapping("/admin/unblock/comment/{id}")
    public void unblockComment(@PathVariable("id") Long commentId) {
        adminService.unBlockComment(commentId);
    }

    @PutMapping("/admin/limit/post")
    public void ChangePostLimit(Integer postLimit){
        adminService.ChangePostLimit(postLimit);
    }

    @PutMapping("/admin/limit/comment")
    public void ChangeCommentLimit(Integer commentLimit){
        adminService.ChangePostLimit(commentLimit);
    }
}