package com.baseurak.AwesomeGreat.post;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

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
