package com.baseurak.AwesomeGreat.nickname;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QNickname is a Querydsl query type for Nickname
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QNickname extends EntityPathBase<Nickname> {

    private static final long serialVersionUID = 219387027L;

    public static final QNickname nickname1 = new QNickname("nickname1");

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final StringPath nickname = createString("nickname");

    public final NumberPath<Integer> sequence = createNumber("sequence", Integer.class);

    public QNickname(String variable) {
        super(Nickname.class, forVariable(variable));
    }

    public QNickname(Path<? extends Nickname> path) {
        super(path.getType(), path.getMetadata());
    }

    public QNickname(PathMetadata metadata) {
        super(Nickname.class, metadata);
    }

}

