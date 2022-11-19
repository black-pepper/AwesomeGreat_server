package com.baseurak.AwesomeGreat.comment;

import com.baseurak.AwesomeGreat.post.Post;
import com.baseurak.AwesomeGreat.post.QPost;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import static com.baseurak.AwesomeGreat.comment.QComment.comment;
import static com.baseurak.AwesomeGreat.post.QPost.*;

/**
 * 댓글 관련 요청을 데이터베이스에서 처리합니다.
 * @Author: Uju
 */
@Slf4j
@Repository
@Transactional
public class CommentRepository {

    private final EntityManager em;
    private final JPAQueryFactory queryFactory;

    public CommentRepository(EntityManager em) {
        this.em = em;
        this.queryFactory = new JPAQueryFactory(em);
    }

    /**
     * comment를 저장하고 post의 댓글 개수를 추가합니다.
     */
    public void write(Post post, Comment comment) {
        post.setCommentCount(post.getCommentCount()+1);
        em.persist(comment);
    }

    /**
     * postid에 해당하는 게시글의 댓글을 모두 가져옵니다.
     */
    public List<Comment> readCommentList(Long postId) {
        return queryFactory
                .selectFrom(comment)
                .where(comment.postId.eq(postId), comment.block.eq(false))
                .fetch();
    }

    /**
     * commentId에 해당하는 댓글을 가져옵니다.
     */
    public Comment readCommentById(Long commentId) {
        Comment findComment = em.find(Comment.class, commentId);
        return findComment;
    }

    /**
     * commentId에 해당하는 댓글의 글을 content로 수정합니다.
     */
    public void update(Long commentId, String content) {
        Comment findComment = em.find(Comment.class, commentId);
        findComment.setContent(content);
    }

    /**
     * commentId에 해당하는 댓글의 글을 제거하고 post의 댓글 개수를 줄입니다.
     */
    public void delete(Post post, Comment comment) {
        post.setCommentCount(post.getCommentCount()-1);
        em.remove(comment);
    }

    /**
     * userId와 commentId에 해당하는 댓글의 추천 상태를 recommend로 변경합니다.
     */
    public void setRecommend(Long commentId, Long userId, int recommend){
        Comment findComment = em.find(Comment.class, commentId);
        CommentState commentState = readCommentState(commentId, userId);
        if (commentState == null) {
            commentState = new CommentState(commentId, findComment.getUserId(), userId, 0, 0);
            em.persist(commentState);
        }
        commentState.setRecommend(recommend);
        findComment.setRecommend(findComment.getRecommend()+((recommend==1)?1:-1));
    }

    /**
     * userId와 commentId에 해당하는 댓글의 신고 상태를 report로 변경합니다.
     */
    public void setReport(Long commentId, Long userId, int report){
        Comment findComment = em.find(Comment.class, commentId);
        CommentState commentState = readCommentState(commentId, userId);
        if (commentState == null) {
            commentState = new CommentState(commentId, findComment.getUserId(), userId, 0, 0);
            em.persist(commentState);
        }
        commentState.setReport(report);
        findComment.setReport(findComment.getReport()+((report==1)?1:-1));
    }

    /**
     * userId와 commentId에 해당하는 댓글 상태를 추천으로 변경합니다.
     * @deprecated 프론트엔드의 추천/신고를 PUT방식으로 변경후 제거 예정입니다.
     */
    @Deprecated
    public void addRecommend(Long commentId, Long userId) {
        Comment findComment = em.find(Comment.class, commentId);
        CommentState commentState = readCommentState(commentId, userId);

        if (commentState == null) {
            commentState = new CommentState(commentId, findComment.getUserId(), userId, 1, 0);
            findComment.setRecommend(findComment.getRecommend()+1);
            em.persist(commentState);
        } else {
            if (commentState.getRecommend()!=1){
                commentState.setRecommend(1);
                findComment.setRecommend(findComment.getRecommend()+1);
            }
        }
    }
    /**
     * userId와 commentId에 해당하는 댓글 상태의 추천을 취소합니다.
     * @deprecated 프론트엔드의 추천/신고를 PUT방식으로 변경후 제거 예정입니다.
     */
    @Deprecated
    public void deleteRecommend(Long commentId, Long userId){
        Comment findComment = em.find(Comment.class, commentId);
        CommentState findCommentState = readCommentState(commentId, userId);

        if (findCommentState == null) return;
        if (findCommentState.getRecommend() == 1){
            findCommentState.setRecommend(0);
            findComment.setRecommend(findComment.getRecommend()-1);
        }
        if (findCommentState.getRecommend()==0 && findCommentState.getReport()==0){
            em.remove(findCommentState);
        }
    }
    /**
     * userId와 commentId에 해당하는 댓글 상태를 신고로 변경합니다.
     * @deprecated 프론트엔드의 추천/신고를 PUT방식으로 변경후 제거 예정입니다.
     */
    @Deprecated
    public void addReport(Long commentId, Long userId) {
        Comment findComment = em.find(Comment.class, commentId);
        CommentState commentState = readCommentState(commentId, userId);

        if (commentState == null) {
            commentState = new CommentState(commentId, findComment.getUserId(), userId, 0, 1);
            em.persist(commentState);
            findComment.setReport(findComment.getReport()+1);
        } else {
            if (commentState.getReport()!=1){
                commentState.setReport(1);
                findComment.setReport(findComment.getReport()+1);
            }
        }
    }
    /**
     * userId와 commentId에 해당하는 댓글 상태의 신고를 취소합니다.
     * @deprecated 프론트엔드의 추천/신고를 PUT방식으로 변경후 제거 예정입니다.
     */
    @Deprecated
    public void deleteReport(Long commentId, Long userId){
        Comment findComment = em.find(Comment.class, commentId);
        CommentState findCommentState = readCommentState(commentId, userId);

        if (findCommentState == null) return;
        if (findCommentState.getReport()==1){
            findCommentState.setReport(0);
            findComment.setReport(findComment.getReport()-1);
        }
        if (findCommentState.getRecommend()==0 && findCommentState.getReport()==0){
            em.remove(findCommentState);
        }
    }

