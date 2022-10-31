package com.baseurak.AwesomeGreat.post;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.sql.Timestamp;

@Data
@Entity
@NoArgsConstructor
public class Post {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long userId;
    private Timestamp uploadDate;
    private String nickname;
    private String content;
    private int recommend;
    private int report;
    private int commentCount;
    private boolean block;

    public Post(String content){
        this.content = content;
        this.recommend = 0;
        this.report = 0;
        this.commentCount = 0;
        this.block = false;
    }
}
