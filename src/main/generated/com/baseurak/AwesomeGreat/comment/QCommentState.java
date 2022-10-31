package com.baseurak.AwesomeGreat.comment;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QCommentState is a Querydsl query type for CommentState
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QCommentState extends EntityPathBase<CommentState> {

    private static final long serialVersionUID = -1774993380L;

    public static final QCommentState commentState = new QCommentState("commentState");

    public final NumberPath<Long> commentId = createNumber("commentId", Long.class);

    public final NumberPath<Long> commentUserId = createNumber("commentUserId", Long.class);

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final NumberPath<Integer> recommend = createNumber("recommend", Integer.class);

    public final NumberPath<Integer> report = createNumber("report", Integer.class);

    public final NumberPath<Long> userId = createNumber("userId", Long.class);

    public QCommentState(String variable) {
        super(CommentState.class, forVariable(variable));
    }

    public QCommentState(Path<? extends CommentState> path) {
        super(path.getType(), path.getMetadata());
    }

    public QCommentState(PathMetadata metadata) {
        super(CommentState.class, metadata);
    }

}

