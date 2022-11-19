package com.baseurak.AwesomeGreat.experience;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
/**
 * 사용자의 게시글과 댓글 통계 정보입니다.
 * @Author: Uju
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Experience {
    private Long postCount;
    private Long postRecommendSum;
    private Long commentCount;
    private Long commentRecommendSum;
}
