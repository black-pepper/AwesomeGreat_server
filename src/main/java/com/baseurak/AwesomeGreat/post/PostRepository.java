package com.baseurak.AwesomeGreat.post;

import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PostRepository {
    void create(Post post);
    List<Post> read(Long postId, int cnt);
    Post read(Long postId);
    void update(Long postId, String content);
    void delete(Long postId);
    List<Post> findByUserId(Long userId);
}
