package com.sparta.spartaproject.domain.gemini;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QGeminiHistory is a Querydsl query type for GeminiHistory
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QGeminiHistory extends EntityPathBase<GeminiHistory> {

    private static final long serialVersionUID = -491267366L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QGeminiHistory geminiHistory = new QGeminiHistory("geminiHistory");

    public final com.sparta.spartaproject.domain.QBaseTimeEntity _super = new com.sparta.spartaproject.domain.QBaseTimeEntity(this);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdAt = _super.createdAt;

    public final ComparablePath<java.util.UUID> id = createComparable("id", java.util.UUID.class);

    public final StringPath requestText = createString("requestText");

    public final StringPath responseText = createString("responseText");

    public final NumberPath<Integer> statusCode = createNumber("statusCode", Integer.class);

    public final com.sparta.spartaproject.domain.store.QStore store;

    //inherited
    public final DateTimePath<java.time.LocalDateTime> updatedAt = _super.updatedAt;

    public final com.sparta.spartaproject.domain.user.QUser user;

    public QGeminiHistory(String variable) {
        this(GeminiHistory.class, forVariable(variable), INITS);
    }

    public QGeminiHistory(Path<? extends GeminiHistory> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QGeminiHistory(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QGeminiHistory(PathMetadata metadata, PathInits inits) {
        this(GeminiHistory.class, metadata, inits);
    }

    public QGeminiHistory(Class<? extends GeminiHistory> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.store = inits.isInitialized("store") ? new com.sparta.spartaproject.domain.store.QStore(forProperty("store"), inits.get("store")) : null;
        this.user = inits.isInitialized("user") ? new com.sparta.spartaproject.domain.user.QUser(forProperty("user")) : null;
    }

}

