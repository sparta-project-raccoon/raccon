package com.sparta.spartaproject.domain.pay;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QPayHistory is a Querydsl query type for PayHistory
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QPayHistory extends EntityPathBase<PayHistory> {

    private static final long serialVersionUID = 1943193926L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QPayHistory payHistory = new QPayHistory("payHistory");

    public final com.sparta.spartaproject.domain.QBaseEntity _super = new com.sparta.spartaproject.domain.QBaseEntity(this);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdAt = _super.createdAt;

    //inherited
    public final NumberPath<Long> createdBy = _super.createdBy;

    public final ComparablePath<java.util.UUID> id = createComparable("id", java.util.UUID.class);

    public final com.sparta.spartaproject.domain.order.QOrder order;

    public final EnumPath<com.sparta.spartaproject.domain.order.PaymentMethod> payMethod = createEnum("payMethod", com.sparta.spartaproject.domain.order.PaymentMethod.class);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> updatedAt = _super.updatedAt;

    //inherited
    public final NumberPath<Long> updatedBy = _super.updatedBy;

    public final com.sparta.spartaproject.domain.user.QUser user;

    public QPayHistory(String variable) {
        this(PayHistory.class, forVariable(variable), INITS);
    }

    public QPayHistory(Path<? extends PayHistory> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QPayHistory(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QPayHistory(PathMetadata metadata, PathInits inits) {
        this(PayHistory.class, metadata, inits);
    }

    public QPayHistory(Class<? extends PayHistory> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.order = inits.isInitialized("order") ? new com.sparta.spartaproject.domain.order.QOrder(forProperty("order"), inits.get("order")) : null;
        this.user = inits.isInitialized("user") ? new com.sparta.spartaproject.domain.user.QUser(forProperty("user")) : null;
    }

}

