package com.baseurak.AwesomeGreat.experience;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;

@Service
@RequiredArgsConstructor
public class ExperienceService {

    final ExperienceRepository experienceRepository;

    Long postCount(Long userId){
        return experienceRepository.postCount(userId);
    }

    Long postRecommendSum(Long userId){
        return experienceRepository.postRecommendSum(userId);
    }

    Long commentCount(Long userId) {
        return experienceRepository.commentCount(userId);
    }

    Long commentRecommendSum(Long userId) {
        return experienceRepository.commentRecommendSum(userId);
    }
}
