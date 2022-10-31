package com.baseurak.AwesomeGreat.comment;

import com.baseurak.AwesomeGreat.post.Post;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import static com.baseurak.AwesomeGreat.comment.QComment.comment;

@Slf4j
@Repository
@Transactional
public class CommentRepository {

    private final EntityManager em;
    private final JPAQueryFactory query;

    public CommentRepository(EntityManager em) {
        this.em = em;
        this.query = new JPAQueryFactory(em);
    }

    public void create(Post post, Comment comment) {
        post.setCommentCount(post.getCommentCount()+1);
        em.persist(comment);
    }

    public List<Comment> read(Long postId) {
//        String jpql = "SELECT c FROM Comment c WHERE c.postId = :postId";
//        TypedQuery<Comment> query = em.createQuery(jpql, Comment.class);
//        query.setParameter("postId", postId);
//        //log.info("result = {}", query.getResultList());
//        return query.getResultList();
        return query
                .selectFrom(comment)
                .where(comment.postId.eq(postId), comment.block.eq(false))
                .fetch();
    }

    public Comment readById(Long commentId) {
        Comment findComment = em.find(Comment.class, commentId);
        return findComment;
    }

    public void update(Long commentId, String contents) {
        Comment findComment = em.find(Comment.class, commentId);
        findComment.setContent(contents);
    }

    public void delete(Post post, Comment comment) {
        post.setCommentCount(post.getCommentCount()-1);
        em.remove(comment);
    }

    public void addRecommend(Long commentId, Long userId) {
        Comment findComment = em.find(Comment.class, commentId);
        CommentState commentState = findCommentState(commentId, userId);

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

    public void deleteRecommend(Long commentId, Long userId){
        Comment findComment = em.find(Comment.class, commentId);
        CommentState findCommentState = findCommentState(commentId, userId);

        if (findCommentState == null) return;
        if (findCommentState.getRecommend() == 1){
            findCommentState.setRecommend(0);
            findComment.setRecommend(findComment.getRecommend()-1);
        }
        if (findCommentState.getRecommend()==0 && findCommentState.getReport()==0){
            em.remove(findCommentState);
        }
    }

    public void addReport(Long commentId, Long userId) {
        Comment findComment = em.find(Comment.class, commentId);
        CommentState commentState = findCommentState(commentId, userId);

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

    public void deleteReport(Long commentId, Long userId){
        Comment findComment = em.find(Comment.class, commentId);
        CommentState findCommentState = findCommentState(commentId, userId);

        if (findCommentState == null) return;
        if (findCommentState.getReport()==1){
            findCommentState.setReport(0);
            findComment.setReport(findComment.getReport()-1);
        }
        if (findCommentState.getRecommend()==0 && findCommentState.getReport()==0){
            em.remove(findCommentState);
        }
    }

    public CommentState findCommentState(Long commentId, Long userId){
        String jpql = "SELECT c FROM CommentState AS c WHERE c.userId = :userId AND c.commentId = :commentId";
        TypedQuery<CommentState> query = em.createQuery(jpql, CommentState.class);
        query.setParameter("userId", userId);
        query.setParameter("commentId", commentId);
        List<CommentState> resultList = query.getResultList();
        if (resultList.isEmpty()) return null;
        return resultList.get(0);
    }

    public String findNickname(Long postId, Long userId){
        String jpql = "SELECT c FROM Comment AS c WHERE c.postId = :postId AND c.userId = :userId";
        TypedQuery<Comment> query = em.createQuery(jpql, Comment.class);
        query.setParameter("postId", postId);
        query.setParameter("userId", userId);

        List<Comment> resultList = query.getResultList();
        if (resultList.isEmpty()) return null;
        return resultList.get(0).getNickname();
    }

    public int checkNickname(Long postId, String nickname){
        String jpql = "SELECT c FROM Comment AS c WHERE c.postId = :postId AND c.nickname LIKE CONCAT(:nickname, '%')";
        TypedQuery<Comment> query = em.createQuery(jpql, Comment.class);
        query.setParameter("postId", postId);
        query.setParameter("nickname", nickname);

        List<Comment> resultList = query.getResultList();

        if (resultList.isEmpty()) return 0;

        List<Long> users = new ArrayList<>();
        for (Comment comment : resultList) {
            if(!users.contains(comment.getUserId())) users.add(comment.getUserId());
        }
        return users.size();
    }

    public List<Comment> findByUserId(Long userId, Long commentId, int cnt) {
//        String jpql = "SELECT c FROM Comment AS c WHERE c.userId = :userId AND c.id <= :commentId ORDER BY c.id desc";
//        TypedQuery<Comment> query = em.createQuery(jpql, Comment.class)
//                .setParameter("userId", userId)
//                .setParameter("commentId", commentId)
//                .setMaxResults(cnt);
//        return query.getResultList();
        return query
                .selectFrom(comment)
                .where(comment.userId.eq(userId), comment.id.loe(commentId), comment.block.eq(false))
                .orderBy(comment.id.desc())
                .limit(cnt)
                .fetch();
    }

    public Long countCommentToday(Long userId){
        List<Long> result = query
                .select(comment.count())
                .from(comment)
                .where(comment.userId.eq(userId), comment.uploadDate.after(CalDate()))
                .fetch();
        if (result == null) return 0L;
        else return result.get(0);
    }

    private Timestamp CalDate() {
        Calendar serverDate = Calendar.getInstance();
        serverDate.setTime(new Date());
        serverDate.add(Calendar.HOUR, 9);
        Date koreanDate = serverDate.getTime();
        koreanDate.setHours(0);
        koreanDate.setMinutes(0);
        koreanDate.setSeconds(0);
        Calendar result = Calendar.getInstance();
        result.setTime(koreanDate);
        result.add(Calendar.HOUR, -9);
        return new Timestamp(result.getTimeInMillis());
    }
}