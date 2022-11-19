package com.baseurak.AwesomeGreat.experience;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
/**
 * 사용자 통계 관련 요청을 HTTP 프로토콜로 받아 처리합니다.
 * @Author: Uju
 */
@Slf4j
@RestController
public class ExperienceController {

    @Autowired ExperienceService experienceService;

    /**
     * GET - /experience/{ussrId} 요청 시 userId에 해당하는 작성한 글 수와 받은 추천 수, 작성한 댓글 수와 받은 추천 수를 찾습니다.
     */
    @GetMapping("/experience/{id}")
    Experience findExperience(@PathVariable("id") Long userId){
        Experience experience = new Experience();

        Long postCount = experienceService.countPost(userId);
        experience.setPostCount((postCount!=null)?postCount:0);

        Long postRecommend = experienceService.sumPostRecommend(userId);
        experience.setPostRecommendSum((postRecommend!=null)?postRecommend:0);

        Long commentCount = experienceService.countComment(userId);
        experience.setCommentCount((commentCount!=null)?commentCount:0);

        Long commentRecommend = experienceService.sumCommentRecommend(userId);
        experience.setCommentRecommendSum((commentRecommend!=null)?commentRecommend:0);

        return experience;
    }
}