package com.baseurak.AwesomeGreat.experience;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Experience {
    Long postCount;
    Long postRecommendSum;
    Long commentCount;
    Long commentRecommendSum;
}
