package com.baseurak.AwesomeGreat.post;

import com.baseurak.AwesomeGreat.exception.NotFoundException;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

import static com.baseurak.AwesomeGreat.post.QPost.post;

@Slf4j
@Repository
@Transactional
public class PostRepository {

    private final EntityManager em;
    private final JPAQueryFactory query;

    public PostRepository(EntityManager em) {
        this.em = em;
        this.query = new JPAQueryFactory(em);
    }

    public void create(Post post) {
        em.persist(post);
    }

    public List<Post> read(Long postId, int cnt) {
//        String jpql = "SELECT p FROM Post p WHERE p.id <= :postId ORDER BY p.id desc";
//        TypedQuery<Post> query = em.createQuery(jpql, Post.class)
//                .setParameter("postId", postId)
//                .setMaxResults(cnt);
//        return query.getResultList();
        return query
                .selectFrom(post)
                .where(post.id.loe(postId), post.block.eq(false))
                .orderBy(post.id.desc())
                .limit(cnt)
                .fetch();
    }

    public Post read(Long postId) {
        Post post = em.find(Post.class, postId);
        if (post==null || post.isBlock()) throw new NotFoundException();
        return post;
    }

    public List<Post> findByUserId(Long userId, Long postId, int cnt) {
//        String jpql = "SELECT p FROM Post p WHERE p.userId = :userId AND p.id <= :postId ORDER BY p.id desc";
//        TypedQuery<Post> query = em.createQuery(jpql, Post.class)
//                .setParameter("userId", userId)
//                .setParameter("postId", postId)
//                .setMaxResults(cnt);
//        return query.getResultList();
        return query
                .selectFrom(post)
                .where(post.id.loe(postId), post.block.eq(false), post.userId.eq(userId))
                .orderBy(post.id.desc())
                .limit(cnt)
                .fetch();
    }

    public void update(Long postId, String content) {
        Post findPost = read(postId);
        findPost.setContent(content);
    }

    public void delete(Long postId) {
        Post findPost = read(postId);
        em.remove(findPost);
    }

    public void addRecommend(Long postId, Long userId) {
        Post findPost = read(postId);
        PostState postState = findPostState(postId, userId);

        if (postState==null) {
            postState = new PostState(postId, findPost.getUserId(), userId, 1, 0);
            findPost.setRecommend(findPost.getRecommend()+1);
            em.persist(postState);
        } else {
            if (postState.getRecommend() != 1){
                postState.setRecommend(1);
                findPost.setRecommend(findPost.getRecommend()+1);
            }
        }
    }

    public void deleteRecommend(Long postId, Long userId) {
        Post findPost = read(postId);
        PostState findPostState = findPostState(postId, userId);

        if (findPostState == null) return;
        if (findPostState.getRecommend() == 1){
            findPostState.setRecommend(0);
            findPost.setRecommend(findPost.getRecommend()-1);
        }
        if (findPostState.getRecommend() == 0 && findPostState.getReport() == 0){
            em.remove(findPostState);
        }
    }

    public void addReport(Long postId, Long userId) {
        Post findPost = read(postId);
        PostState postState = findPostState(postId, userId);

        if (postState==null){
            postState = new PostState(postId, findPost.getUserId(), userId, 0, 1);
            em.persist(postState);
            findPost.setReport(findPost.getReport()+1);
        } else {
            if (postState.getReport() != 1){
                postState.setReport(1);
                findPost.setReport(findPost.getReport()+1);
            }
        }
    }

    public void deleteReport(Long postId, Long userId) {
        Post findPost = read(postId);
        PostState findPostState = findPostState(postId, userId);

        if (findPostState == null) return;
        if (findPostState.getReport() == 1){
            findPostState.setReport(0);
            findPost.setReport(findPost.getReport()-1);
        }
        if (findPostState.getRecommend() == 0 && findPostState.getReport() == 0){
            em.remove(findPostState);
        }
    }

    public PostState findPostState(Long postId, Long userId){
        String jpql = "SELECT p FROM PostState AS p WHERE p.userId = :userId AND p.postId = :postId";
        TypedQuery<PostState> query = em.createQuery(jpql, PostState.class);
        query.setParameter("userId", userId);
        query.setParameter("postId", postId);
        List<PostState> resultList = query.getResultList();
        if (resultList.isEmpty()) return null;
        return resultList.get(0);
    }

    public Long countPostToday(Long userId){
//        String jpql = "SELECT COUNT(p.id) FROM Post p WHERE p.userId = :userId AND p.uploadDate >= current_date";
//        TypedQuery<Long> result = em.createQuery(jpql, Long.class)
//                .setParameter("userId", userId);
//        Long count = result.getSingleResult();
        List<Long> result = query
                .select(post.count())
                .from(post)
                .where(post.userId.eq(userId), post.uploadDate.goe(CalDate()))
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

    public List<Post> search(Long postId, int cnt, String keyword) {
        return query
                .selectFrom(post)
                .where(post.content.contains(keyword), post.id.loe(postId), post.block.eq(false))
                .orderBy(post.id.desc())
                .limit(cnt)
                .fetch();
    }
}