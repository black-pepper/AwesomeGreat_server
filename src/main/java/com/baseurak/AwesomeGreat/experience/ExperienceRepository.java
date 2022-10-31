package com.baseurak.AwesomeGreat.experience;

import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

@Repository
public class ExperienceRepository {
    private final EntityManager em;
    private final JPAQueryFactory query;

    public ExperienceRepository(EntityManager em) {
        this.em = em;
        this.query = new JPAQueryFactory(em);
    }

    Long postCount(Long userId){
        String jpql = "SELECT COUNT(p.id) FROM Post p WHERE p.userId = :userId";
        TypedQuery<Long> query = em.createQuery(jpql, Long.class)
                .setParameter("userId", userId);
        return query.getSingleResult();
    }

    Long postRecommendSum(Long userId){
        String jpql = "SELECT SUM(p.recommend) FROM PostState p WHERE p.postUserId = :userId";
        TypedQuery<Long> query = em.createQuery(jpql, Long.class)
                .setParameter("userId", userId);
        return query.getSingleResult();
    }

    Long commentCount(Long userId) {
        String jpql = "SELECT COUNT(c.id) FROM Comment c WHERE c.userId = :userId";
        TypedQuery<Long> query = em.createQuery(jpql, Long.class)
                .setParameter("userId", userId);
        return query.getSingleResult();
    }

    Long commentRecommendSum(Long userId) {
        String jpql = "SELECT SUM(c.recommend) FROM CommentState c WHERE c.commentUserId = :userId";
        TypedQuery<Long> query = em.createQuery(jpql, Long.class)
                .setParameter("userId", userId);
        return query.getSingleResult();
    }

}
