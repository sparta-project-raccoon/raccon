package com.sparta.spartaproject.mapper;

import com.sparta.spartaproject.domain.image.store.StoreImage;
import com.sparta.spartaproject.dto.response.ImageInfoDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ImageMapper {

    @Mapping(target = "imageId", source = "id")
    ImageInfoDto toImageInfoDto(StoreImage storeImage);

}
