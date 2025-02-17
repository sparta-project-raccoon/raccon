package com.sparta.spartaproject.mapper;

import com.sparta.spartaproject.domain.store.StoreImage;
import com.sparta.spartaproject.dto.response.ImageInfoDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ImageMapper {

    @Mapping(target = "imageId", source = "id")
    ImageInfoDto toImageInfoDtoFromStore(StoreImage storeImage);

    @Mapping(target = "imageId", source = "id")
    ImageInfoDto toImageInfoDtoFromReview(ReviewImage reviewImage);
}
