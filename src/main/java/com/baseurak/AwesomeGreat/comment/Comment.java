package com.baseurak.AwesomeGreat.comment;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
/**
 * 댓글 정보입니다.
 * @Author: Uju
 */
@Data
@Entity
@Table(name="comments")
@NoArgsConstructor
public class Comment {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long postId;
    private Long userId;
    private Timestamp uploadDate;
    private String nickname;
    private String content;
    private int recommend;
    private int report;
    private boolean block;

    public Comment(Long postId, String content){
        this.postId = postId;
        this.content = content;
    }

//    @OneToMany(mappedBy = "comment" , fetch = FetchType.LAZY)
//    private List<CommentState> commentStates = new ArrayList<>();
}
