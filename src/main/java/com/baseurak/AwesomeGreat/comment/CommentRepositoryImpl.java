package com.baseurak.AwesomeGreat.comment;

import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import java.util.List;

@Slf4j
@Repository
@Transactional
public class CommentRepositoryImpl implements CommentRepository {

    private final EntityManager em;
    private final JPAQueryFactory query;

    public CommentRepositoryImpl (EntityManager em) {
        this.em = em;
        this.query = new JPAQueryFactory(em);
    }

    @Override
    public void create(Comment comment) {
        em.persist(comment);
    }

    @Override
    public List<Comment> read(Long postId) {
        String jpql = "SELECT c FROM Comment AS c WHERE c.postId = :postId";
        TypedQuery<Comment> query = em.createQuery(jpql, Comment.class);
        query.setParameter("postId", postId);
        return query.getResultList();
    }

    @Override
    public Comment readById(Long commentId) {
        Comment findComment = em.find(Comment.class, commentId);
        return findComment;
    }

    @Override
    public void update(Long commentId, String contents) {
        Comment findComment = em.find(Comment.class, commentId);
        findComment.setContent(contents);
    }

    @Override
    public void delete(Long commentId) {
        Comment findComment = em.find(Comment.class, commentId);
        em.remove(findComment);
    }

    @Override
    public String findNickname(Long postId, Long userId){
        String jpql = "SELECT c FROM Comment AS c WHERE c.postId = :postId AND c.userId = :userId";
        TypedQuery<Comment> query = em.createQuery(jpql, Comment.class);
        query.setParameter("postId", postId);
        query.setParameter("userId", userId);

        List<Comment> resultList = query.getResultList();
        if (resultList.isEmpty()) return null;
        return resultList.get(0).getNickname();
    }

    @Override
    public List<Comment> findByUserId(Long userId) {
        String jpql = "SELECT c FROM Comment AS c WHERE c.userId = :userId";
        TypedQuery<Comment> query = em.createQuery(jpql, Comment.class);
        query.setParameter("userId", userId);
        return query.getResultList();
    }

}
