package com.sparta.spartaproject.domain.category;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QCategory is a Querydsl query type for Category
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QCategory extends EntityPathBase<Category> {

    private static final long serialVersionUID = 690612794L;

    public static final QCategory category = new QCategory("category");

    public final com.sparta.spartaproject.domain.QBaseTimeEntity _super = new com.sparta.spartaproject.domain.QBaseTimeEntity(this);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdAt = _super.createdAt;

    public final ComparablePath<java.util.UUID> id = createComparable("id", java.util.UUID.class);

    public final StringPath name = createString("name");

    public final ListPath<com.sparta.spartaproject.domain.store.StoreCategory, com.sparta.spartaproject.domain.store.QStoreCategory> storeCategories = this.<com.sparta.spartaproject.domain.store.StoreCategory, com.sparta.spartaproject.domain.store.QStoreCategory>createList("storeCategories", com.sparta.spartaproject.domain.store.StoreCategory.class, com.sparta.spartaproject.domain.store.QStoreCategory.class, PathInits.DIRECT2);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> updatedAt = _super.updatedAt;

    public QCategory(String variable) {
        super(Category.class, forVariable(variable));
    }

    public QCategory(Path<? extends Category> path) {
        super(path.getType(), path.getMetadata());
    }

    public QCategory(PathMetadata metadata) {
        super(Category.class, metadata);
    }

}

