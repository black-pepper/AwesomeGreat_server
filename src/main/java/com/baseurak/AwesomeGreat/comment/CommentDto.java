package com.baseurak.AwesomeGreat.comment;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;
/**
 * 로그인한 사용자의 좋아요,신고 상태가 포함된 댓글 정보입니다.
 * @Author: Uju
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CommentDto {
    private Long id;
    private Long postId;
    private Long userId;
    private Timestamp uploadDate;
    private String nickname;
    private String content;
    private int recommend;
    private int myRecommend;
    private int myReport;

    public CommentDto(Comment comment){
        this.id = comment.getId();
        this.postId = comment.getPostId();
        this.userId = comment.getUserId();
        this.uploadDate = comment.getUploadDate();
        this.nickname = comment.getNickname();
        this.content = comment.getContent();
        this.recommend = comment.getRecommend();
    }
}
