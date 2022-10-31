package com.baseurak.AwesomeGreat.experience;

import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
/**
 * 사용자 관련 통계 요청을 데이터베이스에서 처리합니다.
 * @Author: Uju
 */
@Repository
public class ExperienceRepository {
    private final EntityManager em;
    private final JPAQueryFactory query;

    public ExperienceRepository(EntityManager em) {
        this.em = em;
        this.query = new JPAQueryFactory(em);
    }

    /**
     * userId에 해당하는 게시글의 수를 계산합니다.
     */
    Long countPost(Long userId){
        String jpql = "SELECT COUNT(p.id) FROM Post p WHERE p.userId = :userId";
        TypedQuery<Long> query = em.createQuery(jpql, Long.class)
                .setParameter("userId", userId);
        return query.getSingleResult();
    }

    /**
     * userId에 해당하는 게시글에 받은 추천 수를 계산합니다.
     */
    Long sumPostRecommend(Long userId){
        String jpql = "SELECT SUM(p.recommend) FROM PostState p WHERE p.postUserId = :userId";
        TypedQuery<Long> query = em.createQuery(jpql, Long.class)
                .setParameter("userId", userId);
        return query.getSingleResult();
    }

    /**
     * userId에 해당하는 댓글의 수를 계산합니다.
     */
    Long countComment(Long userId) {
        String jpql = "SELECT COUNT(c.id) FROM Comment c WHERE c.userId = :userId";
        TypedQuery<Long> query = em.createQuery(jpql, Long.class)
                .setParameter("userId", userId);
        return query.getSingleResult();
    }

    /**
     * userId에 해당하는 댓글의 추천 수를 계산합니다.
     */
    Long sumCommentRecommend(Long userId) {
        String jpql = "SELECT SUM(c.recommend) FROM CommentState c WHERE c.commentUserId = :userId";
        TypedQuery<Long> query = em.createQuery(jpql, Long.class)
                .setParameter("userId", userId);
        return query.getSingleResult();
    }

}
