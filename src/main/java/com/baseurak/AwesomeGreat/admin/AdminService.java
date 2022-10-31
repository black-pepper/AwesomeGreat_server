package com.baseurak.AwesomeGreat.admin;

import com.baseurak.AwesomeGreat.comment.Comment;
import com.baseurak.AwesomeGreat.post.Post;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class AdminService {

    private final AdminRepository adminRepository;
    private final NumericalSetting numericalSetting;

    //전체 게시글
    public List<Post> readAllPost(Long postId, int cnt){
        return adminRepository.readAllPost(postId, cnt);
    }

    //전체 댓글
    public List<Comment> readAllComment(Long commentId, int cnt){
        return adminRepository.readAllComment(commentId, cnt);
    }

    //차단된 게시글 보기
    public List<Post> readBlockedPost(Long postId, int cnt){
        return adminRepository.readBlockedPost(postId, cnt);
    }

    //차단된 댓글 보기
    public List<Comment> readBlockedComment(Long commentId, int cnt){
        return adminRepository.readBlockedComment(commentId, cnt);
    }

    //게시글 차단
    public void blockPost(Long postId){
        adminRepository.blockPost(postId);
    }

    //댓글 차단
    public void blockComment(Long commentId){
        adminRepository.blockComment(commentId);
    }

    //게시물 차단 해제
    public void unBlockPost(Long postId){
        adminRepository.unBlockPost(postId);
    }

    //댓글 차단 해제
    public void unBlockComment(Long commentId){
        adminRepository.unBlockComment(commentId);
    }

    public void ChangePostLimit(Integer postLimit){
        numericalSetting.setPostLimit(postLimit);
    }

    public void ChangeCommentLimit(Integer commentLimit){
        numericalSetting.setCommentLimit(commentLimit);
    }

}
