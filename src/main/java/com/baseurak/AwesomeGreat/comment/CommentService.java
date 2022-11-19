package com.baseurak.AwesomeGreat.comment;

import java.util.List;

public interface CommentService{
    void write(Comment comment);
    List<Comment> read(Long postId);
    void modify(Long commentId, String content);
    void delete(Long commentId);
    List<Comment> findByUserId(Long userId);
}
