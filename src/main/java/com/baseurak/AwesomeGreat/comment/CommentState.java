package com.baseurak.AwesomeGreat.comment;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@Entity
@NoArgsConstructor
public class CommentState {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
//    @ManyToOne(fetch = FetchType.LAZY)
    private Long commentId;
    private Long commentUserId;
    private Long userId;
    private int recommend;
    private int report;
//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "comment_id")
//    private Comment comment;

    public CommentState(Long commentId, Long commentUserId, Long userId, int recommend, int report){
        this.commentId = commentId;
        this.commentUserId = commentUserId;
        this.userId = userId;
        this.recommend = recommend;
        this.report = report;
    }
}
