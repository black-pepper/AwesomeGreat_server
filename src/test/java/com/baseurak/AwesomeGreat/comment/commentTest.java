package com.baseurak.AwesomeGreat.comment;

import com.baseurak.AwesomeGreat.post.Post;
import com.baseurak.AwesomeGreat.post.PostService;
import com.baseurak.AwesomeGreat.user.Role;
import com.baseurak.AwesomeGreat.user.User;
import com.baseurak.AwesomeGreat.user.UserInfo;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@Slf4j
@SpringBootTest
@Transactional
public class commentTest {

    @Autowired
    PostService postService;
    @Autowired
    CommentService commentService;

    User user;
    Post post;
    List<Comment> comments = new ArrayList<>();
    List<CommentDto> commentDtos = new ArrayList<>();

    @BeforeEach
    public void beforeEach() {
        user = new User();
        user.setId(0L);
        user.setDemerit(0);
        user.setRole(Role.USER);
        UserInfo userInfo = new UserInfo(user);
        userInfo.setPostToday(0L);
        userInfo.setCommentToday(0L);

        post = postService.write("COMMENT TEST", userInfo);

        Long postId = post.getId();

        Comment comment1 = commentService.write(postId, "TEST1", userInfo);
        Comment comment2 = commentService.write(postId, "TEST2", userInfo);
        Comment comment3 = commentService.write(postId, "TEST3", userInfo);

        comments.add(comment1);
        comments.add(comment2);
        comments.add(comment3);

        commentDtos.add(new CommentDto(comment1));
        commentDtos.add(new CommentDto(comment2));
        commentDtos.add(new CommentDto(comment3));
    }

    @Test
    void read() {
        List<CommentDto> findComments = commentService.readCommentDtoList(post.getId(), user);
        assertThat(findComments).isEqualTo(commentDtos);
    }

    @Test
    void modify() {
        CommentDto commentDto = commentDtos.get(0);
        commentService.modify(commentDto.getId(), "MODIFY", user);
        CommentDto findCommentDto = commentService.readCommentDtoList(post.getId(), user).get(0);
        assertThat(findCommentDto.getContent()).isEqualTo("MODIFY");
    }

    @Test
    void delete() {
        CommentDto commentDto = commentDtos.get(0);
        commentService.delete(commentDto.getId(), user);
        CommentDto findCommentDto = commentService.readCommentDtoList(post.getId(), user).get(0);
        assertThat(findCommentDto.getContent()).isEqualTo("TEST2");
    }
}