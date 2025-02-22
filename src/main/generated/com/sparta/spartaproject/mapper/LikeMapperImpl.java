package com.sparta.spartaproject.mapper;

import com.sparta.spartaproject.domain.like.Like;
import com.sparta.spartaproject.domain.store.Store;
import com.sparta.spartaproject.domain.user.User;
import com.sparta.spartaproject.dto.response.LikeDto;
import java.time.LocalDateTime;
import java.util.UUID;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-02-22T22:30:15+0900",
    comments = "version: 1.5.5.Final, compiler: javac, environment: Java 17.0.14 (Oracle Corporation)"
)
@Component
public class LikeMapperImpl implements LikeMapper {

    @Override
    public LikeDto toLikeDto(Like like) {
        if ( like == null ) {
            return null;
        }

        Long userId = null;
        UUID storeId = null;
        UUID id = null;
        Boolean isDeleted = null;
        LocalDateTime createdAt = null;
        LocalDateTime updatedAt = null;

        userId = likeUserId( like );
        storeId = likeStoreId( like );
        id = like.getId();
        isDeleted = like.getIsDeleted();
        createdAt = like.getCreatedAt();
        updatedAt = like.getUpdatedAt();

        LikeDto likeDto = new LikeDto( id, userId, storeId, isDeleted, createdAt, updatedAt );

        return likeDto;
    }

    @Override
    public Like toLike(Store store, User user) {
        if ( store == null && user == null ) {
            return null;
        }

        Like.LikeBuilder<?, ?> like = Like.builder();

        like.store( store );
        like.user( user );

        return like.build();
    }

    private Long likeUserId(Like like) {
        if ( like == null ) {
            return null;
        }
        User user = like.getUser();
        if ( user == null ) {
            return null;
        }
        Long id = user.getId();
        if ( id == null ) {
            return null;
        }
        return id;
    }

    private UUID likeStoreId(Like like) {
        if ( like == null ) {
            return null;
        }
        Store store = like.getStore();
        if ( store == null ) {
            return null;
        }
        UUID id = store.getId();
        if ( id == null ) {
            return null;
        }
        return id;
    }
}
