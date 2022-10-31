package com.baseurak.AwesomeGreat.admin;

import com.baseurak.AwesomeGreat.comment.Comment;
import com.baseurak.AwesomeGreat.post.Post;
import com.baseurak.AwesomeGreat.user.User;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.transaction.Transactional;
import java.util.List;

@Slf4j
@Repository
@Transactional
public class AdminRepository {
    private final EntityManager em;
    private final JPAQueryFactory query;

    public AdminRepository(EntityManager em) {
        this.em = em;
        this.query = new JPAQueryFactory(em);
    }

    public List<Post> readAllPost(Long postId, int cnt){
        String jpql = "SELECT p FROM Post p WHERE p.id <= :postId ORDER BY p.id desc";
        TypedQuery<Post> query = em.createQuery(jpql, Post.class)
                .setParameter("postId", postId)
                .setMaxResults(cnt);
        return query.getResultList();
    }

    public List<Comment> readAllComment(Long commentId, int cnt){
        String jpql = "SELECT c FROM Comment c WHERE c.id <= :commentId ORDER BY c.id desc";
        TypedQuery<Comment> query = em.createQuery(jpql, Comment.class)
                .setParameter("commentId", commentId)
                .setMaxResults(cnt);
        return query.getResultList();
    }

    public List<Post> readBlockedPost(Long postId, int cnt){
        String jpql = "SELECT p FROM Post p WHERE p.id <= :postId AND block=1 ORDER BY p.id desc";
        TypedQuery<Post> query = em.createQuery(jpql, Post.class)
                .setParameter("postId", postId)
                .setMaxResults(cnt);
        return query.getResultList();
    }

    public List<Comment> readBlockedComment(Long commentId, int cnt){
        String jpql = "SELECT c FROM Comment c WHERE c.id <= :commentId AND block=1 ORDER BY c.id desc";
        TypedQuery<Comment> query = em.createQuery(jpql, Comment.class)
                .setParameter("commentId", commentId)
                .setMaxResults(cnt);
        return query.getResultList();
    }

    public void blockPost(Long postId) {
        Post findPost = em.find(Post.class, postId);
        User findUser = em.find(User.class, findPost.getUserId());
        findPost.setBlock(true);
        findUser.setDemerit(findUser.getDemerit()+1);
    }

    public void blockComment(Long commentId) {
        Comment findComment = em.find(Comment.class, commentId);
        User findUser = em.find(User.class, findComment.getUserId());
        findComment.setBlock(true);
        findUser.setDemerit(findUser.getDemerit()+1);
    }

    public void unBlockPost(Long postId){
        Post findPost = em.find(Post.class, postId);
        User findUser = em.find(User.class, findPost.getUserId());
        findPost.setBlock(false);
        findUser.setDemerit(findUser.getDemerit()-1);
    }

    public void unBlockComment(Long commentId){
        Comment findComment = em.find(Comment.class, commentId);
        User findUser = em.find(User.class, findComment.getUserId());
        findComment.setBlock(false);
        findUser.setDemerit(findUser.getDemerit()-1);
    }

}
