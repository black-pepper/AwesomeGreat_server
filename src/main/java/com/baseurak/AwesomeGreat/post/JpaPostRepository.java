package com.baseurak.AwesomeGreat.post;

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
public class JpaPostRepository implements PostRepository{

    private final EntityManager em;
    private final JPAQueryFactory query;

    public JpaPostRepository(EntityManager em) {
        this.em = em;
        this.query = new JPAQueryFactory(em);
    }

    @Override
    public void create(Post post) {
        em.persist(post);
    }

    @Override
    public List<Post> read(Long postId, int cnt) {
        String jpql = "SELECT p FROM Post p WHERE p.id <= :postId ORDER BY p.id desc";
        TypedQuery<Post> query = em.createQuery(jpql, Post.class)
                .setParameter("postId", postId)
                .setMaxResults(cnt);
        return query.getResultList();
    }

    @Override
    public Post read(Long postId) {
        Post post = em.find(Post.class, postId);
        return post;
    }

    @Override
    public List<Post> findByUserId(Long userId) {
        String jpql = "SELECT p FROM Post p WHERE p.userId = :userId ORDER BY p.id desc";
        TypedQuery<Post> query = em.createQuery(jpql, Post.class)
                .setParameter("userId", userId);
        return query.getResultList();
    }

    @Override
    public void update(Long postId, String content) {
        Post findPost = em.find(Post.class, postId);
        findPost.setContent(content);
    }

    @Override
    public void delete(Long postId) {
        Post findPost = em.find(Post.class, postId);
        em.remove(findPost);
    }
}