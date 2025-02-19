package com.sparta.spartaproject.mapper;

import com.sparta.spartaproject.domain.like.Like;
import com.sparta.spartaproject.domain.user.User;
import com.sparta.spartaproject.dto.request.CreateLikeRequestDto;
import com.sparta.spartaproject.dto.response.LikeDto;
import java.time.LocalDateTime;
import java.util.UUID;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-02-18T21:35:09+0900",
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
        UUID id = null;
        UUID storeId = null;
        Boolean isDeleted = null;
        LocalDateTime createdAt = null;
        LocalDateTime updatedAt = null;

        userId = likeUserId( like );
        id = like.getId();
        storeId = like.getStoreId();
        isDeleted = like.getIsDeleted();
        createdAt = like.getCreatedAt();
        updatedAt = like.getUpdatedAt();

        LikeDto likeDto = new LikeDto( id, userId, storeId, isDeleted, createdAt, updatedAt );

        return likeDto;
    }

    @Override
    public Like toLike(CreateLikeRequestDto request, User user) {
        if ( request == null && user == null ) {
            return null;
        }

        Like.LikeBuilder<?, ?> like = Like.builder();

        if ( request != null ) {
            like.storeId( request.storeId() );
        }
        if ( user != null ) {
            like.user( user );
            like.createdAt( user.getCreatedAt() );
            like.updatedAt( user.getUpdatedAt() );
            like.isDeleted( user.getIsDeleted() );
            like.deletedAt( user.getDeletedAt() );
        }

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
}
