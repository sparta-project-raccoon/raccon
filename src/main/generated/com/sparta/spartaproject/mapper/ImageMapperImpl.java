package com.sparta.spartaproject.mapper;

import com.sparta.spartaproject.domain.store.StoreImage;
import com.sparta.spartaproject.dto.response.ImageInfoDto;
import java.util.UUID;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-02-23T11:04:24+0900",
    comments = "version: 1.5.5.Final, compiler: javac, environment: Java 17.0.14 (Oracle Corporation)"
)
@Component
public class ImageMapperImpl implements ImageMapper {

    @Override
    public ImageInfoDto toImageInfoDtoFromStore(StoreImage storeImage) {
        if ( storeImage == null ) {
            return null;
        }

        UUID imageId = null;
        String path = null;

        imageId = storeImage.getId();
        path = storeImage.getPath();

        ImageInfoDto imageInfoDto = new ImageInfoDto( imageId, path );

        return imageInfoDto;
    }
}
