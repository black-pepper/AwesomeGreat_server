package com.baseurak.AwesomeGreat.comment;

import com.baseurak.AwesomeGreat.admin.NumericalSetting;
import com.baseurak.AwesomeGreat.exception.BadRequestException;
import com.baseurak.AwesomeGreat.nickname.NicknameService;
import com.baseurak.AwesomeGreat.post.Post;
import com.baseurak.AwesomeGreat.post.PostService;
import com.baseurak.AwesomeGreat.user.User;
import com.baseurak.AwesomeGreat.user.UserInfo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;
    private final PostService postService;
    private final NicknameService nicknameService;
    private final NumericalSetting numericalSetting;

    public Comment write(Long postId, String content, UserInfo userInfo) {

        if (userInfo.getCommentToday()>=numericalSetting.getCommentLimit()){
            throw new BadRequestException();
        }

        Comment comment = new Comment(postId, content);
        LocalDateTime now = LocalDateTime.now();
        comment.setUploadDate(Timestamp.valueOf(now));
        comment.setUserId(userInfo.getUserId());

        Post findPost =  postService.read(comment.getPostId());
        String nickname;
        if (userInfo.getUserId() == findPost.getUserId()) { //글쓴이 확인
            nickname = findPost.getNickname();
        } else {
            nickname = commentRepository.findNickname(comment.getPostId(), comment.getUserId());
            if (nickname == null) {
                nickname = nicknameService.makeNickname();
                int count = commentRepository.checkNickname(comment.getPostId(), nickname);
                if (count!=0) nickname = nickname+String.valueOf(count+1); //중복 닉네임 처리
            }
        }

        comment.setNickname(nickname);
        comment.setReport(0);
        commentRepository.create(findPost, comment);
        return comment;
    }

    public List<CommentDto> read(Long postId, User user) {
        List<Comment> comments = commentRepository.read(postId);
        List<CommentDto> commentDtos = new ArrayList<>();

        ModelMapper modelMapper = new ModelMapper();
        for (int i = 0; i < comments.size(); i++) {
            commentDtos.add(modelMapper.map(comments.get(i), CommentDto.class));
        }

        if (user == null) return commentDtos;
        for (int i = 0; i < comments.size(); i++) {
            CommentState commentState = commentRepository.findCommentState(comments.get(i).getId(), user.getId());
            if (commentState == null) continue;
            commentDtos.get(i).setMyRecommend(commentState.getRecommend());
            commentDtos.get(i).setMyReport(commentState.getReport());
        }
        return commentDtos;
    }

    public void modify(Long commentId, String content, User user) {
        if (checkUser(commentId, user)){
            commentRepository.update(commentId, content);
        }
    }

    public void delete(Long commentId, User user) {
        if (checkUser(commentId, user)){
            Comment findComment = commentRepository.readById(commentId);
            Post findPost = postService.read(findComment.getPostId());
            commentRepository.delete(findPost, findComment);
        }
    }

    public void addRecommend(Long commentId, User user) {
        commentRepository.addRecommend(commentId, user.getId());
    }

    public void deleteRecommend(Long commentId, User user) {
        commentRepository.deleteRecommend(commentId, user.getId());
    }

    public void addReport(Long commentId, User user) {
        commentRepository.addReport(commentId, user.getId());
    }

    public void deleteReport(Long commentId, User user) {
        commentRepository.deleteReport(commentId, user.getId());
    }

    public List<Comment> findByUserId(Long userId, Long commentId, int cnt) {
        return commentRepository.findByUserId(userId, commentId, cnt);
    }

    private Boolean checkUser(Long commentId, User user) {
        Long findCommentUserId = commentRepository.readById(commentId).getUserId();
        Long findUserId = user.getId();
        return findCommentUserId.equals(findUserId);
    }
}