package com.baseurak.AwesomeGreat.post;

import org.springframework.stereotype.Service;

import java.util.List;

public interface PostService {
    void write(Post post);
    List<Post> read(Long postId, int cnt);
    Post read(Long postId);
    void modify(Long postId, String contents);
    void delete(Long postId);

    List<Post> findByUserId(Long userId);
}