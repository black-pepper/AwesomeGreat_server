package com.baseurak.AwesomeGreat.admin;

import com.baseurak.AwesomeGreat.comment.Comment;
import com.baseurak.AwesomeGreat.post.Post;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 관리자 관련 요청을 처리하기 위해 해당 로직을 실행합니다.
 * @Author: Uju
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class AdminService {

    private final AdminRepository adminRepository;
    private final NumericalSetting numericalSetting;

    /**
     * postId보다 작은 id를 가진 게시글을 cnt개 가져옵니다.
     */
    public List<Post> readAllPost(Long postId, int cnt){
        return adminRepository.readAllPost(postId, cnt);
    }

    /**
     * commentId보다 작은 id를 가진 댓글을 cnt개 가져옵니다.
     */
    public List<Comment> readAllComment(Long commentId, int cnt){
        return adminRepository.readAllComment(commentId, cnt);
    }

    /**
     * postId보다 작은 id를 가진 차단된 게시글을 cnt개 가져옵니다.
     */
    public List<Post> readBlockedPost(Long postId, int cnt){
        return adminRepository.readBlockedPost(postId, cnt);
    }

    /**
     * commentId보다 작은 id를 가진 댓글을 cnt개 가져옵니다.
     */
    public List<Comment> readBlockedComment(Long commentId, int cnt){
        return adminRepository.readBlockedComment(commentId, cnt);
    }

    /**
     * postId에 해당하는 글의 block을 설정합니다.
     */
    public void blockPost(Long postId, Boolean block){
        adminRepository.setBlockPost(postId, block);
    }

    /**
     * commentId에 해당하는 댓글의 block을 설정합니다.
     */
    public void blockComment(Long commentId, Boolean block){
        adminRepository.setBlockComment(commentId, block);
    }

    /**
     * 하루 최대 게시글 수를 설정합니다.
     */
    public void ChangePostLimit(Integer postLimit){
        numericalSetting.setPostLimit(postLimit);
    }

    /**
     * 하루 최대 댓글 수를 설정합니다.
     */
    public void ChangeCommentLimit(Integer commentLimit){
        numericalSetting.setCommentLimit(commentLimit);
    }

}
