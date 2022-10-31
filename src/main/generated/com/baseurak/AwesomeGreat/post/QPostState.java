package com.baseurak.AwesomeGreat.post;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QPostState is a Querydsl query type for PostState
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QPostState extends EntityPathBase<PostState> {

    private static final long serialVersionUID = 908089214L;

    public static final QPostState postState = new QPostState("postState");

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final NumberPath<Long> postId = createNumber("postId", Long.class);

    public final NumberPath<Long> postUserId = createNumber("postUserId", Long.class);

    public final NumberPath<Integer> recommend = createNumber("recommend", Integer.class);

    public final NumberPath<Integer> report = createNumber("report", Integer.class);

    public final NumberPath<Long> userId = createNumber("userId", Long.class);

    public QPostState(String variable) {
        super(PostState.class, forVariable(variable));
    }

    public QPostState(Path<? extends PostState> path) {
        super(path.getType(), path.getMetadata());
    }

    public QPostState(PathMetadata metadata) {
        super(PostState.class, metadata);
    }

}

