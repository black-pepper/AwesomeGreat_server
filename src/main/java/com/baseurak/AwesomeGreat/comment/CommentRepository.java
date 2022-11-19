package com.baseurak.AwesomeGreat.comment;
import java.util.List;

public interface CommentRepository {
    void create(Comment comment);
    List<Comment> read(Long postId);
    Comment readById(Long commentId);
    void update(Long commentId, String contents);
    void delete(Long commentId);
    String findNickname(Long commentId, Long userId);

    List<Comment> findByUserId(Long userId);
}