    /**
     * commentId와 userId에 해당하는 추천/신고 정보를 가져옵니다.
     */
    public CommentState readCommentState(Long commentId, Long userId){
        String jpql = "SELECT c FROM CommentState AS c WHERE c.userId = :userId AND c.commentId = :commentId";
        TypedQuery<CommentState> query = em.createQuery(jpql, CommentState.class);
        query.setParameter("userId", userId);
        query.setParameter("commentId", commentId);
        List<CommentState> resultList = query.getResultList();
        if (resultList.isEmpty()) return null;
        return resultList.get(0);
    }

    /**
     * 게시글(postId)에서 사용자(userId)가 이전에 사용한 닉네임을 찾습니다.
     * @return 이전에 사용하던 닉네임이 존재하면 해당 닉네임을, 아니라면 null을 반환합니다.
     */
    public String findNickname(Long postId, Long userId){
        String jpql = "SELECT c FROM Comment AS c WHERE c.postId = :postId AND c.userId = :userId";
        TypedQuery<Comment> query = em.createQuery(jpql, Comment.class);
        query.setParameter("postId", postId);
        query.setParameter("userId", userId);

        List<Comment> resultList = query.getResultList();
        if (resultList.isEmpty()) return null;
        return resultList.get(0).getNickname();
    }

    /**
     * 게시글(postId)에서 같은 닉네임(nickname)을 사용하는 사람이 있는지 확인합니다.
     * @return 같은 닉네임을 사용하는 인원 수를 리턴합니다.
     */
    public int checkNickname(Long postId, String nickname){
        String jpql = "SELECT c FROM Comment AS c WHERE c.postId = :postId AND c.nickname LIKE CONCAT(:nickname, '%')";
        TypedQuery<Comment> query = em.createQuery(jpql, Comment.class);
        query.setParameter("postId", postId);
        query.setParameter("nickname", nickname);

        List<Comment> resultList = query.getResultList();

        List<Long> users = new ArrayList<>();

        Post findPost = queryFactory
                .selectFrom(post)
                .where(post.id.eq(postId))
                .fetch().get(0);
        if (findPost.getNickname() == nickname){
            users.add(findPost.getUserId());
        }
        for (Comment comment : resultList) {
            if(!users.contains(comment.getUserId())) users.add(comment.getUserId());
        }
        return users.size();
    }

    /**
     * userId가 작성한 댓글을 가져옵니다.
     * @param commentId: commentId보다 작은 id를 가진 댓글을 가져옵니다.
     * @param cnt: 가져오는 댓글의 최대 개수 입니다.
     */
    public List<Comment> readCommentListByUserId(Long userId, Long commentId, int cnt) {
        return queryFactory
                .selectFrom(comment)
                .where(comment.userId.eq(userId), comment.id.loe(commentId), comment.block.eq(false))
                .orderBy(comment.id.desc())
                .limit(cnt)
                .fetch();
    }

    /**
     * 사용자가 당일 작성한 댓글의 수를 계산합니다.
     */
    public Long countCommentToday(Long userId){
        List<Long> result = queryFactory
                .select(comment.count())
                .from(comment)
                .where(comment.userId.eq(userId), comment.uploadDate.after(getDate()))
                .fetch();
        if (result == null) return 0L;
        else return result.get(0);
    }

    /**
     * 쿼리문에 필요한 당일 날짜를 계산합니다.
     * @return 서버가 표준시일 때를 기준으로 한국의 현재 날짜를 표준시로 변환하여 반환합니다.
     */
    private Timestamp getDate() {
        Calendar serverDate = Calendar.getInstance();
        serverDate.setTime(new Date());
        if (serverDate.get(Calendar.HOUR_OF_DAY)<15){
            serverDate.add(Calendar.DAY_OF_MONTH, -1);
        }

        Date queryDate = new Date(
                serverDate.get(Calendar.YEAR),
                serverDate.get(Calendar.MONTH),
                serverDate.get(Calendar.DAY_OF_MONTH)
        );
        queryDate.setHours(15);
        return new Timestamp(queryDate.getTime());
    }
}