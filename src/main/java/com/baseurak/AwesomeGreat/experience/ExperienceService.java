package com.baseurak.AwesomeGreat.experience;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
/**
 * 사용자 통계 관련 요청을 처리하기 위해 해당 로직을 실행합니다.
 * @Author: Uju
 */
@Service
@RequiredArgsConstructor
public class ExperienceService {

    final ExperienceRepository experienceRepository;

    /**
     * userId에 해당하는 게시글의 수를 가져옵니다.
     */
    public Long countPost(Long userId){
        return experienceRepository.countPost(userId);
    }

    /**
     * userId에 해당하는 게시글에 받은 추천 수를 가져옵니다.
     */
    public Long sumPostRecommend(Long userId){
        return experienceRepository.sumPostRecommend(userId);
    }

    /**
     * userId에 해당하는 댓글의 수를 가져옵니다.
     */
    public Long countComment(Long userId) {
        return experienceRepository.countComment(userId);
    }

    /**
     * userId에 해당하는 댓글의 추천 수를 가져옵니다.
     */
    public Long sumCommentRecommend(Long userId) {
        return experienceRepository.sumCommentRecommend(userId);
    }
}
