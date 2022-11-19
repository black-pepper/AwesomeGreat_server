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
/**
 * 게시글 관련 요청을 데이터베이스에서 처리합니다.
 * @Author: Uju
 */
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

    /**
     * post를 저장합니다.
     */
    public void write(Post post) {
        em.persist(post);
    }

    /**
     * 차단되지 않은 postId이하의 게시글을 최신순으로 cnt개 가져옵니다.
     */
    public List<Post> readPostList(Long postId, int cnt) {
        return query
                .selectFrom(post)
                .where(post.id.loe(postId), post.block.eq(false))
                .orderBy(post.id.desc())
                .limit(cnt)
                .fetch();
    }

    /**
     * postId에 해당하는 게시글을 가져옵니다.
     */
    public Post readPost(Long postId) {
        Post post = em.find(Post.class, postId);
        if (post==null) throw new NotFoundException();
        return post;
    }

    /**
     * 사용자(userId)가 작성한 게시글을 가져옵니다.
     * @param postId: postId보다 작은 id를 가진 게시글을 가져옵니다.
     * @param cnt: 가져오는 게시글의 최대 개수 입니다.
     */
    public List<Post> readPostListByUserId(Long userId, Long postId, int cnt) {
        return query
                .selectFrom(post)
                .where(post.id.loe(postId), post.block.eq(false), post.userId.eq(userId))
                .orderBy(post.id.desc())
                .limit(cnt)
                .fetch();
    }

    /**
     * postId에 해당하는 게시글의 글을 content로 수정합니다.
     */
    public void modify(Long postId, String content) {
        Post findPost = readPost(postId);
        findPost.setContent(content);
    }

    /**
     * postId에 해당하는 게시글을 삭제합니다.
     */
    public void delete(Long postId) {
        Post findPost = readPost(postId);
        em.remove(findPost);
    }

    /**
     * user와 postId에 해당하는 게시글의 추천 상태를 recommend로 변경합니다.
     */
    public void setRecommend(Long postId, Long userId, int recommend) {
        Post findPost = readPost(postId);
        PostState postState = readPostState(postId, userId);
        if (postState==null) {
            postState = new PostState(postId, findPost.getUserId(), userId, 0, 0);
            em.persist(postState);
        }
        postState.setRecommend(recommend);
        findPost.setRecommend(findPost.getRecommend()+((recommend==1)?1:-1));
    }

    /**
     * user와 postId에 해당하는 게시글의 신고 상태를 report로 변경합니다.
     */
    public void setReport(Long postId, Long userId, int report) {
        Post findPost = readPost(postId);
        PostState postState = readPostState(postId, userId);
        if (postState==null) {
            postState = new PostState(postId, findPost.getUserId(), userId, 0, 0);
            em.persist(postState);
        }
        postState.setReport(report);
        findPost.setRecommend(findPost.getReport()+((report==1)?1:-1));
    }

    /**
     * user가 postId에 해당하는 게시글을 추천합니다.
     * @deprecated 프론트엔드의 추천/신고를 PUT방식으로 변경후 제거 예정입니다.
     */
    @Deprecated
    public void addRecommend(Long postId, Long userId) {
        Post findPost = readPost(postId);
        PostState postState = readPostState(postId, userId);

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
    /**
     * user가 postId에 해당하는 게시글의 추천을 취소합니다.
     * @deprecated 프론트엔드의 추천/신고를 PUT방식으로 변경후 제거 예정입니다.
     */
    @Deprecated
    public void deleteRecommend(Long postId, Long userId) {
        Post findPost = readPost(postId);
        PostState findPostState = readPostState(postId, userId);

        if (findPostState == null) return;

        try{
            if (findPostState.getRecommend() == 0 && findPostState.getReport() == 0){
                em.remove(findPostState);
            }

            if (findPostState.getRecommend() == 1){
                findPostState.setRecommend(0);
                findPost.setRecommend(findPost.getRecommend()-1);
            }
        }
        catch(Exception e){
            e.printStackTrace();
        }
    }
    /**
     * user가 postId에 해당하는 게시글을 신고합니다.
     * @deprecated 프론트엔드의 추천/신고를 PUT방식으로 변경후 제거 예정입니다.
     */
    @Deprecated
    public void addReport(Long postId, Long userId) {
        Post findPost = readPost(postId);
        PostState postState = readPostState(postId, userId);

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
    /**
     * user가 postId에 해당하는 게시글의 신고를 취소합니다.
     * @deprecated 프론트엔드의 추천/신고를 PUT방식으로 변경후 제거 예정입니다.
     */
    @Deprecated
    public void deleteReport(Long postId, Long userId) {
        Post findPost = readPost(postId);
        PostState findPostState = readPostState(postId, userId);

        if (findPostState == null) return;
        if (findPostState.getReport() == 1){
            findPostState.setReport(0);
            findPost.setReport(findPost.getReport()-1);
        }
        if (findPostState.getRecommend() == 0 && findPostState.getReport() == 0){
            em.remove(findPostState);
        }
    }

    /**
     * postId와 userId에 해당하는 추천/신고 정보를 가져옵니다.
     */
    public PostState readPostState(Long postId, Long userId){
        String jpql = "SELECT p FROM PostState AS p WHERE p.userId = :userId AND p.postId = :postId";
        TypedQuery<PostState> query = em.createQuery(jpql, PostState.class);
        query.setParameter("userId", userId);
        query.setParameter("postId", postId);
        List<PostState> resultList = query.getResultList();
        if (resultList.isEmpty()) return null;
        return resultList.get(0);
    }

    /**
     * 사용자가 당일 작성한 게시글의 수를 계산합니다.
     */
    public Long countPostToday(Long userId){
        List<Long> result = query
                .select(post.count())
                .from(post)
                .where(post.userId.eq(userId), post.uploadDate.goe(getDate()))
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

    /**
     * keyword가 포함된 게시글을 가져옵니다.
     * @param postId: postId보다 작은 id를 가진 게시글을 가져옵니다.
     * @param cnt: 가져오는 글의 최대 개수입니다.
     */
    public List<Post> readPostListByKeyword(Long postId, int cnt, String keyword) {
        return query
                .selectFrom(post)
                .where(post.content.contains(keyword), post.id.loe(postId), post.block.eq(false))
                .orderBy(post.id.desc())
                .limit(cnt)
                .fetch();
    }
}