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

/**
 * 관리자 관련 요청을 데이터베이스에서 처리합니다.
 * @Author: Uju
 */
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

    /**
     * post테이블에서 postId보다 id가 작은 글을 최신순으로 cnt개 가져옵니다.
     */
    public List<Post> readAllPost(Long postId, int cnt){
        String jpql = "SELECT p FROM Post p WHERE p.id <= :postId ORDER BY p.id desc";
        TypedQuery<Post> query = em.createQuery(jpql, Post.class)
                .setParameter("postId", postId)
                .setMaxResults(cnt);
        return query.getResultList();
    }

    /**
     * comment테이블에서 commentId보다 id가 작은 댓글을 최신순으로 cnt개 가져옵니다.
     */
    public List<Comment> readAllComment(Long commentId, int cnt){
        String jpql = "SELECT c FROM Comment c WHERE c.id <= :commentId ORDER BY c.id desc";
        TypedQuery<Comment> query = em.createQuery(jpql, Comment.class)
                .setParameter("commentId", commentId)
                .setMaxResults(cnt);
        return query.getResultList();
    }

    /**
     * post테이블에서 postId보다 id가 작고 차단된 글만 최신순으로 cnt개 가져옵니다.
     */
    public List<Post> readBlockedPost(Long postId, int cnt){
        String jpql = "SELECT p FROM Post p WHERE p.id <= :postId AND block=1 ORDER BY p.id desc";
        TypedQuery<Post> query = em.createQuery(jpql, Post.class)
                .setParameter("postId", postId)
                .setMaxResults(cnt);
        return query.getResultList();
    }

    /**
     * comment테이블에서 commentId보다 id가 작고 차단된 댓글만 최신순으로 cnt개 가져옵니다.
     */
    public List<Comment> readBlockedComment(Long commentId, int cnt){
        String jpql = "SELECT c FROM Comment c WHERE c.id <= :commentId AND block=1 ORDER BY c.id desc";
        TypedQuery<Comment> query = em.createQuery(jpql, Comment.class)
                .setParameter("commentId", commentId)
                .setMaxResults(cnt);
        return query.getResultList();
    }

    /**
     * postId에 해당하는 글의 block을 설정합니다.
     */
    public void setBlockPost(Long postId, Boolean block) {
        Post findPost = em.find(Post.class, postId);
        User findUser = em.find(User.class, findPost.getUserId());
        if (findPost.isBlock() != block){
            findPost.setBlock(block);
            findUser.setDemerit(findUser.getDemerit()+((block)?1:-1));
        }
    }

    /**
     * commentId에 해당하는 댓글의 block을 설정합니다.
     */
    public void setBlockComment(Long commentId, Boolean block) {
        Comment findComment = em.find(Comment.class, commentId);
        User findUser = em.find(User.class, findComment.getUserId());
        if (findComment.isBlock() != block){
            findComment.setBlock(block);
            findUser.setDemerit(findUser.getDemerit()+((block)?1:-1));
        }
    }
}