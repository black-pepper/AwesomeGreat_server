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
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
/**
 * 댓글 관련 요청을 처리하기 위해 해당 로직을 실행합니다.
 * @Author: Uju
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;
    private final PostService postService;
    private final NicknameService nicknameService;
    private final NumericalSetting numericalSetting;

    /**
     * postId에 해당하는 게시글의 댓글에 내용이 content인 새로운 댓글을 저장합니다.
     * @param userInfo: 사용자가 댓글을 작성할 수 있는지 확인하고 댓글에 사용자의 정보를 저장합니다.
     */
    public Comment write(Long postId, String content, UserInfo userInfo) {

        if (userInfo.getCommentToday()>=numericalSetting.getCommentLimit()){
            throw new BadRequestException();
        }

        Comment comment = new Comment(postId, content);
        LocalDateTime now = LocalDateTime.now();
        comment.setUploadDate(Timestamp.valueOf(now));
        comment.setUserId(userInfo.getUserId());

        Post findPost =  postService.readPost(comment.getPostId());
        comment.setNickname(getNickname(userInfo, findPost));
        comment.setReport(0);
        commentRepository.write(findPost, comment);
        return comment;
    }

    /**
     * 이전에 해당 글에서 닉네임이 생성되었는지 확인하고 있다면 해당 댓글의 닉네임을, 아니라면 새로운 닉네임을 생성하여 반환합니다.
     * @param userInfo: 사용자 정보를 확인합니다.
     * @param findPost: 게시글 정보를 확인합니다.
     */
    private String getNickname(UserInfo userInfo, Post findPost) {
        String nickname;
        if (userInfo.getUserId() == findPost.getUserId()) { //글쓴이 확인
            nickname = findPost.getNickname();
        } else {
            nickname = commentRepository.findNickname(findPost.getId(), userInfo.getUserId());
            if (nickname == null) {
                nickname = nicknameService.getNickname();
                int count = commentRepository.checkNickname(findPost.getId(), nickname);
                if (count!=0) nickname = nickname+String.valueOf(count+1); //중복 닉네임 처리
            }
        }
        return nickname;
    }

    /**
     * postid에 해당하는 게시글의 댓글을 모두 가져옵니다.
     * @return user의 추천/신고 정보가 추가된 CommentDto 리스트를 반환합니다.
     */
    public List<CommentDto> readCommentDtoList(Long postId, User user) {
        List<Comment> comments = commentRepository.readCommentList(postId);
        List<CommentDto> commentDtos = new ArrayList<>();

        //ModelMapper modelMapper = new ModelMapper();
        for (int i = 0; i < comments.size(); i++) {
            //commentDtos.add(modelMapper.map(comments.get(i), CommentDto.class));
            commentDtos.add(new CommentDto(comments.get(i)));
        }

        if (user == null) return commentDtos;
        for (int i = 0; i < comments.size(); i++) {
            CommentState commentState = commentRepository.readCommentState(comments.get(i).getId(), user.getId());
            if (commentState == null) continue;
            commentDtos.get(i).setMyRecommend(commentState.getRecommend());
            commentDtos.get(i).setMyReport(commentState.getReport());
        }
        return commentDtos;
    }

    /**
     * commentId에 해당하는 댓글의 내용을 content로 수정합니다.
     * @param user: 요청한 사용자와 해당 댓글을 작성한 사용자가 같은지 확인합니다.
     */
    public void modify(Long commentId, String content, User user) {
        if (checkUser(commentId, user)){
            commentRepository.update(commentId, content);
        }
    }

    /**
     * commentId에 해당하는 댓글을 삭제합니다.
     * @param user: 요청한 사용자와 해당 댓글을 작성한 사용자가 같은지 확인합니다.
     */
    public void delete(Long commentId, User user) {
        if (checkUser(commentId, user)){
            Comment findComment = commentRepository.readCommentById(commentId);
            Post findPost = postService.readPost(findComment.getPostId());
            commentRepository.delete(findPost, findComment);
        }
    }

    /**
     * user와 commentId에 해당하는 댓글의 추천 상태를 recommend로 변경합니다.
     */
    public void setRecommend(Long commentId, User user, int recommend){
        commentRepository.setRecommend(commentId, user.getId(), recommend);
    }

    /**
     * user와 commentId에 해당하는 댓글의 신고 상태를 report로 변경합니다.
     */
    public void setReport(Long commentId, User user, int report){
        commentRepository.setReport(commentId, user.getId(), report);
    }

    /**
     * user가 commentId에 해당하는 댓글을 추천합니다.
     * @deprecated 프론트엔드의 추천/신고를 PUT방식으로 변경후 제거 예정입니다.
     */
    @Deprecated
    public void addRecommend(Long commentId, User user) {
        commentRepository.addRecommend(commentId, user.getId());
    }
    /**
     * user가 commentId에 해당하는 댓글의 추천을 취소합니다.
     * @deprecated 프론트엔드의 추천/신고를 PUT방식으로 변경후 제거 예정입니다.
     */
    @Deprecated
    public void deleteRecommend(Long commentId, User user) {
        commentRepository.deleteRecommend(commentId, user.getId());
    }
    /**
     * user가 commentId에 해당하는 댓글을 신고합니다.
     * @deprecated 프론트엔드의 추천/신고를 PUT방식으로 변경후 제거 예정입니다.
     */
    @Deprecated
    public void addReport(Long commentId, User user) {
        commentRepository.addReport(commentId, user.getId());
    }
    /**
     * user가 commentId에 해당하는 댓글의 신고를 취소합니다.
     * @deprecated 프론트엔드의 추천/신고를 PUT방식으로 변경후 제거 예정입니다.
     */
    @Deprecated
    public void deleteReport(Long commentId, User user) {
        commentRepository.deleteReport(commentId, user.getId());
    }

    /**
     * userId가 작성한 댓글을 가져옵니다.
     * @param commentId: commentId보다 작은 id를 가진 댓글을 가져옵니다.
     * @param cnt: 가져오는 댓글의 최대 개수 입니다.
     */
    public List<Comment> readCommentListByUserId(Long userId, Long commentId, int cnt) {
        return commentRepository.readCommentListByUserId(userId, commentId, cnt);
    }

    /**
     * commentId에 해당하는 댓글의 작성자와 user가 같은지 확인합니다.
     */
    private Boolean checkUser(Long commentId, User user) {
        Long findCommentUserId = commentRepository.readCommentById(commentId).getUserId();
        Long findUserId = user.getId();
        return findCommentUserId.equals(findUserId);
    }
}