package com.baseurak.AwesomeGreat.post;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Data
@Entity
@NoArgsConstructor
public class PostState {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long postId;
    private Long postUserId;
    private Long userId;
    private int recommend;
    private int report;

    public PostState(Long postId, Long postUserId, Long userId, int recommend, int report){
        this.postId = postId;
        this.postUserId = postUserId;
        this.userId = userId;
        this.recommend = recommend;
        this.report = report;
    }
}