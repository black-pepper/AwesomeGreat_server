package com.baseurak.AwesomeGreat.post;

import com.baseurak.AwesomeGreat.exception.NotFoundException;
import com.baseurak.AwesomeGreat.user.Role;
import com.baseurak.AwesomeGreat.user.User;
import com.baseurak.AwesomeGreat.user.UserInfo;
import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest
@Transactional
@Slf4j
class PostServiceTest {

    @Autowired
    PostService postService;
    User user;
    List<Post> posts = new ArrayList<>();

    @BeforeEach
    public void beforeEach(){
        user = new User();
        user.setId(0L);
        user.setDemerit(0);
        user.setRole(Role.USER);
        UserInfo userInfo = new UserInfo(user);
        userInfo.setPostToday(0L);

        Post post1 = postService.write("TEST1", userInfo);
        Post post2 = postService.write("TEST2", userInfo);
        Post post3 = postService.write("TEST3", userInfo);

        posts.add(post3);
        posts.add(post2);
        posts.add(post1);
    }

    @Test
    void read() {
        List<Post> findPosts = postService.read(999999L, 3);
        assertThat(findPosts).isEqualTo(posts);
//        for (Post post : posts) {
//            System.out.println("post " + post);
//        }
    }

    @Test
    void modify() {
        Post post = posts.get(0);
        postService.modify(post.getId(), "MODIFY", user);
        //log.info("postId = {}", post.getId());
        Post findPost = postService.read(post.getId());
        //log.info("findPostId = {}", findPost.getId());
        assertThat(findPost.getContent()).isEqualTo("MODIFY");
    }

    @Test
    void delete() {
        Post post = posts.get(0);
        postService.delete(post.getId(), user);
        assertThatThrownBy(() -> postService.read(post.getId()))
                .isInstanceOf(NotFoundException.class);
    }
}