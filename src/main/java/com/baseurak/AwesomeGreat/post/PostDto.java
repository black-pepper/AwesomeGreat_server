package com.baseurak.AwesomeGreat.post;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;
/**
 * 로그인한 사용자의 좋아요,신고 상태가 포함된 게시글 정보입니다.
 * @Author: Uju
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PostDto {
    private Long id;
    private Long userId;
    private Timestamp uploadDate;
    private String nickname;
    private String content;
    private int recommend;
    private int commentCount;
    private int myRecommend;
    private int myReport;
}
