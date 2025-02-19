package com.sparta.spartaproject.domain.food;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QFood is a Querydsl query type for Food
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QFood extends EntityPathBase<Food> {

    private static final long serialVersionUID = 1106868794L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QFood food = new QFood("food");

    public final com.sparta.spartaproject.domain.QBaseEntity _super = new com.sparta.spartaproject.domain.QBaseEntity(this);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdAt = _super.createdAt;

    //inherited
    public final NumberPath<Long> createdBy = _super.createdBy;

    public final DateTimePath<java.time.LocalDateTime> deletedAt = createDateTime("deletedAt", java.time.LocalDateTime.class);

    public final StringPath description = createString("description");

    public final ComparablePath<java.util.UUID> id = createComparable("id", java.util.UUID.class);

    public final StringPath imagePath = createString("imagePath");

    public final BooleanPath isDeleted = createBoolean("isDeleted");

    public final BooleanPath isDisplayed = createBoolean("isDisplayed");

    public final StringPath name = createString("name");

    public final NumberPath<Integer> price = createNumber("price", Integer.class);

    public final EnumPath<Status> status = createEnum("status", Status.class);

    public final com.sparta.spartaproject.domain.store.QStore store;

    //inherited
    public final DateTimePath<java.time.LocalDateTime> updatedAt = _super.updatedAt;

    //inherited
    public final NumberPath<Long> updatedBy = _super.updatedBy;

    public QFood(String variable) {
        this(Food.class, forVariable(variable), INITS);
    }

    public QFood(Path<? extends Food> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QFood(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QFood(PathMetadata metadata, PathInits inits) {
        this(Food.class, metadata, inits);
    }

    public QFood(Class<? extends Food> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.store = inits.isInitialized("store") ? new com.sparta.spartaproject.domain.store.QStore(forProperty("store"), inits.get("store")) : null;
    }

}

