package com.baseurak.AwesomeGreat.comment;

import com.baseurak.AwesomeGreat.nickname.NicknameService;
import com.baseurak.AwesomeGreat.user.User;
import com.baseurak.AwesomeGreat.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService {

    private final CommentRepository commentRepository;
    private final UserService userService;
    private final NicknameService nicknameService;

    @Override
    public void write(Comment comment) {
        LocalDateTime now = LocalDateTime.now();
        comment.setUploadDate(Timestamp.valueOf(now));
        User findUser = userService.findUserBySessionId();
        comment.setUserId(findUser.getId());

        String nickname = commentRepository.findNickname(comment.getPostId(), comment.getUserId());
        if (nickname == null) nickname = nicknameService.makeNickname();
        comment.setNickname(nickname);

        comment.setReport(0);
        commentRepository.create(comment);
    }

    @Override
    public List<Comment> read(Long commentId) {
        return commentRepository.findByUserId(commentId);
    }

    @Override
    public void modify(Long commentId, String content) {
        if (checkUser(commentId)){
            commentRepository.update(commentId, content);
        }
    }

    @Override
    public void delete(Long commentId) {
        if (checkUser(commentId)){
            commentRepository.delete(commentId);
        }
    }

    @Override
    public List<Comment> findByUserId(Long userId) {
        return commentRepository.findByUserId(userId);
    }

    public Boolean checkUser(Long commentId) {
        Long findCommentUserId = commentRepository.readById(commentId).getUserId();
        Long findUserId = userService.findUserBySessionId().getId();
        return findCommentUserId.equals(findUserId);
    }
}