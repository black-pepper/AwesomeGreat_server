package com.baseurak.AwesomeGreat.experience;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
public class ExperienceController {

    @Autowired ExperienceService experienceService;

    @GetMapping("/experience/{id}")
    Experience postSum(@PathVariable("id") Long userId){
        Experience experience = new Experience();

        Long postCount = experienceService.postCount(userId);
        experience.setPostCount((postCount!=null)?postCount:0);

        Long postRecommend = experienceService.postRecommendSum(userId);
        experience.setPostRecommendSum((postRecommend!=null)?postRecommend:0);

        Long commentCount = experienceService.commentCount(userId);
        experience.setCommentCount((commentCount!=null)?commentCount:0);

        Long commentRecommend = experienceService.commentRecommendSum(userId);
        experience.setCommentRecommendSum((commentRecommend!=null)?commentRecommend:0);

        return experience;
    }
}